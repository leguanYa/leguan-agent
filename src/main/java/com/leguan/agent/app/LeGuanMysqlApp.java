package com.leguan.agent.app;

import com.leguan.agent.advisor.MyLoggerAdvisor;
import com.leguan.agent.repository.MyFileChatMemoryRepository;
import com.leguan.agent.repository.MySqlChatMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
@Slf4j
public class LeGuanMysqlApp {

    private final ChatClient chatClient;

    // 系统提示词
    private final PromptTemplate systemPrompt;

    // 用户提示词
    private final PromptTemplate userPrompt;

    public LeGuanMysqlApp(ChatModel openAiChatModel,
                          @Value("classpath:/prompts/love-system.st") Resource systemPromptResource,
                          @Value("classpath:/prompts/love-user.st")Resource userPromptResource,
                          MySqlChatMemoryRepository mySqlChatMemoryRepository)
            throws IOException {
        this.systemPrompt = new PromptTemplate(systemPromptResource.getContentAsString(StandardCharsets.UTF_8));
        this.userPrompt = new PromptTemplate(userPromptResource.getContentAsString(StandardCharsets.UTF_8));
        ChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(mySqlChatMemoryRepository)
                .maxMessages(10)
                .build();
        chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(systemPrompt.render())
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build(),
                        // 自定义拦截器
                        new MyLoggerAdvisor()
                )
                .build();
    }



    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec ->
                        spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        return content;
    }
}
