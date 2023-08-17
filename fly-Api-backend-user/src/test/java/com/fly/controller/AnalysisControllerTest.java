package com.fly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fly.common.ErrorCode;
import com.fly.exception.BusinessException;
import com.fly.mapper.UserInterfaceInfoMapper;
import com.fly.service.impl.InterfaceInfoServiceImpl;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.vo.InterfaceInfoVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnalysisControllerTest {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Resource
    private InterfaceInfoServiceImpl interfaceInfoService;

    @Test
    void getTopInvokeInterfaceInfoVo() {

        List<InterfaceInfoVo> userInterfaceInfos = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);

        Map<Long, List<InterfaceInfoVo>> map = userInterfaceInfos.stream().collect(Collectors.groupingBy(InterfaceInfoVo::getInterfaceInfoId));

        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", map.keySet());
        List<InterfaceInfoNew> interfaceInfoNewList = interfaceInfoService.list(queryWrapper);

        if (CollectionUtils.isEmpty(interfaceInfoNewList)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        List<InterfaceInfoVo> interfaceInfoVoList = interfaceInfoNewList.stream().map(interfaceInfoNew -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            String name = interfaceInfoNew.getName();
            interfaceInfoVo.setInterfaceInfoName(name);
            interfaceInfoVo.setInterfaceInfoId(interfaceInfoNew.getId());
            interfaceInfoVo.setAllInvokeNum(map.get(interfaceInfoNew.getId()).get(0).getAllInvokeNum());

            return interfaceInfoVo;
        }).collect(Collectors.toList());

        interfaceInfoVoList.forEach(System.out::println);

//        Map<Long, List<InterfaceInfoVo>> map = userInterfaceInfos.stream().collect(Collectors.groupingBy(InterfaceInfoVo::getId));
//
//        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("id", map.keySet());
//        List<InterfaceInfoNew> interfaceInfoNewList = interfaceInfoService.list(queryWrapper);
//
//        if (CollectionUtils.isEmpty(interfaceInfoNewList)) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
//        }
//
//        List<InterfaceInfoVo> interfaceInfoVoList = interfaceInfoNewList
//                .stream()
//                .map(interfaceInfoNew -> {
//                    InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
//                    BeanUtils.copyProperties(interfaceInfoNew, interfaceInfoVo);
//                    Integer allInvokeNum = map.get(interfaceInfoNew.getId()).get(0).getAllInvokeNum();
//                    interfaceInfoVo.setAllInvokeNum(allInvokeNum);
//                    return interfaceInfoVo;
//                }).collect(Collectors.toList());
//
//        System.out.println(interfaceInfoVoList);
    }
}