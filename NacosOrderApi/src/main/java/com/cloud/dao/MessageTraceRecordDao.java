package com.cloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.model.message.MessageTraceRecordModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTraceRecordDao extends BaseMapper<MessageTraceRecordModel> {

    @Insert("INSERT INTO MESSAGE_TRACE_RECORD(ID,CONTENT,BUSINESS_TYPE,BUSINESS_ID,CREATE_TIME,EXTRA) VALUES(#{id},#{content},#{businessType},#{businessId},#{createTime},#{extra})")
    boolean save(MessageTraceRecordModel messageTraceRecordModel);

    @Select("SELECT * FROM MESSAGE_TRACE_RECORD WHERE MESSAGEID = #{messageId}")
    MessageTraceRecordModel selectByMessageId(String messageId);
}
