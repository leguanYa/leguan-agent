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

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel openAiEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(openAiEmbeddingModel).build();
        List<Document> documents = loveAppDocumentLoader.loadMarkDowns();
        // 自动补充元数据信息
        List<Document> enricherDocuments = myKeywordEnricher.enricherDocuments(documents);
        simpleVectorStore.add(enricherDocuments);
        return simpleVectorStore;
    }
}
