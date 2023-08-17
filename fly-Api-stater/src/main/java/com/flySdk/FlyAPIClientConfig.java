package com.flySdk;

import com.flySdk.client.FlyApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// 读取配置
@ConfigurationProperties( "flyapi.client" )
@Data
@ComponentScan
public class FlyAPIClientConfig {
    private String assessKey;
    private String secretKey;

    @Bean
    public FlyApiClient flyApiClient() {
        return new FlyApiClient(assessKey, secretKey);
    }
}
