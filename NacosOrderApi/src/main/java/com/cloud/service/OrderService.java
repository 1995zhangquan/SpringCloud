package com.cloud.service;

import com.cloud.dao.OrderMapper;
import com.cloud.model.OrderModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    public List<OrderModel> getList() {
        return orderMapper.getOrderList();
    }

    public OrderModel getOrderById(Long id) {
        return orderMapper.getOrderById(id);
    }

}
