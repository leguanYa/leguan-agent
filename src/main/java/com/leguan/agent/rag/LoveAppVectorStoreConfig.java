package com.leguan.agent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description：
 * @Author：LeGuan
 * @Package：com.leguan.agent.rag
 * @Date: 2026/2/28
 */
@Configuration
public class LoveAppVectorStoreConfig {


    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;


    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel openAiEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(openAiEmbeddingModel).build();
        List<Document> documents = loveAppDocumentLoader.loadMarkDowns();
        try {
            simpleVectorStore.add(documents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleVectorStore;
    }
}
