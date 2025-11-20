package com.cloud.model.message;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class MessageModel implements Serializable {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务ID，用于分区策略
     */
    private String businessId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 扩展字段，用于存储额外信息
     */
    private String extra;
}
