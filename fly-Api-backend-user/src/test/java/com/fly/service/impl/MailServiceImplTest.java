package com.fly.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailServiceImplTest {
    @Resource
    private MailServiceImpl mailService;


    @Test
    void sendEmail() {
        mailService.sendEmail();
    }

    @Test
    void sendMimeEmail() {
        mailService.sendMimeEmail();
    }
}