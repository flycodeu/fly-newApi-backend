package com.flyinterface;

import com.fly.flyapiclientsdk.client.FlyApiClient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class FlyApiInterfaceApplicationTests {
    @Resource
    private FlyApiClient flyApiClient;
    @Test
    void contextLoads() {
//        String res1 = flyApiClient.getNameByGet("fly");
//
//        User user = new User();
//        user.setName("fly");
//        String nameByPostJson = flyApiClient.getNameByPostJson(user);
//        System.out.println(res1);
//        System.out.println(nameByPostJson);
    }

}
