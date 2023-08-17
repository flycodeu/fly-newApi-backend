package com.fly.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserInterfaceInfoServiceTest {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Test
    void invokeCount() {
        Boolean aBoolean = userInterfaceInfoService.invokeCount(26L, 14L);
        System.out.println(aBoolean);
    }
}