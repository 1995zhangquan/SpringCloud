package com.cloud.message;

import com.cloud.attr.KafkaStatic;
import com.cloud.model.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {KafkaStatic.KAFKA_TOPIC_ORDER}/*groupId = "group_order"*/)
    public void getMessage(ConsumerRecord record, Acknowledgment ack,@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("KafkaConsumer 监听到消息：{}-{}，message：{}", record.topic(), record.partition(), record.value());
        Optional<Object> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object object = message.get();
            log.info("KafkaConsumer 收到消息：topic：{}，message：{}", topic, object);
            ack.acknowledge();// 手动提交offset

        }
    }

    @KafkaListener(topics = {KafkaStatic.KAFKA_TOPIC_ORDER}/*groupId = "group_permit"*/)
    public void batch(List<ConsumerRecord<String, Object>>  records) {
        records.forEach(record -> {
            log.info("KafkaConsumer 监听到消息：{}-{}，message：{}", record.topic(), record.partition(), record.value());
        });
    }

    @Transactional("kafkaTransactionManager")
    @KafkaListener(topics = {KafkaStatic.KAFKA_TOPIC_ORDER}/*groupId = "group_permit"*/)
    public void consumePojo(OrderModel  orderModel) {
        //todosomething
    }
}
