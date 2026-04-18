package com.leguan.agent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class LeguanToolAppTest {

    @Resource
    private LeguanToolApp leguanToolApp;

    @Test
    void doChatWithTools() {
        String chatId = UUID.randomUUID().toString();
        String message = "上海今天的天气怎么样";

        String s = leguanToolApp.doChatWithTools(message, chatId);
        System.out.println("回答如下");
        System.out.println(s);
    }

    @Test
    void doChatWithToolsTwo() {
        // 测试联网搜索问题的答案
        testMessage("周末想去上海约会，推荐几个适合情侣的小众打卡地？");

        // 测试网页抓取：恋爱案例分析
        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");

        // 测试资源下载：图片下载
//        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");

        // 测试终端操作：执行代码
        testMessage("我的系统是windows 请执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的恋爱档案为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = leguanToolApp.doChatWithToolsTwo(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithUserTools() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我查询用户信息";
        String answer = leguanToolApp.doChatWithUserTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithUserCustomizationTools() {
        String chatId = UUID.randomUUID().toString();
        String message = "上海今天的天气怎么样，获取完天气后在帮我查询用户信息";
        String answer = leguanToolApp.doChatWithUserCustomizationTools(message, chatId);
        Assertions.assertNotNull(answer);
    }



    @Test
    void doChatWithUserCustomizationToolsss() {
        String chatId = UUID.randomUUID().toString();
        String message = "上海今天的天气怎么样，获取完天气后在帮我查询用户信息";
        String answer = leguanToolApp.doChatWithUserCustomizationToolAdvisor(message, chatId);
        log.info("回答如下:{}", answer);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMCP() {
        String chatId = UUID.randomUUID().toString();
        String message = "我现在在上海浦东新区，推荐几个美食地点";
        String s = leguanToolApp.doChatWithMCP(message, chatId);
        System.out.println("回答如下");
        System.out.println(s);
    }
}