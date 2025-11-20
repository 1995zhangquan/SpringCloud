package com.cloud.service;

import com.cloud.dao.OrderDao;
import com.cloud.model.OrderModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService {

    @Resource
    private OrderDao orderDao;

    public List<OrderModel> getList() {
        return orderDao.getOrderList();
    }

    public OrderModel getOrderById(Long id) {
        return orderDao.getOrderById(id);
    }

}
