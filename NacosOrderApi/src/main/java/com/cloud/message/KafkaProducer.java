package com.cloud.message;

import com.cloud.attr.KafkaStatic;
import com.cloud.model.message.MessageModel;
import com.cloud.service.message.MessageTraceRecordService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * kafka 生产者
 */
@Slf4j
@Component
public class KafkaProducer {

    @Autowired
    private MessageTraceRecordService messageTraceRecordService;
    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate<String, MessageModel> kafkaTemplate;
    @Autowired
    @Qualifier("transactionalKafkaTemplate")
    private KafkaTemplate<String, MessageModel> transactionKafkaTemplate;



    public void sendMessage(String topic, MessageModel messageModel, String message) {
        log.info("sendMessage:::::::::message:{}", message);
       // StringUtils.hasText(topic, "主题名称不能为空");
        Objects.requireNonNull(messageModel, "消息实体不能为空");
        log.info("MessageModel 需要发送的实体: {}", messageModel);

        if (StringUtils.isBlank(messageModel.getMessageId())) {
            messageModel.setMessageId(UUID.randomUUID().toString());
        }
        if (null == messageModel.getCreateTime()) {
            messageModel.setCreateTime(new Date());
        }

        //记录消息发送的轨迹
        messageTraceRecordService.savePreSendRecord(messageModel, topic);

        //发送消息
        ListenableFuture<SendResult<String, MessageModel>> future = kafkaTemplate.send(topic, messageModel);
        //可以传递对象
        //kafkaTemplate.send(StaticUtil.KAFKA_TOPIC_ORDER, new OrderModel(xxxx));
        //future.addCallback(new SuccessCallback<SendResult<String, Object>>(){
        future.addCallback(new ListenableFutureCallback<SendResult<String, MessageModel>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("KafkaProducer 发送消息失败，异常原因: {}", ex.getMessage());
                messageTraceRecordService.saveSendFail(messageModel.getMessageId(), topic, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, MessageModel> result) {
                // 消息发送到的topic
                String successBackTopic = result.getRecordMetadata().topic();
                log.info("topic::::{}", topic);
                log.info("成功回调的successBackTopic::::{}", successBackTopic);

                //是否和当前主题topic一致
                // 消息发送到的分区
                int partition = result.getRecordMetadata().partition();
                // 消息在分区内的offset
                long offset = result.getRecordMetadata().offset();
                log.info("kafka消息发送成功，主题：{}，消息id{}，分区：{}，偏移量：{}", topic, messageModel.getMessageId(), partition, offset);

                messageTraceRecordService.saveSendSuccess(messageModel.getMessageId(), successBackTopic, partition, offset);
            }
        });

    }

    //事务发送消息
    @Transactional(rollbackFor = ExecutionException.class)
    public void sendMessageTransaction(MessageModel messageModel, String message) {
        Objects.requireNonNull(messageModel, "消息实体不能为空");
        log.info("MessageModel 需要发送的实体: {}", messageModel);

        if (StringUtils.isBlank(messageModel.getMessageId())) {
            messageModel.setMessageId(UUID.randomUUID().toString());
        }
        if (null == messageModel.getCreateTime()) {
            messageModel.setCreateTime(new Date());
        }
        String transactionalTopic = KafkaStatic.TOPIC_TRANSACTIONAL;
        //记录消息发送的轨迹
        messageTraceRecordService.savePreSendRecord(messageModel, transactionalTopic);

        transactionKafkaTemplate.executeInTransaction(kafkaOperations -> {
            SendResult<String, MessageModel> sendResult = null;
            try {
                sendResult = kafkaOperations.send(transactionalTopic, messageModel.getMessageId(), messageModel).get();
                log.info("发送结果：{}", sendResult);
                log.info("kafka事务消息发送：主题：{}，消息id：{}，分区：{}，偏移量：{}", transactionalTopic, messageModel.getMessageId(),
                        sendResult.getRecordMetadata().partition(), sendResult.getRecordMetadata().offset());
                messageTraceRecordService.saveSendSuccess(messageModel.getMessageId(), transactionalTopic, sendResult.getRecordMetadata().partition(), sendResult.getRecordMetadata().offset());
            } catch (InterruptedException e) {
                log.error("KafkaProducer 创建发送失败，异常原因: {}", e.getMessage());
                messageTraceRecordService.saveSendFail(messageModel.getMessageId(), transactionalTopic, e.getMessage());
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                log.error("KafkaProducer 创建发送失败，异常原因: {}", e.getMessage());
                messageTraceRecordService.saveSendFail(messageModel.getMessageId(), transactionalTopic, e.getMessage());
                throw new RuntimeException(e);
            }
            return sendResult;
        });

    }

    public MessageModel createMessageModel(String conetent, String businessType, String businessId, String extra) {
        MessageModel messageModel = new MessageModel();
        messageModel.setMessageId(UUID.randomUUID().toString());
        messageModel.setContent(conetent);
        messageModel.setBusinessType(businessType);
        messageModel.setBusinessId(businessId);
        messageModel.setExtra(extra);
        messageModel.setCreateTime(new Date());
        return messageModel;
    }
}
