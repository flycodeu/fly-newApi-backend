package com.fly.service.impl.Inner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InnerUserInterfaceInfoServiceImplTest {

    @Resource
    private InnerUserInterfaceInfoServiceImpl interfaceInfoService;


    @Test
    void name() {
        Boolean aBoolean = interfaceInfoService.invokeCount(23L, 6L);
        System.out.println(aBoolean);
    }
}