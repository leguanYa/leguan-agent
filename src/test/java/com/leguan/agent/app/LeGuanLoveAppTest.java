package com.leguan.agent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LeGuanLoveAppTest {


    @Resource
    private LeGuanLoveApp leGuanLoveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员乐观";
        String answer = leGuanLoveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想让另一半（某某）更爱我";
        answer = leGuanLoveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = leGuanLoveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {

        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员乐观,我想让另一半（向日葵）更爱我，我不知道该怎么做,只用回答20个字左右就好";
        LeGuanLoveApp.LoveReport loveReport = leGuanLoveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);

//        String message1 = "我想让另一半更爱我，怎么半";
//        LeGuanLoveApp.LoveReport loveReport1 = leGuanLoveApp.doChatWithReport(message1, chatId);
//        Assertions.assertNotNull(loveReport1);

    }
}