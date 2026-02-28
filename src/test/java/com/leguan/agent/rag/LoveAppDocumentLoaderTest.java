package com.leguan.agent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description：
 * @Author：ZhangHui
 * @Package：com.leguan.agent.rag
 * @Date: 2026/2/28
 */
@SpringBootTest
class LoveAppDocumentLoaderTest {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Test
    void loadMarkDowns() {
        List<Document> documents = loveAppDocumentLoader.loadMarkDowns();
        System.out.println("-------");
    }
}