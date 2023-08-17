package com.fly.controller;

import com.fly.service.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Resource
    private MailService mailService;

    @GetMapping("/sendEmail")
    public String sendMail(){

        return "";
    }
}
