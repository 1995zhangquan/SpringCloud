package com.cloud.config;

import com.cloud.attr.KafkaStatic;
import com.cloud.model.message.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
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
    private ProducerFactory<String, MessageModel> producerFactory;


    @Bean
    public NewTopic normalTopic() {
        return new NewTopic(KafkaStatic.TOPIC_NORML, 3, (short) 1); // 3个分区，1个副本
    }

    @Bean
    public NewTopic partitionTopic() {
        return new NewTopic(KafkaStatic.TOPIC_PARTITION, 5, (short) 1); // 5个分区，1个副本
    }
    @Bean
    public NewTopic transactionTopic() {
        return new NewTopic(KafkaStatic.TOPIC_TRANSACTIONAL, 3, (short) 1); // 3个分区，1个副本
    }

    @Bean
    public NewTopic deadLetterTopic() {
        return new NewTopic(KafkaStatic.TOPIC_DEAD_LETTER, 1, (short) 1);
    }

    private void handleRetry(ProducerRecord<String, MessageModel> producerRecord, Exception exception) {
        log.info("KafkaProducer 订单消息发送失败，准备重试：producerRecord::::{}:::::{}", producerRecord,exception.getMessage());
        // 方案1: 简单重试几次
        // 方案2: 记录到数据库待重试表
        // 方案3: 发送到重试队列
        //方案4:发送到死信主题
    }


    @Bean
    public KafkaTemplate<String, MessageModel> kafkaTemplate() {
        KafkaTemplate<String, MessageModel> kafkaTemplate = new KafkaTemplate<String, MessageModel>(producerFactory);
        kafkaTemplate.setProducerListener(new ProducerListener<String, MessageModel>() {
            @Override
            public void onSuccess(ProducerRecord<String, MessageModel> producerRecord, RecordMetadata recordMetadata) {
                log.info("KafkaProducer 发送消息成功：{}", recordMetadata);
            }

            @Override
            public void onError(ProducerRecord<String, MessageModel> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                log.info("KafkaProducer 订单消息发送失败：{}", exception.getMessage());
                //重试
                handleRetry(producerRecord, exception);
            }
        });
        return kafkaTemplate;
    }

    /**
     * 配置Kafka事务管理器
     * @return
     */
    @Bean
    public KafkaTransactionManager<String, MessageModel> kafkaTransactionManager() {
        return new KafkaTransactionManager<String, MessageModel>(producerFactory);
    }

    /**
     * 配置事务Kafka模板
     *
     * @return 事务Kafka模板
     */
    @Bean
    public KafkaTemplate<String, MessageModel> transactionalKafkaTemplate() {
        return new KafkaTemplate<String, MessageModel>(producerFactory);
    }
}
