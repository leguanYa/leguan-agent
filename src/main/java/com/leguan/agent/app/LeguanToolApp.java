package com.leguan.agent.app;

import com.leguan.agent.advisor.MyLoggerAdvisor;
import com.leguan.agent.repository.MyFileChatMemoryRepository;
import com.leguan.agent.tool.WeatherTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class LeguanToolApp {

    private final ChatClient chatClient;



    // 系统提示词
    private final PromptTemplate systemPrompt;

    // 用户提示词
    private final PromptTemplate userPrompt;



    public LeguanToolApp(ChatModel openAiChatModel,
                         @Value("classpath:/prompts/love-system.st") Resource systemPromptResource,
                         @Value("classpath:/prompts/love-user.st")Resource userPromptResource)
            throws IOException {
        this.systemPrompt = new PromptTemplate(systemPromptResource.getContentAsString(StandardCharsets.UTF_8));
        this.userPrompt = new PromptTemplate(userPromptResource.getContentAsString(StandardCharsets.UTF_8));
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        MyFileChatMemoryRepository chatMemoryRepository = new MyFileChatMemoryRepository(fileDir);
        ChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();
        chatClient = ChatClient.builder(openAiChatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build(),
                        // 自定义拦截器
                        new MyLoggerAdvisor()
                )
                .build();
    }



    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .tools(new WeatherTools())
                .advisors(spec ->
                        spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        Integer totalTokens = response.getMetadata().getUsage().getTotalTokens();
        String model = response.getMetadata().getModel();
        return content;
    }


    @jakarta.annotation.Resource
    private ToolCallback[] allTools;

    public String doChatWithToolsTwo(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .toolCallbacks(allTools)
                .advisors(spec ->
                        spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}
