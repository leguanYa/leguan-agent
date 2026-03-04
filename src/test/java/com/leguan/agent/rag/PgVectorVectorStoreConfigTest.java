package com.leguan.agent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class PgVectorVectorStoreConfigTest {

    @Resource
    private VectorStore pgVectorVectorStore;

    @Test
    void test() {
        List<Document> documents = List.of(
                new Document("恋爱中约会的地点，上海推荐:迪士尼，南京东路，菏泽曹县推荐:南湖公园，八里湾!!", Map.of("上海", "迪士尼、南京东路",   "曹县", "南湖公园、八里湾")),
                new Document("山东菏泽曹县牛逼666我的宝贝！"),
                new Document("约会的城市推荐上海、曹县", Map.of("城市", "热门")));
        // Add the documents to PGVector
        pgVectorVectorStore.add(documents);
        List<Document> results = this.pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("恋爱中推荐城市和地点").topK(5).build());
        System.out.println("11111111");
    }

}