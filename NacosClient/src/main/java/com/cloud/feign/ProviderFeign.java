package com.cloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "nacos-server") //name是必需的，对应Eureka中的服务名
public interface ProviderFeign {

    @GetMapping("/invoke")
    String invoke();

    @GetMapping("/organization/{organizationId}")
    List findByOrganization(@PathVariable("organizationId") Long organizationId);
}
