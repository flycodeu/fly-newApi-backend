package com.fly.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.request.Interface.InterfaceInfoUserQueryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InterfaceInfoServiceImplTest {
    @Resource
    private InterfaceInfoServiceImpl interfaceInfoService;
    @Test
    void getUserInterfaceInfoByPage() {
        InterfaceInfoUserQueryRequest interfaceInfoUserQueryRequest = new InterfaceInfoUserQueryRequest();
        interfaceInfoUserQueryRequest.setUserId(6L);
        Page<InterfaceInfoNew> userInterfaceInfoByPage = interfaceInfoService.getUserInterfaceInfoByPage(interfaceInfoUserQueryRequest);
        List<InterfaceInfoNew> records =
                userInterfaceInfoByPage.getRecords();
        System.out.println(records);
    }
}