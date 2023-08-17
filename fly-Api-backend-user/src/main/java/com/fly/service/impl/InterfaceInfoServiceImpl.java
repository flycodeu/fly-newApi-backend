package com.fly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fly.constant.CommonConstant;
import com.fly.exception.BusinessException;
import com.fly.mapper.InterfaceInfoMapper;
import com.fly.service.InterfaceInfoService;
import com.fly.service.UserService;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.request.UserInterface.UserInterfaceInfoCanAccess;
import com.flySdk.client.FlyApiClient;
import com.fly.common.ErrorCode;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.enums.InterfaceInfoStatusEnum;
import com.flyCommon.model.request.DeleteRequest;
import com.flyCommon.model.request.IdRequest;
import com.flyCommon.model.request.Interface.InterfaceInfoAddRequest;
import com.flyCommon.model.request.Interface.InterfaceInfoInvokeRequest;
import com.flyCommon.model.request.Interface.InterfaceInfoQueryRequest;
import com.flyCommon.model.request.Interface.InterfaceInfoUpdateRequest;
import com.flyCommon.model.vo.UserVO;
import com.fly.utils.ThrowUtils;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author admin
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-07-13 18:26:14
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfoNew>
        implements InterfaceInfoService {
    private static final String IPV4_PATTERN =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    @Resource
    private UserService userService;
    @Resource
    private FlyApiClient flyApiClient;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请填写完整信息");
        }
        String token = interfaceInfoAddRequest.getToken();
        InterfaceInfoNew interfaceInfoNew = new InterfaceInfoNew();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfoNew);
        this.validInterfaceInfo(interfaceInfoNew, true);
        UserVO loginUserRedis = userService.getLoginUserRedis(token);
        Long userId = loginUserRedis.getId();
        interfaceInfoNew.setUserId(userId);
        boolean save = this.save(interfaceInfoNew);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建失败");
        }
        return interfaceInfoNew.getId();
    }

    @Override
    public Boolean deleteInterfaceInfo(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除条件为空");
        }
        Long id = deleteRequest.getId();
        boolean b = this.removeById(id);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    @Override
    public void validInterfaceInfo(InterfaceInfoNew interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String ipAddress = interfaceInfo.getIPAddress();
        Integer port = interfaceInfo.getPort();

        Integer status = interfaceInfo.getStatus();
        String method = interfaceInfo.getMethod();

        if (add) {
            if (StringUtils.isAllBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "调用名过长");
        }

        if (StringUtils.isNotBlank(description) && description.length() > 8102) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }

        if (String.valueOf(port).length() > 6 || String.valueOf(port).length() < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "端口号不符合规定");
        }

        if (url == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "url不能为空");
        }

        if (!isValidIPAddress(ipAddress)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ip地址不正确");
        }

        if (interfaceInfo.getInvokeCount() > 1000) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "次数过多");
        }

        if (interfaceInfo.getInvokeCount() < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "次数必须要为正值");
        }
