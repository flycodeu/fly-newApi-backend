package com.flyinterface;

import com.flySdk.client.FlyApiClient;
import com.flySdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class FlyApiInterfaceApplicationTests {
    @Resource
    private FlyApiClient flyApiClient;
    @Test
    void contextLoads() {
        String res1 = flyApiClient.getNameByGet("fly");

        User user = new User();
        user.setName("fly");
        String nameByPostJson = flyApiClient.getNameByPostJson(user);
        System.out.println(res1);
        System.out.println(nameByPostJson);
    }

}
