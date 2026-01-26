package com.leguan.agent.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description：
 * @Author：LeGuan
 * @Package：com.leguan.agent.demo.invoke
 * @Date: 2026/1/20
 */
@Component
public class SpingOpenAiAiInvoke implements CommandLineRunner {
    @Resource
    private ChatModel openAiChatModel;
    @Override
    public void run(String... args) throws Exception {
//        AssistantMessage output = openAiChatModel.call(new Prompt("你好，我是乐观，一个Java程序员, 那么你谁"))
//                .getResult()
//                .getOutput();
//        System.out.println("openAi回答 ====" + output.getText());

    }
}
