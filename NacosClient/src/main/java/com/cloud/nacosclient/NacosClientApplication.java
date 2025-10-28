package com.cloud.nacosclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//开启服务注册
@EnableDiscoveryClient
//使用feign客户端
@EnableFeignClients(basePackages = {"com.cloud.feign"})
@SpringBootApplication(scanBasePackages = {"com.cloud"})
public class NacosClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosClientApplication.class, args);
    }

}
