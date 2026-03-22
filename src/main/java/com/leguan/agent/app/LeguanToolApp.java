package com.leguan.agent.app;

import com.leguan.agent.advisor.MyLoggerAdvisor;
import com.leguan.agent.advisor.SafeLoggerAdvisor;
import com.leguan.agent.repository.MyFileChatMemoryRepository;
import com.leguan.agent.tool.CustomerTools;
import com.leguan.agent.tool.WeatherTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @jakarta.annotation.Resource
    private CustomerTools customerTools;

    public String doChatWithUserTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .tools(customerTools)
                // 使用工具上下文的方式处理一些参数传递，例如用户信息等
                .toolContext(Map.of("userId", 1L))
                .advisors(spec ->
                        spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }


    @jakarta.annotation.Resource
    private ChatModel openAiChatModel;

    // 自定义控制tool
    public String doChatWithUserCustomizationTools(String message, String conversationId) {

        ToolCallingManager toolCallingManager = DefaultToolCallingManager.builder().build();
        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();


        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(ToolCallbacks.from(new WeatherTools(), customerTools))
                .toolContext(Map.of("userId", 1L))
                // 禁止内部工具执行
                .internalToolExecutionEnabled(false)
                .build();
        Prompt prompt = new Prompt(
                List.of(new UserMessage(message)),
                chatOptions);
        chatMemory.add(conversationId, prompt.getInstructions());

        Prompt promptWithMemory = new Prompt(chatMemory.get(conversationId), chatOptions);
        ChatResponse chatResponse = openAiChatModel.call(promptWithMemory);
        chatMemory.add(conversationId, chatResponse.getResult().getOutput());

        while (chatResponse.hasToolCalls()) {
            ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(promptWithMemory,
                    chatResponse);
            chatMemory.add(conversationId, toolExecutionResult.conversationHistory()
                    .get(toolExecutionResult.conversationHistory().size() - 1));
            promptWithMemory = new Prompt(chatMemory.get(conversationId), chatOptions);
            chatResponse = openAiChatModel.call(promptWithMemory);
            chatMemory.add(conversationId, chatResponse.getResult().getOutput());
        }

        UserMessage newUserMessage = new UserMessage("What did I ask you earlier?");
        chatMemory.add(conversationId, newUserMessage);

        ChatResponse newResponse = openAiChatModel.call(new Prompt(chatMemory.get(conversationId)));

        String text = newResponse.getResult().getOutput().getText();
        System.out.println(text);
        return text;

    }



    public String doChatWithUserCustomizationToolAdvisor(String message, String conversationId) {


        ToolCallingManager toolCallingManager = DefaultToolCallingManager.builder().build();
        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

        var advisors = List.of(new SafeLoggerAdvisor());

        Map<String, Object> context = new HashMap<>();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(ToolCallbacks.from(new WeatherTools(), customerTools))
                .toolContext(Map.of("userId", 1L))
                .internalToolExecutionEnabled(false)
                .build();

        // 只加 User
        chatMemory.add(conversationId, new UserMessage(message));

        List<Message> messages = new ArrayList<>(chatMemory.get(conversationId));

        for (SafeLoggerAdvisor advisor : advisors) {
            advisor.before(conversationId, messages, context);
        }


        Prompt prompt = new Prompt(messages, chatOptions);

        ChatResponse chatResponse = openAiChatModel.call(prompt);

        //  after
        for (SafeLoggerAdvisor advisor : advisors) {
            advisor.after(conversationId, chatResponse, context);
        }

        chatMemory.add(conversationId, chatResponse.getResult().getOutput());

        while (chatResponse.hasToolCalls()) {

            ToolExecutionResult result =
                    toolCallingManager.executeToolCalls(prompt, chatResponse);

            Message toolMessage = result.conversationHistory()
                    .get(result.conversationHistory().size() - 1);

            chatMemory.add(conversationId, toolMessage);

            messages = new ArrayList<>(chatMemory.get(conversationId));

            // before
            for (SafeLoggerAdvisor advisor : advisors) {
                advisor.before(conversationId, messages, context);
            }
            prompt = new Prompt(messages, chatOptions);
            chatResponse = openAiChatModel.call(prompt);

            //  after
            for (SafeLoggerAdvisor advisor : advisors) {
                advisor.after(conversationId, chatResponse, context);
            }
            chatMemory.add(conversationId, chatResponse.getResult().getOutput());
        }

        return chatResponse.getResult().getOutput().getText();
    }
}
