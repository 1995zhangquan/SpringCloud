package com.cloud.main;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

//开启hystrix支持
@EnableHystrix
//实现服务注册
@EnableDiscoveryClient
//@EnableCaching //缓存
@RefreshScope
@MapperScan("com.cloud.dao")
@SpringBootApplication(scanBasePackages = {"com.cloud"})
public class NacosOrderApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosOrderApiApplication.class, args);
    }

}
