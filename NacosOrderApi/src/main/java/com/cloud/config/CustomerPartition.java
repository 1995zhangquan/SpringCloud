package com.cloud.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomerPartition implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] bytes1, Cluster cluster) {
        //获取所有主题
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);

        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
