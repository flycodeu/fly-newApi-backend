package com.fly.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flyCommon.model.request.User.UserQueryRequest;
import com.flyCommon.model.request.UserInterface.UserInterfaceInfoVoRequest;
import com.flyCommon.model.vo.UserInterfaceInfoVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserInterfaceInfoServiceImplTest {
    @Resource
    private UserInterfaceInfoServiceImpl userInterfaceInfoService;
    @Test
    void getAllInterfaceInfoDetailByPage() {

        Page<UserInterfaceInfoVo> allInterfaceInfoDetailByPage = userInterfaceInfoService.getAllInterfaceInfoDetailByPage(new UserInterfaceInfoVoRequest());
        List<UserInterfaceInfoVo> records = allInterfaceInfoDetailByPage.getRecords();

        for (UserInterfaceInfoVo record : records) {
            System.out.println(record);
        }
    }
}