package com.leguan.agent.app;

import com.leguan.agent.advisor.MyLoggerAdvisor;
import com.leguan.agent.advisor.ReReadingAdvisor;
import com.leguan.agent.repository.MyFileChatMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;


@Component
@Slf4j
public class LeGuanLoveApp {

    private final ChatClient chatClient;

    // 系统提示词
    private final PromptTemplate systemPrompt;

    // 用户提示词
    private final PromptTemplate userPrompt;



    public LeGuanLoveApp(ChatModel openAiChatModel,
                         @Value("classpath:/prompts/love-system.st")Resource systemPromptResource,
                         @Value("classpath:/prompts/love-user.st")Resource userPromptResource)
                        throws IOException {
        this.systemPrompt = new PromptTemplate(systemPromptResource.getContentAsString(StandardCharsets.UTF_8));
        this.userPrompt = new PromptTemplate(userPromptResource.getContentAsString(StandardCharsets.UTF_8));
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        MyFileChatMemoryRepository  chatMemoryRepository = new MyFileChatMemoryRepository(fileDir);
        // 初始化基于内存的对话记忆
//        InMemoryChatMemoryRepository chatMemoryRepository = new InMemoryChatMemoryRepository();
        ChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();
        chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(systemPrompt.render())
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build(),
                        // 自定义拦截器
                        new MyLoggerAdvisor()
                        // 自定义Re2拦截器
//                        new ReReadingAdvisor()
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
        Integer totalTokens = response.getMetadata().getUsage().getTotalTokens();
        String model = response.getMetadata().getModel();
        log.info("模型: {}，消耗token:{}", model, totalTokens);
        log.info("content: {}", content);
        return content;
    }


    record LoveReport(String tittle, List<String> suggestions, List<String> locations) {
    }


    public LoveReport doChatWithReport(String message, String chatId) {

        HashMap<String, Object> userPromptMap = new HashMap<>();
        userPromptMap.put("count", 5);
        String render = userPrompt.render(userPromptMap);
        LoveReport entity = chatClient
                .prompt()
                .user(message + "\n" + render)
                .system(systemPrompt.render() + "每次对话都要生成恋爱结果，标题为{用户名}的恋爱报告，内容建议为字符串列表")
                .advisors(spec ->
                        spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .entity(LoveReport.class);
        log.info("loveReport : {}", entity);
        return entity;
    }
}

