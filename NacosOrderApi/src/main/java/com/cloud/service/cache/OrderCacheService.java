package com.cloud.service.cache;

import com.cloud.aop.Cache;
import com.cloud.dao.OrderMapper;
import com.cloud.model.OrderModel;
import com.cloud.util.RedisUtil;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * redis缓存方式：
 * 1.使用自动注解@EnableCaching+@Cacheable实现自动缓存
 * 2.使用redistemplate手动，RedisUtil封装好的工具类
 *
 * key:
 * #root.methodName：获取方法名
 * #id：获取参数列表中的id属性
 * #root.args[0]：获取参数列表中第一个参数
 * #result：返回结果对象
 */


@Slf4j
@Service
public class OrderCacheService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private RedisUtil redisUtil;


    //查询
    //@Cacheable(value = "cacheOrder", key ="#id" )
    @Cache(name = "getKey")
    public OrderModel getOrderById(Long id) {
        redisUtil.sendMessage(RedisUtil.CHANNEL_NAME_ORDER, "土豆土豆，我是地瓜," + id + "已进入缓存中");
        return orderMapper.getOrderById(id);
    }
    //更新
    //@CachePut(value = "cacheOrder", key ="#orderModel.id")
    public OrderModel updateOrder(OrderModel orderModel) {
        if (orderMapper.insert(orderModel) > 0) {
            return orderModel;
        } else {
            return orderModel;
        }
    }

    //删除
    //@CacheEvict(value = "cacheOrder", key ="#id")
    public boolean deleteOrder(Long id) {
        return orderMapper.deleteById(id) > 0;
    }

}
