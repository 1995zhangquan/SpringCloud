package com.cloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "nacos-server")
public interface ProviderFeign {

    @GetMapping("/invoke")
    String invoke();

}
