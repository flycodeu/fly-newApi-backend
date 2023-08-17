package com.example.flyapigateway;

import com.flyCommon.service.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;


@EnableDubbo
// 禁用数据库
@SpringBootApplication( exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
} )
@Service
public class FlyApiGatewayApplication {

    @DubboReference
    private DemoService demoService;


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FlyApiGatewayApplication.class, args);
        FlyApiGatewayApplication application = context.getBean(FlyApiGatewayApplication.class);
        String result = application.doSayHello("world");
        String result2 = application.doSayHello2("world");
        System.out.println("result: " + result);
        System.out.println("result: " + result2);
    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }

    public String doSayHello2(String name) {
        return demoService.sayHello2(name);
    }

//    @Bean
//    public GlobalFilter customFilter() {
//        return new CustomGlobalFilter();
//    }
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("tobaidu", r -> r.path("/baidu")
//                        .uri("https://www.baidu.com"))
//                .route("toflyicu",r->r.path("/fly")
//                        .uri("http://www.flycode.icu"))
//                .build();
//    }
}
