package com.cloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderListController {

    @RequestMapping("searchList")
    public String searchList() {
        return "订单列表111";
    }

}
