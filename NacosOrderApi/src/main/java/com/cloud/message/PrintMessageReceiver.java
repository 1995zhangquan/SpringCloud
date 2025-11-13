package com.cloud.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PrintMessageReceiver {

    @Resource
    private RedisTemplate redisTemplate;

    public void receiveMessage(String channel, String message) {
        log.info("receive channel: {}", channel);
        log.info("receive message: {}", message);
    }
}
