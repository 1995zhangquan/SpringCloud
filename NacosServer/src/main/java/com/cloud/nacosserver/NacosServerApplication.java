package com.cloud.nacosserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Nacos Server Application
 */
//实现服务注册
@EnableDiscoveryClient
//@SpringBootApplication(scanBasePackages = {"com.cloud"},exclude = SecurityAutoConfiguration.class)
@SpringBootApplication(scanBasePackages = {"com.cloud"})
public class NacosServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosServerApplication.class, args);
    }

}
