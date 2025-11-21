package com.cloud.model.message;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.aop.ColumnName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("MESSAGE_TRACE_RECORD")
public class MessageTraceRecordModel implements Serializable {

    @ColumnName(value = "id", isPrimaryKey = true)
    private Integer id;
    @ColumnName("MESSAGEID")
    private String messageId;
    @ColumnName("TOPIC")
    private String topic;
    @ColumnName("PARTITION")
    private Integer partition;
    @ColumnName("OFFSET")
    private Long offset;
    @ColumnName("SENDSTATUS")
    private Integer sendStatus; // 0待发送 1发送成功 2发送失败
    @ColumnName("sendContent")
    private String sendContent;
    @ColumnName("CREATETIME")
    private Date createTime;
    @ColumnName("UPDATETIME")
    private Date updateTime;

    //消费
    @ColumnName("CONSUMESTATUS")
    private Integer consumeStatus; // 1消费成功 2消费失败
    @ColumnName("CONSUMECONTENT")
    private String consumeContent;
    @ColumnName("CONSUMETIME")
    private Date consumeTime; //消费时间
    @ColumnName("CONSUMERTOPIC")
    private String consumerTopic;
    @ColumnName("CONSUMERGROUP")
    private String consumerGroup;
    @ColumnName("CONSUMERPARTITION")
    private Integer consumerPartition;
    @ColumnName("CONSUMEROFFSET")
    private Long consumerOffset;

}
