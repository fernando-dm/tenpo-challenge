package com.tenpo.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(
        scanBasePackages = {
                "com.tenpo.apirest",
        }
)
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.tenpo.clients"
)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApiRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiRestApplication.class, args);
    }
}
