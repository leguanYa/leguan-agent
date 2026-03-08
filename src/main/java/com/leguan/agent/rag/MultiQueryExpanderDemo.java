package com.leguan.agent.rag;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 查询扩展器
 */
@Component
public class MultiQueryExpanderDemo {


    private final ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDemo(ChatModel openAiChatModel) {
        chatClientBuilder = ChatClient.builder(openAiChatModel);
    }

    public List<Query> expand(String message) {
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        return queryExpander.expand(new Query(message));
    }
}
