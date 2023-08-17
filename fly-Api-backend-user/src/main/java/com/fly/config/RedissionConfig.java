package com.fly.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 动态读取yml里面的配置
@ConfigurationProperties( prefix = "spring.redis" )
@Data
public class RedissionConfig {

    private Integer database;
    private String host;
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(database);

        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
