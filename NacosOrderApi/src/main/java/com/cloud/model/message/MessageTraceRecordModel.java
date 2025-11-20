package com.cloud.model.message;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("MESSAGE_TRACE_RECORD")
public class MessageTraceRecordModel implements Serializable {

    @TableId
    private Integer id;
    @TableField("MESSAGEID")
    private String messageId;
    @TableField("TOPIC")
    private String topic;
    @TableField("PARTITION")
    private Integer partition;
    @TableField("OFFSET")
    private Long offset;
    @TableField("SENDSTATUS")
    private Integer sendStatus; // 0待发送 1发送成功 2发送失败
    @TableField("CONTENT")
    private String content;
    @TableField("CREATETIME")
    private Date createTime;
    @TableField("UPDATETIME")
    private Date updateTime;
}
