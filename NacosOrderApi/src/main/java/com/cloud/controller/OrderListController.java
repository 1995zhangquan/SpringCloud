package com.cloud.controller;

import com.cloud.service.OrderService;
import com.cloud.service.cache.OrderCacheService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderListController {

    @Resource
    private OrderCacheService orderCacheService;
    @Resource
    private OrderService orderService;

    @RequestMapping("searchList")
    public String searchList() {
        return "订单列表111";
    }

    @RequestMapping("searchOrderId")
    public String searchOrderId() {
        long orderId = 4399L;
        return orderCacheService.getOrderById(orderId).getMemo();
    }
}
