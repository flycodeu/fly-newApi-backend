package com.fly.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.utils.RedisConstants;
import com.flyCommon.model.request.User.UserQueryRequest;
import com.flyCommon.model.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void listPageUsers() {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setCurrent(1);
        userQueryRequest.setPageSize(20);
        long begin = System.currentTimeMillis();
        Page<UserVO> userVOPage = userService.listPageUsers(userQueryRequest);
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }
}