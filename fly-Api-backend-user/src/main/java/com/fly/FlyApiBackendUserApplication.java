package com.fly;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.fly.mapper")
@EnableDubbo
@EnableScheduling
public class FlyApiBackendUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyApiBackendUserApplication.class, args);
    }

}
