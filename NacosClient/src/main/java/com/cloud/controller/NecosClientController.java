package com.cloud.controller;

import com.cloud.feign.ProviderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class NecosClientController {

    @Autowired
    private ProviderFeign providerFeign;

    @GetMapping("testClient")
    public String testClient() {
        return providerFeign.invoke();
    }
}
