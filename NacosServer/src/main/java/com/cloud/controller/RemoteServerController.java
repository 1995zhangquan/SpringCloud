package com.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//配置发布之后，动态刷新配置
@RefreshScope
@RestController
public class RemoteServerController {

    @Value("${user.id:1}")
    private String id;
    @Value("${user.name:defaultUser}")
    private String name;
    @Value("${user.pass:defaultPass}")
    private String pass;

    @RequestMapping("getConfig")
    public String getConfigStr() {
        return "user:" + id + ":" + name + ":" + pass;
    }
}