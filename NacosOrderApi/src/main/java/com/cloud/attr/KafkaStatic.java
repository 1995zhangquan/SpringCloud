package com.cloud.attr;

/**
 * kafka静态类
 */

public class KafkaStatic {

    //kafka主题
    public static final String KAFKA_TOPIC_ORDER = "TOPIC_ORDER";

    /**
     * 普通消息主题
     */
    public static final String NORMAL_TOPIC = "normal_topic";

    /**
     * 分区消息主题
     */
    public static final String PARTITION_TOPIC = "partition_topic";

    /**
     * 事务消息主题
     */
    public static final String TRANSACTIONAL_TOPIC = "transactional_topic";

    /**
     * 死信主题
     */
    public static final String DEAD_LETTER_TOPIC = "dead_letter_topic";

    /**
     * 普通消费者组
     */
    public static final String NORMAL_CONSUMER_GROUP = "normal_consumer_group";

    /**
     * 分区消费者组
     */
    public static final String PARTITION_CONSUMER_GROUP = "partition_consumer_group";

    /**
     * 事务消费者组
     */
    public static final String TRANSACTIONAL_CONSUMER_GROUP = "transactional_consumer_group";

    /**
     * 死信消费者组
     */
    public static final String DEAD_LETTER_CONSUMER_GROUP = "dead_letter_consumer_group";

    /**
     * 事务ID前缀
     */
    public static final String TRANSACTION_ID_PREFIX = "tx-";

}
