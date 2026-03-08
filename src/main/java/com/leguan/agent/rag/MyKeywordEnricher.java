package com.leguan.agent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基于Ai的文档元信息增强器（为文档补充元信息）
 */
@Component
public class MyKeywordEnricher {

    @Resource
    private ChatModel openAiChatModel;

    // 关键元信息数量
    private static final Integer MAX_KEYWORDS = 6;

    List<Document> enricherDocuments(List<Document> documents) {
        PromptTemplate promptTemplate = new PromptTemplate(String.format("{context_str}. 请为本文档提供%S个独特关键词。格式为逗号分离。关键词:", MAX_KEYWORDS));
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(this.openAiChatModel, promptTemplate);
        return keywordMetadataEnricher.apply(documents);
    }
}
