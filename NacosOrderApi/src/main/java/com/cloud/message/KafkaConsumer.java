package com.cloud.message;

import com.cloud.attr.KafkaStatic;
import com.cloud.model.message.MessageModel;
import com.cloud.service.message.MessageTraceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private MessageTraceRecordService messageTraceRecordService;

    @KafkaListener(topics = {KafkaStatic.TOPIC_NORML}, groupId = KafkaStatic.GROUP_NORMAL_CONSUMER)
    public void consumeNormalMessage(ConsumerRecord<String, MessageModel> record, Acknowledgment ack, @Header("kafka_receivedTopic") String topic,
                           @Header("kafka_receivedPartitionId") int partition, @Header("kafka_offset") long offset) {
        MessageModel value = record.value();
        try {
            log.info("KafkaConsumer 监听到消息：{}-{}，message：{}", record.topic(), record.partition(), record.value());

            messageTraceRecordService.consumeSuccess(value.getMessageId(), topic, KafkaStatic.GROUP_NORMAL_CONSUMER, partition, offset);
            ack.acknowledge(); //手动确认消费
            log.info("KafkaConsumer 确认消费成功：主题：{}，分区：{}，偏移量：{}", record.topic(), record.partition(), record.offset());
        } catch (Exception e){
            messageTraceRecordService.consumeFail(value.getMessageId(), topic, e.getMessage());
            log.error("KafkaConsumer 确认消费失败：主题：{}", record.topic());
        }

    }

    //消费事务朱提
    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = {KafkaStatic.TOPIC_TRANSACTIONAL}, groupId = KafkaStatic.GROUP_TRANSACTIONAL_CONSUMER)
    public void consumeTransactionMessage(ConsumerRecord<String, MessageModel> record, Acknowledgment ack, @Header("kafka_receivedTopic") String topic,
                                          @Header("kafka_receivedPartitionId") int partition, @Header("kafka_offset") long offset) {
        log.info("KafkaConsumer 消息：{}-{}，message：{}", record.topic(), record.partition(), record.value());
        MessageModel value = record.value();
        try {
            log.info("KafkaConsumer 监听到消息：{}-{}，message：{}", record.topic(), record.partition(), record.value());

            messageTraceRecordService.consumeSuccess(value.getMessageId(), topic, KafkaStatic.GROUP_NORMAL_CONSUMER, partition, offset);
            ack.acknowledge(); //手动确认消费
            log.info("KafkaConsumer 确认消费成功：主题：{}，分区：{}，偏移量：{}", record.topic(), record.partition(), record.offset());
        } catch (Exception e){
            messageTraceRecordService.consumeFail(value.getMessageId(), topic, e.getMessage());
            log.error("KafkaConsumer 确认消费失败，数据回滚：主题：{}", record.topic());
        }
    }
}
