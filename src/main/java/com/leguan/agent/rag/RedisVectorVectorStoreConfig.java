package com.leguan.agent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

import java.util.List;

/**
 * @Description：
 * @Author：ZhangHui
 * @Package：com.leguan.agent.rag
 * @Date: 2026/3/19
 */
@Configuration
public class RedisVectorVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled("127.0.0.1", 6379);
    }

    @Bean
    public VectorStore redisVectorVectorStore(JedisPooled jedisPooled, EmbeddingModel openAiEmbeddingModel) {
//        RedisV
        RedisVectorStore redisVectorStore = RedisVectorStore.builder(jedisPooled, openAiEmbeddingModel)
                .indexName("custom-index")                // Optional: defaults to "spring-ai-index"
                .prefix("custom-prefix")                  // Optional: defaults to "embedding:"
                .metadataFields(                         // Optional: define metadata fields for filtering
                        RedisVectorStore.MetadataField.tag("country"),
                        RedisVectorStore.MetadataField.numeric("year"))
                .initializeSchema(true)                   // Optional: defaults to false
                .batchingStrategy(new TokenCountBatchingStrategy()) // Optional: defaults to TokenCountBatchingStrategy
                .build();
        List<Document> documents = loveAppDocumentLoader.loadMarkDowns();
        redisVectorStore.add(documents);
        return redisVectorStore;
    }
}
