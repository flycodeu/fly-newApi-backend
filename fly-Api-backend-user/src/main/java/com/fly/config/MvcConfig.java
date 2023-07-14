package com.fly.config;

import com.fly.utils.LoginInterceptor;
import com.fly.utils.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录，刷新
        registry.addInterceptor(new LoginInterceptor())
                // 这里面写的是后端接口，不需要在前面加入/api
                .excludePathPatterns(
//                        "/user/account/login",
//                        "/user/register",
//                        "/user/phone/login",
//                        "/user/code",
//                        "/user/logout"
                        "/**"
                        )
                .order(1)
        ;

        registry.addInterceptor(new RefreshTokenInterceptor(redisTemplate)).addPathPatterns("/**").order(0);
    }
}
