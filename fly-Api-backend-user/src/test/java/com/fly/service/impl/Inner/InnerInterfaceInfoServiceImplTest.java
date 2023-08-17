package com.fly.service.impl.Inner;

import com.flyCommon.model.entity.InterfaceInfoNew;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class InnerInterfaceInfoServiceImplTest {

    @Resource
    private InnerInterfaceInfoServiceImpl interfaceInfoService;

    @Test
    void getInterfaceInfo() {
        InterfaceInfoNew post = interfaceInfoService.getInterfaceInfo("http://localhost:7550/name/user", "post", "");
        System.out.println(post);
    }
}