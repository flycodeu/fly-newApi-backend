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

    }
}