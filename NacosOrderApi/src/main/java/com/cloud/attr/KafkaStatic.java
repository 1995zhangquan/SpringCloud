package com.cloud.attr;

/**
 * kafka静态类
 */

public class KafkaStatic {

    // kafka主题
    public static final String TOPIC_NORML = "normal_topic"; //普通消息主题
    public static final String TOPIC_PARTITION = "partition_topic"; //分区消息主题
    public static final String TOPIC_TRANSACTIONAL = "transactional_topic"; //事务消息主题
    public static final String TOPIC_DEAD_LETTER = "dead_letter_topic"; //死信主题

    // kafka消费者组
    public static final String GROUP_NORMAL_CONSUMER = "normal_consumer_group"; //普通消费者组
    public static final String GROUP_PARTITION_CONSUMER = "partition_consumer_group"; //分区消费者组
    public static final String GROUP_TRANSACTIONAL_CONSUMER = "transactional_consumer_group"; //事务消费者组
    public static final String GROUP_DEAD_LETTER_CONSUMER = "dead_letter_consumer_group"; //死信消费者组

    /**
     * 事务ID前缀
     */
    public static final String TRANSACTION_ID_PREFIX = "tx-";

}