//
//        if (interfaceInfo.getPrice() < 0) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "价格必须是正值");
//        }

    }

    @Override
    public Boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        InterfaceInfoNew interfaceInfoNew = new InterfaceInfoNew();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfoNew);
        // 检验
        validInterfaceInfo(interfaceInfoNew, false);
        Long id = interfaceInfoNew.getId();
        InterfaceInfoNew byId = this.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        boolean b = this.updateById(interfaceInfoNew);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return true;
    }

    @Override
    public InterfaceInfoNew getInterfaceInfoById(Long id) {
        InterfaceInfoNew byId = this.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }


        return this.getById(id);
    }

    @Override
    public List<InterfaceInfoNew> getAllInterfaceInfoByList(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfoNew interfaceInfoNew = new InterfaceInfoNew();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoNew);
        }
        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>(interfaceInfoNew);
        queryWrapper.orderByDesc("id");
        return this.list(queryWrapper);
    }

    @Override
    public Page<InterfaceInfoNew> getAllInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        //key
        String cacheKey = generateCacheKey(interfaceInfoQueryRequest);

        // Try to retrieve cached data from Redis
        Page<InterfaceInfoNew> cachedData = getCachedData(cacheKey);
        if (cachedData != null) {
            return cachedData;
        }

        // If not cached, perform the database query
        Page<InterfaceInfoNew> result = executeQuery(interfaceInfoQueryRequest);

        // Cache the result in Redis
        cacheData(cacheKey, result);

        return result;
    }

    // 生成key
    private String generateCacheKey(InterfaceInfoQueryRequest request) {
        return "interface_info_cache:" + request.toString();
    }
    // 获取数据
    private Page<InterfaceInfoNew> getCachedData(String cacheKey) {
        return (Page<InterfaceInfoNew>) redisTemplate.opsForValue().get(cacheKey);
    }
    // 缓存数据
    private void cacheData(String cacheKey, Page<InterfaceInfoNew> data) {

        redisTemplate.opsForValue().set(cacheKey, data, Duration.ofMinutes(10)); // Set a suitable timeout
    }

    private Page<InterfaceInfoNew> executeQuery(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        long current = interfaceInfoQueryRequest.getCurrent();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String name = interfaceInfoQueryRequest.getName();
        Integer port = interfaceInfoQueryRequest.getPort();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        String description = interfaceInfoQueryRequest.getDescription();

        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>();
        if (name != null) {
            queryWrapper.like("name", name);
        }
        if (port != null) {
            queryWrapper.eq("port", port);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (method != null) {
            queryWrapper.like("method", method);
        }
        if (description != null) {
            queryWrapper.like("description", description);
        }

        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        queryWrapper.orderByDesc("id");
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }
//    public Page<InterfaceInfoNew> getAllInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
//        if (interfaceInfoQueryRequest == null) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
//        }
//
//        InterfaceInfoNew interfaceInfoNew = new InterfaceInfoNew();
//        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoNew);
//        long pageSize = interfaceInfoQueryRequest.getPageSize();
//        long current = interfaceInfoQueryRequest.getCurrent();
//        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
//        String sortField = interfaceInfoQueryRequest.getSortField();
//        String name = interfaceInfoQueryRequest.getName();
//        Integer port = interfaceInfoQueryRequest.getPort();
//        Integer status = interfaceInfoQueryRequest.getStatus();
//        String method = interfaceInfoQueryRequest.getMethod();
//        String description = interfaceInfoQueryRequest.getDescription();
//        ThrowUtils.throwIf(pageSize > 20, ErrorCode.FORBIDDEN_ERROR, "请勿爬虫");
//
//        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>();
//        if (name != null) {
//            queryWrapper.like("name", name);
//        }
//        if (port != null) {
//            queryWrapper.eq("port", port);
//        }
//        if (status != null) {
//            queryWrapper.eq("status", status);
//        }
//        if (method != null) {
//            queryWrapper.like("method", method);
//        }
//        if (description != null) {
//            queryWrapper.like("description", description);
//        }
//
//        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
//                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
//        queryWrapper.orderByDesc("id");
//        return this.page(new Page<>(current, pageSize), queryWrapper);
//    }

    @Override
    public Boolean onLineInterfaceInfo(IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        InterfaceInfoNew interfaceInfoNew = this.getById(id);
        if (interfaceInfoNew == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // todo 判断是否可以调用
        com.flySdk.model.User user = new com.flySdk.model.User();
        user.setName("fly");
        String res = flyApiClient.getNameByPostJson(user);
        if (StringUtils.isBlank(res)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        // 修改上线
        InterfaceInfoNew interfaceInfoNew1 = new InterfaceInfoNew();
        interfaceInfoNew1.setId(id);
        interfaceInfoNew1.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean b = this.updateById(interfaceInfoNew1);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }

        return true;
    }

    @Override
    public Boolean offLineInterfaceInfo(IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        InterfaceInfoNew interfaceInfoNew = this.getById(id);
        if (interfaceInfoNew == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 修改上线
        InterfaceInfoNew interfaceInfoNew1 = new InterfaceInfoNew();
        interfaceInfoNew1.setId(id);
        interfaceInfoNew1.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean b = this.updateById(interfaceInfoNew1);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }

        return true;
    }

    @Override
    public Object invokeInterface(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long infoInvokeRequestId = interfaceInfoInvokeRequest.getId();
        InterfaceInfoNew interfaceInfoNew = this.getById(infoInvokeRequestId);
        if (interfaceInfoNew == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String requestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        Integer status = interfaceInfoNew.getStatus();
        if (status == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已下线");
        }
        UserVO loginUserRedis = userService.getLoginUserRedis(interfaceInfoInvokeRequest.getToken());
        if (loginUserRedis == null || loginUserRedis.getId() <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        com.flyCommon.model.entity.User user = userService.getById(loginUserRedis.getId());

        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        FlyApiClient flyApiClient2 = new FlyApiClient(accessKey, secretKey);

        Gson gson = new Gson();
        com.flySdk.model.User fromJson = gson.fromJson(requestParams, com.flySdk.model.User.class);

        return flyApiClient2.getNameByPostJson(fromJson);
    }


    /**
     * 正则检验ip地址
     *
     * @param ipAddress
     * @return
     */
    public static boolean isValidIPAddress(String ipAddress) {
        if ((ipAddress != null)) {
            if (ipAddress.equals("localhost")) {
                return true;
            } else {
                return Pattern.matches("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$", ipAddress);
            }
        }
        return false;
    }


}




