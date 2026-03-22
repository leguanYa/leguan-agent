package com.leguan.agent.advisor;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;
import java.util.Map;

public interface MyAdvisor {

    void before(String conversationId,
                List<Message> messages,
                Map<String, Object> context);

    void after(String conversationId,
               ChatResponse response,
               Map<String, Object> context);
}