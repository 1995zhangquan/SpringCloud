package com.cloud.message;

import com.cloud.util.StaticUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {StaticUtil.KAFKA_TOPIC_ORDER}/*groupId = "group_order"*/)
    public void getMessage(ConsumerRecord record, Acknowledgment ack,@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<Object> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object object = message.get();
            log.info("KafkaConsumer 收到消息：topic：{}，message：{}", topic, object);
            ack.acknowledge();

        }
    }
}
