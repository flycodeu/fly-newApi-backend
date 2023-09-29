package com.fly.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.common.ErrorCode;
import com.fly.constant.CommonConstant;
import com.fly.exception.BusinessException;
import com.fly.mapper.UserInterfaceInfoMapper;
import com.fly.service.UserInterfaceInfoService;
import com.fly.utils.RedisConstants;
import com.fly.utils.ThrowUtils;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.User;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.request.DeleteRequest;
import com.flyCommon.model.request.UserInterface.*;
import com.flyCommon.model.vo.UserInterfaceInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author admin
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
 * @createDate 2023-07-13 18:17:14
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {
    @Resource
    private InterfaceInfoServiceImpl interfaceInfoService;

    private final Lock lock = new ReentrantLock();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserServiceImpl userService;

    @Override
    public Long addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest) {
        if (userInterfaceInfoAddRequest == null || userInterfaceInfoAddRequest.getInterfaceInfoId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceInfoId = userInterfaceInfoAddRequest.getInterfaceInfoId();
        Long userId = userInterfaceInfoAddRequest.getUserId();
        Integer totalNum = userInterfaceInfoAddRequest.getTotalNum();
        InterfaceInfoNew interfaceInfoById = interfaceInfoService.getInterfaceInfoById(interfaceInfoId);
        if (interfaceInfoById == null || userId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        Integer invokeCount = interfaceInfoById.getInvokeCount();
        if (invokeCount <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求次数不能小于0");
        }

        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
        userInterfaceInfo.setUserId(userId);
        userInterfaceInfo.setTotalNum(0);
        userInterfaceInfo.setLeftNum(invokeCount);
        userInterfaceInfo.setStatus(0);
        boolean save = this.save(userInterfaceInfo);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        return userInterfaceInfo.getId();
    }

    @Override
    public Boolean deleteInterfaceInfo(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return this.removeById(deleteRequest.getId());
    }

    @Override
    public Boolean updateInterfaceInfo(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        Long id = userInterfaceInfoUpdateRequest.getId();
        Integer status = userInterfaceInfoUpdateRequest.getStatus();
        Integer totalNum = userInterfaceInfoUpdateRequest.getTotalNum();
        Integer leftNum = userInterfaceInfoUpdateRequest.getLeftNum();
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setId(id);
        userInterfaceInfo.setStatus(status);
        userInterfaceInfo.setLeftNum(leftNum);
        validUserInterfaceInfo(userInterfaceInfo, false);
        return this.updateById(userInterfaceInfo);
    }

    @Override
    public UserInterfaceInfo getUserInterfaceInfoById(Long id) {
        UserInterfaceInfo byId = this.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        return byId;
    }

    @Override
    public List<UserInterfaceInfo> getAllInterfaceInfoByList(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfo);
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfo);
        queryWrapper.orderByDesc("id");
        return this.list(queryWrapper);
    }

    @Override
    public Page<UserInterfaceInfo> getAllInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        long begin = System.currentTimeMillis();
        String key = RedisConstants.USER_INTERFACE_LIST_PAGE + userInterfaceInfoQueryRequest;
        Page<UserInterfaceInfo> cacheData = (Page<UserInterfaceInfo>) redisTemplate.opsForValue().get(key);
        if (cacheData != null) {
            return cacheData;
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfo);
        long pageSize = userInterfaceInfoQueryRequest.getPageSize();
        long current = userInterfaceInfoQueryRequest.getCurrent();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.FORBIDDEN_ERROR, "请勿爬虫");
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfo);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        queryWrapper.orderByDesc("id");

        cacheData = this.page(new Page<>(current, pageSize), queryWrapper);
        redisTemplate.opsForValue().set(key, cacheData, RedisConstants.USER_INTERFACE_LIST_PAGE_TIME, TimeUnit.MINUTES);
