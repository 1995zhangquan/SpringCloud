package com.cloud.service.message;

import com.cloud.attr.CommonStatic;
import com.cloud.dao.MessageTraceRecordDao;
import com.cloud.model.message.MessageModel;
import com.cloud.model.message.MessageTraceRecordModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class MessageTraceRecordService {

    @Autowired
    private MessageTraceRecordDao messageTraceRecordDao;


    public boolean savePreSendRecord(MessageModel messageModel, String topic) {
        MessageTraceRecordModel recordModel = new MessageTraceRecordModel();
        //recordModel.setId();逐渐自增
        recordModel.setMessageId(messageModel.getMessageId());
        recordModel.setTopic(topic);
        recordModel.setSendStatus(CommonStatic.MESSAGE_SEND_PRE);
        recordModel.setCreateTime(new Date());
        return messageTraceRecordDao.save(recordModel);
    }

    public void saveSendSuccess(String messageId, String topic, Integer partition, Long offset) {
        MessageTraceRecordModel recordModel = messageTraceRecordDao.selectByMessageId(messageId);
        if (null != recordModel) {
            recordModel.setTopic(topic);
            recordModel.setPartition(partition);
            recordModel.setOffset(offset);
            recordModel.setSendStatus(CommonStatic.MESSAGE_SEND_SUCCESS);
            recordModel.setSendContent("send:消息发送成功，messageId:::::" + messageId);
            recordModel.setUpdateTime(new Date());
            messageTraceRecordDao.updateById(recordModel);
        } else {
            log.error("保存发送成功消息记录失败，未找到对应的消息记录id:{}",messageId);
        }
    }

    /**
     * partition 和 offset 是由 Kafka broker 在成功接收消息后分配的，如果消息发送失败，就不会有这些信息
     * @param messageId
     * @param topic
     * @param errorMsg
     */
    public void saveSendFail(String messageId, String topic, String errorMsg) {
        MessageTraceRecordModel recordModel = messageTraceRecordDao.selectByMessageId(messageId);
        if (null != recordModel) {
            recordModel.setTopic(topic);
            recordModel.setSendStatus(CommonStatic.MESSAGE_SEND_FAIL);
            recordModel.setSendContent("send:消息发送失败，messageId:::::" + messageId + ",原因：" + errorMsg);
            recordModel.setUpdateTime(new Date());
            messageTraceRecordDao.updateById(recordModel);
        } else {
            log.error("保存发送失败消息记录失败，未找到对应的消息记录id:{}",messageId);
        }
    }

    public void consumeSuccess(String messageId, String consumerTopic, String consumerGroup, int consumerPartition, long consumerOffset) {
        MessageTraceRecordModel recordModel = messageTraceRecordDao.selectByMessageId(messageId);
        if (null != recordModel) {
            recordModel.setConsumeStatus(CommonStatic.MESSAGE_CONSUME_SUCCESS);
            recordModel.setConsumeTime(new Date());
            recordModel.setConsumeContent("consume:消息消费成功，messageId:::::" + messageId);
            recordModel.setConsumerTopic(consumerTopic);
            recordModel.setConsumerGroup(consumerGroup);
            recordModel.setConsumerPartition(consumerPartition);
            recordModel.setConsumerOffset(consumerOffset);
            messageTraceRecordDao.updateById(recordModel);
        } else {
            log.error("消息消费失败，未找到对应的消息记录id:{}",messageId);
        }
    }

    public void consumeFail(String messageId, String topic, String errorMsg) {
        MessageTraceRecordModel recordModel = messageTraceRecordDao.selectByMessageId(messageId);
        if (null != recordModel) {
            recordModel.setConsumeStatus(CommonStatic.MESSAGE_CONSUME_FAIL);
            recordModel.setConsumeTime(new Date());
            recordModel.setConsumerTopic(topic);
            recordModel.setConsumeContent("consume:消息消费失败，messageId:::::" + messageId + ",原因：" + errorMsg);
            messageTraceRecordDao.updateById(recordModel);
        } else {
            log.error("消息消费失败，未找到对应的消息记录id:{}",messageId);
        }
    }

}
