package com.cloud.config;

import com.cloud.model.message.MessageModel;
import com.cloud.model.message.MessageTraceRecordModel;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class CustomerPartition implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //获取所有主题
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitons = partitions.size();
        if (!(value instanceof MessageModel)) {
            throw new InvalidRecordException("消息必须是MessageModel类型");
        }
        MessageModel messageModel = (MessageModel) value;
        String businessId = messageModel.getBusinessId();
        if (StringUtils.isEmpty(businessId)) {
            if (null == keyBytes) {
                //当消息的businessId为空且没有指定key时，使用Murmur2哈希算法对消息内容进行哈希计算
                // ，然后取正数并对分区数量取模，以此来决定消息应该被分配到哪个分区。
                // 这样可以实现基于消息内容的负载均衡分布。
                return Utils.toPositive(Utils.murmur2(valueBytes)) % numPartitons;
            } else {
                return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitons;
            }
        }
        // 根据业务ID计算分区，确保相同业务ID的消息在同一分区
        return Math.abs(businessId.hashCode()) % numPartitons;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
