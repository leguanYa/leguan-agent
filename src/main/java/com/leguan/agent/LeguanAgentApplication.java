package com.leguan.agent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.ai.vectorstore.redis.autoconfigure.RedisVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {PgVectorStoreAutoConfiguration.class, RedisVectorStoreAutoConfiguration.class})
@MapperScan("com.leguan.agent.mapper")
public class LeguanAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeguanAgentApplication.class, args);
    }

}
