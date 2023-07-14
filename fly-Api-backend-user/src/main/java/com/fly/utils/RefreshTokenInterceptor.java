package com.fly.utils;

import cn.hutool.core.bean.BeanUtil;
import com.fly.constant.UserConstant;
import com.fly.model.entity.User;
import com.fly.model.vo.UserVO;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 刷新token
 */

public class RefreshTokenInterceptor implements HandlerInterceptor {

    // private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public RefreshTokenInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1 plus redis 获取token
        String token = request.getHeader("authorization");
        // 2 plus 判断token存在
        if (StrUtil.isBlank(token)) {
            return true;
        }
        // 3 取出userdto信息
        String key = RedisConstants.LOGIN_TOKEN + token;
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        if (map.isEmpty()) {
            return true;
        }
        // 4 map转换为userdto
        UserVO userDTO = BeanUtil.fillBeanWithMap(map, new UserVO(), false);

        // 5 刷新token
        redisTemplate.expire(RedisConstants.LOGIN_TOKEN + token, RedisConstants.LOGIN_USER_TIME, TimeUnit.MINUTES);

        UserHolder.saveUser(userDTO);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}
