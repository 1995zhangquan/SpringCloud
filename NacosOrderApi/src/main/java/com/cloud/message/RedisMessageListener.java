package com.cloud.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RedisMessageListener implements MessageListener {

    @Resource
    private RedisTemplate redisTemplate;
    
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(pattern);
        log.info("RedisMessageListener onMessage channel::::" + channel);

        //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        Object object = redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.info("RedisMessageListener onMessage object::::" + object);

    }
}
