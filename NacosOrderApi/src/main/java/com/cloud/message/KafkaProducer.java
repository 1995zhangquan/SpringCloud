package com.cloud.message;

import com.cloud.util.RedisUtil;
import com.cloud.util.StaticUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * kafka 生产者
 */
@Slf4j
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String message) {
        log.info("KafkaProducer 需要发送的消息: {}", message);
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(StaticUtil.KAFKA_TOPIC_ORDER, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("KafkaProducer 发送消息失败,: {}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                result.getRecordMetadata().partition();
                result.getRecordMetadata().offset();
                log.info("KafkaProducer 订单消息发送成功：{}", result.toString());
            }
        });

    }
}
