package com.cloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class NacosController {

    //下面方法可以改进
    @GetMapping("/invoke")
    @HystrixCommand(fallbackMethod = "requestTimeOut", commandProperties = {
            ////设置服务调用超时10秒时触发服务降级
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String invoke() {
        return "我是nacos-server::::请求时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String requestTimeOut() {
        return "请求超时，请稍后重试";
    }


}
