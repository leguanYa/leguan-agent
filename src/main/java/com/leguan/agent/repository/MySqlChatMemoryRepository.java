package com.leguan.agent.repository;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leguan.agent.entity.UserChatMessage;
import com.leguan.agent.mapper.UserChatMessageMapper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class MySqlChatMemoryRepository implements ChatMemoryRepository {

    @Resource
    private UserChatMessageMapper userChatMessageMapper;

    // 写死的用户id
    private static final Long userId = 1L;

    @Override
    public List<String> findConversationIds() {
        QueryWrapper<UserChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<UserChatMessage> userChatMessages = userChatMessageMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(userChatMessages)) {
            return userChatMessages.stream().map(UserChatMessage::getConversationId).toList();
        } else {
            return List.of();
        }

    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        QueryWrapper<UserChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("conversation_id", conversationId);
        queryWrapper.orderByAsc("create_time");
        List<UserChatMessage> userChatMessages = userChatMessageMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(userChatMessages)) {
            return userChatMessages.stream().map(MySqlChatMemoryRepository::toMessage).toList();
        }
        return List.of();
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        if (CollectionUtil.isEmpty(messages)) {
            return;
        }

        // 查询当前已有消息数量
        QueryWrapper<UserChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("conversation_id", conversationId);
        queryWrapper.eq("is_delete", 0);

        Long count = userChatMessageMapper.selectCount(queryWrapper);

        int historySize = count.intValue();
        int incomingSize = messages.size();

        // 如果没有新增，直接返回（防止重复）
        if (incomingSize <= historySize) {
            return;
        }

        // 3只取新增部分（核心）
        List<Message> newMessages = messages.subList(historySize, incomingSize);

        List<UserChatMessage> saveList = new ArrayList<>();
        for (Message message : newMessages) {
            saveList.add(toUserMessage(message, conversationId));
        }

        userChatMessageMapper.insert(saveList);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        QueryWrapper<UserChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("conversation_id", conversationId);
        queryWrapper.eq("is_delete", 0);
        UserChatMessage userChatMessage = new UserChatMessage();
        userChatMessage.setIsDelete(1);
        userChatMessageMapper.update(userChatMessage, queryWrapper);
    }


    public static Message toMessage(UserChatMessage chatMessage) {
        MessageType messageType = chatMessage.getMessageType();
        String text = chatMessage.getText();
        Map<String, Object> metadata = chatMessage.getMetadata();
        return switch (messageType) {
            case USER -> new UserMessage(text);
            case ASSISTANT -> {
                AssistantMessage.Builder content = AssistantMessage.builder().content(text);
                if (null != metadata && !metadata.isEmpty()) {
                    content.properties(metadata);
                }
                yield content.build();
            }

            case SYSTEM -> new SystemMessage(text);
            case TOOL -> {
                ToolResponseMessage.Builder responses = ToolResponseMessage.builder().responses(List.of());
                if (null != metadata && !metadata.isEmpty()) {
                    responses.metadata(metadata);
                }
                yield responses.build();
            }
        };
    }

    public static UserChatMessage toUserMessage(Message message, String conversationId) {
        UserChatMessage userChatMessage = new UserChatMessage();
        userChatMessage.setMessageType(message.getMessageType());
        userChatMessage.setText(message.getText());
        userChatMessage.setMetadata(message.getMetadata());
        userChatMessage.setCreateTime(new Date());
        userChatMessage.setUpdateTime(new Date());
        userChatMessage.setUserId(userId);
        userChatMessage.setConversationId(conversationId);
        userChatMessage.setIsDelete(0);
        return userChatMessage;
    }
}
