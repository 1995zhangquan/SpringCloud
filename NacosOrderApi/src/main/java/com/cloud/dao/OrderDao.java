package com.cloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.model.OrderModel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderDao extends BaseMapper<OrderModel> {


    @Select("SELECT * FROM ORDER_MODEL")
    public List<OrderModel> getOrderList();

    @Select("SELECT * FROM ORDER_MODEL WHERE ID = #{id}")
    public OrderModel getOrderById(Long id);
}
