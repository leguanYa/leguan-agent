package com.leguan.agent.rag;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 * 显示声明基于pgVectorVectorStore
 * 因为生成的bean在PgVectorStoreAutoConfiguration加载的时候会embeddingModel不知道加载谁，所以需要在启动类中排除
 */
@Configuration
public class PgVectorVectorStoreConfig {


    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;


    @Bean
    public VectorStore pgVectorVectorStore(DataSource dataSource, EmbeddingModel openAiEmbeddingModel) {

        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource pg = ds.getDataSource("postgres");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(pg);
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, openAiEmbeddingModel)
                .dimensions(1536)                    // 不要盲目设置
                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                     // Optional: defaults to HNSW
                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
                .build();
        // 加载数据，这里是为了测试，不要每次都加载
        List<Document> documents = loveAppDocumentLoader.loadMarkDowns();
        vectorStore.add(documents);
        return vectorStore;
    }
}
