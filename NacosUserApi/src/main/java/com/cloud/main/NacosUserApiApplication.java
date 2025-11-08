package com.cloud.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

//开启hystrix支持
@EnableHystrix
//实现服务注册
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.cloud"})
public class NacosUserApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosUserApiApplication.class, args);
    }

}
