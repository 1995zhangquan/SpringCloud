package com.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.transaction.KafkaTransactionManager;

/**
 * 监听器
 *  监听生产者消息是否发送成功，如果失败，则进行重试或者记录到数据库重试
 */
@Slf4j
@Configuration
public class KafkaConfig {

    @Autowired
    private ProducerFactory producerFactory;

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<String, Object>(producerFactory);
        kafkaTemplate.setProducerListener(new ProducerListener<String, Object>() {
            @Override
            public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
                log.info("KafkaProducer 发送消息成功：{}", recordMetadata);
            }

            @Override
            public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                log.info("KafkaProducer 订单消息发送失败：{}", exception.getMessage());
                //重试
                handleRetry(producerRecord, exception);
            }
        });
        return kafkaTemplate;
    }

    private void handleRetry(ProducerRecord<String, Object> producerRecord, Exception exception) {
        // 方案1: 简单重试几次
        // 方案2: 记录到数据库待重试表
        // 方案3: 发送到重试队列
    }

    @Bean
    public KafkaTransactionManager<String, Object> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory);
    }

}
