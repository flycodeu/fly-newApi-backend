package com.flyinterface.controller;

import com.flyinterface.config.YuAiConfig;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping( "/ai" )
public class YuCongMingController {
    @Resource
    private YuAiConfig yuAiConfig;

    @PostMapping( "/chat" )
    public void zelinAIChat() {
        String result = yuAiConfig.doChart(1651468516836098050L, "你是谁");
        System.out.println(result);
    }
}
