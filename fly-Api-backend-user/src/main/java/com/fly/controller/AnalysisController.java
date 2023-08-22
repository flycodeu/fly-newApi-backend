package com.fly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fly.annotation.AuthCheck;
import com.fly.common.ErrorCode;
import com.fly.constant.UserConstant;
import com.fly.exception.BusinessException;
import com.fly.mapper.UserInterfaceInfoMapper;
import com.fly.service.impl.InterfaceInfoServiceImpl;
import com.fly.service.impl.UserServiceImpl;
import com.fly.utils.RedisConstants;
import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ResultUtils;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.vo.InterfaceInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 分析页
 *
 * @author fly
 */

@RequestMapping( "/analysis" )
@RestController
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Resource
    private InterfaceInfoServiceImpl interfaceInfoService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserServiceImpl userService;

    /**
     * 分析前top个数据
     *
     * @return
     */
    @GetMapping( "/top" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<List<InterfaceInfoVo>> getTopInvokeInterfaceInfoVo(int limit) {
        String key = RedisConstants.COUNT_LIST_INTERFACE;
        List<InterfaceInfoVo> infoVoList = (List<InterfaceInfoVo>) redisTemplate.opsForValue().get(key);
        if (infoVoList != null) {
            return ResultUtils.success(infoVoList);
        }

        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(limit);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoObjMap = userInterfaceInfoList.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoObjMap.keySet());
        List<InterfaceInfoNew> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(interfaceInfoList)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        infoVoList = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVo);
            int totalNum = interfaceInfoObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVo.setTotalNum(totalNum);
            return interfaceInfoVo;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, infoVoList, RedisConstants.COUNT_LIST_INTERFACE_TIME, TimeUnit.MINUTES);

        return ResultUtils.success(infoVoList);
    }

    /**
     * 查看每月用户注册情况
     *
     * @return
     */
    @GetMapping( "/registerCount" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<List<Map<String, Object>>> getUserRegisterOrderByMonth() {
        List<Map<String, Object>> userRegisterOrderByMonth = userService.getUserRegisterOrderByMonth();
        return ResultUtils.success(userRegisterOrderByMonth);
    }


}
