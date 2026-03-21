package com.leguan.agent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LeGuanMysqlAppTest {

    @Resource
    private LeGuanMysqlApp leGuanMysqlApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员乐观";
        String answer = leGuanMysqlApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想让另一半（某某）更爱我";
        answer = leGuanMysqlApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = leGuanMysqlApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }
}