//        log.error("cost===>" + (System.currentTimeMillis() - begin));
        return cacheData;
    }

    /**
     * 返回详细接口用户信息
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public Page<UserInterfaceInfoVo> getAllInterfaceInfoDetailByPage(UserInterfaceInfoVoRequest userInterfaceInfoQueryRequest) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        String key = RedisConstants.USER_INTERFACE_DETAIL_LIST_PAGE + userInterfaceInfoQueryRequest.getUserName() + ":" + userInterfaceInfoQueryRequest.getInterfaceInfoName() + ":" + userInterfaceInfoQueryRequest.getStatus() + ":" + userInterfaceInfoQueryRequest.getCurrent() + ":" + userInterfaceInfoQueryRequest.getPageSize();
        Page<UserInterfaceInfoVo> cacheData = (Page<UserInterfaceInfoVo>) redisTemplate.opsForValue().get(key);
        if (cacheData != null) {
            return cacheData;
        }

        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setUserId(userInterfaceInfoQueryRequest.getUserId());
        userInterfaceInfo.setInterfaceInfoId(userInterfaceInfoQueryRequest.getInterfaceInfoId());
        userInterfaceInfo.setStatus(userInterfaceInfoQueryRequest.getStatus());

        long pageSize = userInterfaceInfoQueryRequest.getPageSize();
        long current = userInterfaceInfoQueryRequest.getCurrent();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        ThrowUtils.throwIf(pageSize > 40, ErrorCode.FORBIDDEN_ERROR, "请勿爬虫");
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfo);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        queryWrapper.orderByDesc("id");

        Page<UserInterfaceInfo> page = this.page(new Page<>(current, pageSize), queryWrapper);
        long pageCurrent = page.getCurrent();
        long total = page.getTotal();

        List<UserInterfaceInfoVo> infoVoList = page.getRecords().stream().map(userInterfaceInfo1 -> {
            Long userId = userInterfaceInfo1.getUserId();
            Long interfaceInfoId = userInterfaceInfo1.getInterfaceInfoId();
            UserInterfaceInfoVo userInterfaceInfoVo = new UserInterfaceInfoVo();
            User user = userService.getById(userId);
            InterfaceInfoNew interfaceInfoNew = interfaceInfoService.getById(interfaceInfoId);
            userInterfaceInfoVo.setUserId(userId);
            userInterfaceInfoVo.setUserName(user.getUserName());
            userInterfaceInfoVo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfoVo.setInterfaceInfoName(interfaceInfoNew.getName());
            userInterfaceInfoVo.setLeftCount(userInterfaceInfo1.getLeftNum());
            userInterfaceInfoVo.setTotalCount(userInterfaceInfo1.getTotalNum());
            userInterfaceInfoVo.setId(userInterfaceInfo1.getId());
            userInterfaceInfoVo.setStatus(userInterfaceInfo1.getStatus());
            return userInterfaceInfoVo;
        }).collect(Collectors.toList());

        cacheData = new Page<>(pageCurrent, pageSize);
        cacheData.setRecords(infoVoList);
        cacheData.setTotal(total);

        redisTemplate.opsForValue().set(key, cacheData, RedisConstants.USER_INTERFACE_DETAIL_LIST_PAGE_TIME, TimeUnit.MINUTES);
        return cacheData;
    }

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer leftNum = userInterfaceInfo.getLeftNum();
        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        if (add) {
            if (id <= 0 || userId <= 0) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "请求数据不存在");
            }
        }

        if (leftNum < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据不能小于0");
        }

    }


    @Override
    public synchronized Boolean invokeCount(Long interfaceInfoId, Long userId) {
        if (interfaceInfoId == null || userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求为空");
        }
        lock.lock();
        // todo 加锁
        try {
            UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("interfaceInfoId", interfaceInfoId);
            updateWrapper.eq("userId", userId);
            updateWrapper.setSql("leftNum=leftNum-1,totalNum=totalNum+1");
            updateWrapper.gt("leftNum", 0);
            return this.update(updateWrapper);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Integer getUserInterfaceInfoInvokeCount(UserInterfaceInfoCount userInterfaceInfoCount) {
        if (userInterfaceInfoCount == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        Long userId = userInterfaceInfoCount.getUserId();
        Long interfaceInfoId = userInterfaceInfoCount.getInterfaceInfoId();
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        UserInterfaceInfo one = this.getOne(queryWrapper);
        if (one == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return one.getLeftNum();
    }

    @Override
    public Boolean addUserInterfaceInfoInToTable(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        long interfaceCount = interfaceInfoService.count();

        String key = RedisConstants.USER_INVOKE_COUNT_ACCESS + userId;
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        if (count != null && count == interfaceCount) {
            return false;
        }

        redisTemplate.opsForValue().set(key, interfaceCount);

        long userInterfaceCount = this.count(new QueryWrapper<UserInterfaceInfo>().eq("userId", userId));
        if (interfaceCount == userInterfaceCount) {
            return true;
        }

        List<Long> existingInterfaceIds = this.listObjs(new QueryWrapper<UserInterfaceInfo>()
                .select("interfaceInfoId")
                .eq("userId", userId), obj -> (Long) obj);

        List<InterfaceInfoNew> allInterfaceInfos = interfaceInfoService.list();
        List<UserInterfaceInfo> userInterfaceInfosToAdd = new ArrayList<>();

        for (InterfaceInfoNew interfaceInfo : allInterfaceInfos) {
            if (!existingInterfaceIds.contains(interfaceInfo.getId())) {
                UserInterfaceInfo newEntry = new UserInterfaceInfo();
                newEntry.setInterfaceInfoId(interfaceInfo.getId());
                newEntry.setUserId(userId);
                newEntry.setLeftNum(interfaceInfo.getInvokeCount());
                userInterfaceInfosToAdd.add(newEntry);
            }
        }
        if (!userInterfaceInfosToAdd.isEmpty()) {
            return this.saveBatch(userInterfaceInfosToAdd);
        } else {
            return true; // No new data to insert
        }

    }


}




