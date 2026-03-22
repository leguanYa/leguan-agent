package com.leguan.agent.advisor;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SafeLoggerAdvisor  implements MyAdvisor {

    @Override
    public void before(String conversationId,
                       List<Message> messages,
                       Map<String, Object> context) {

        System.out.println("请求消息：" + messages);
    }

    @Override
    public void after(String conversationId,
                      ChatResponse response,
                      Map<String, Object> context) {

        System.out.println("响应：" + response.getResult().getOutput());
    }
}