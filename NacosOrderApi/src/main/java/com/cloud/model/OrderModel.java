package com.cloud.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Data
@TableName("ORDER_MODEL")
public class OrderModel implements Serializable {

    @TableId
    private Long id;
    @TableField("USER_ID")
    private Integer orderType;
    @TableField("USER_ID")
    private Integer state;
    @TableField("MEMO")
    private String memo;
    @TableField("USER_ID")
    private Long createBy;
    @TableField("USER_ID")
    private Date createTime;
}
