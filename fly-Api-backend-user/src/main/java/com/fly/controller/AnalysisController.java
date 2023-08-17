package com.fly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fly.annotation.AuthCheck;
import com.fly.common.ErrorCode;
import com.fly.constant.UserConstant;
import com.fly.exception.BusinessException;
import com.fly.mapper.UserInterfaceInfoMapper;
import com.fly.service.impl.InterfaceInfoServiceImpl;
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

    /**
     * 分析前top个数据
     *
     * @return
     */
    @GetMapping( "/top" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<List<InterfaceInfoVo>> getTopInvokeInterfaceInfoVo(int limit) {
        String key = RedisConstants.TOP_LIST_INTERFACE + limit;
        List<InterfaceInfoVo> interfaceInfoVoList;
        interfaceInfoVoList = (List<InterfaceInfoVo>) redisTemplate.opsForValue().get(key);
        if (interfaceInfoVoList != null) {
            return ResultUtils.success(interfaceInfoVoList);
        }

        // 返回InterfaceInfoVo集合
        List<InterfaceInfoVo> userInterfaceInfos = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(limit);
        // 将集合转换为map
        Map<Long, List<InterfaceInfoVo>> map = userInterfaceInfos.stream().collect(Collectors.groupingBy(InterfaceInfoVo::getInterfaceInfoId));
        // 获取到接口里面的所有符合的id
        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", map.keySet());
        List<InterfaceInfoNew> interfaceInfoNewList = interfaceInfoService.list(queryWrapper);

        if (CollectionUtils.isEmpty(interfaceInfoNewList)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        // 给interfaceInfoVo赋值
        interfaceInfoVoList = interfaceInfoNewList.stream().map(interfaceInfoNew -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            String name = interfaceInfoNew.getName();
            interfaceInfoVo.setInterfaceInfoName(name);
            interfaceInfoVo.setInterfaceInfoId(interfaceInfoNew.getId());
            interfaceInfoVo.setAllInvokeNum(map.get(interfaceInfoNew.getId()).get(0).getAllInvokeNum());

            return interfaceInfoVo;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, interfaceInfoVoList);

        return ResultUtils.success(interfaceInfoVoList);
    }


}
