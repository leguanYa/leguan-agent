package com.leguan.agent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.springframework.ai.chat.messages.MessageType;

import java.util.Date;
import java.util.Map;

@Data
@TableName("user_chat_message")
public class UserChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户id
    @TableField(value = "user_id")
    private Long userId;

    // 用户会话id
    @TableField(value = "conversation_id")
    private String conversationId;

    // 消息类型
    @TableField(value = "message_type")
    private MessageType messageType;

    // 内容
    @TableField(value = "text")
    private String text;

    // 元信息
    @TableField(value = "metadata", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> metadata;

    // 创建时间
    @TableField(value = "create_time")
    private Date createTime;

    // 更新时间
    @TableField(value = "update_time")
    private Date updateTime;

    // 逻辑删除:0=未删,1=删除
    @TableField(value = "is_delete")
    private Integer isDelete;
}
