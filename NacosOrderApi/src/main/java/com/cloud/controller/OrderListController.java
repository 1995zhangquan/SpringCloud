package com.cloud.controller;

import com.cloud.attr.RedisStatic;
import com.cloud.service.OrderService;
import com.cloud.service.cache.OrderCacheService;
import com.cloud.util.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderListController {

    @Resource
    private OrderCacheService orderCacheService;
    @Resource
    private OrderService orderService;
    @Resource
    private RedisUtil redisUtil;

    @RequestMapping("searchList")
    public String searchList() {
        return "订单列表111";
    }

    @RequestMapping("searchOrderId")
    public String searchOrderId() {
        long orderId = 4399L;
        return orderCacheService.getOrderById(orderId).getMemo();
    }

    @RequestMapping("getMessage")
    public String getMessage() {
        redisUtil.sendMessage(RedisStatic.CHANNEL_NAME_ORDER, "我主动发送消息");
        return "消息已发送";
    }
}
