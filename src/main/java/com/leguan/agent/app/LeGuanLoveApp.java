package com.leguan.agent.app;

import com.leguan.agent.advisor.MyLoggerAdvisor;
import com.leguan.agent.advisor.ReReadingAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class LeGuanLoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    public LeGuanLoveApp(ChatModel openAiChatModel) {
        // 初始化基于内存的对话记忆

        InMemoryChatMemoryRepository inMemoryChatMemoryRepository = new InMemoryChatMemoryRepository();
        ChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(inMemoryChatMemoryRepository)
                .maxMessages(10)
                .build();
        chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(SYSTEM_PROMPT)
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

    record LoveReport(String tittle, List<String> suggestions) {
    }


    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport entity = chatClient
                .prompt()
                .user(message)
                .system(SYSTEM_PROMPT + "每次对话都要生成恋爱结果，标题为{用户名}的恋爱报告，内容建议为字符串列表")
                .advisors(spec ->
                        spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .entity(LoveReport.class);
        log.info("loveReport : {}", entity);
        return entity;
    }
}

