package com.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class NacosController {

    //下面方法可以改进
    @GetMapping("/invoke")
    public String invoke() {
        return "我是nacos-server::::请求时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String requestTimeOut() {
        return "请求超时，请稍后重试";
    }

}
