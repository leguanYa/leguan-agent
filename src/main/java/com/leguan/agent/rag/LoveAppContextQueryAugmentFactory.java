package com.leguan.agent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 创建上下文查询增强的工厂
 */
public class LoveAppContextQueryAugmentFactory {

    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate promptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回到恋爱相关的问题，别的暂时没办法帮助到你，
                有问题可以联系程序员乐观进行相关处理
                """);

        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false) // 如果设置为true，会继续查询，用的是大模型相关的；如果是false，那么会返回指定的内容
                .emptyContextPromptTemplate(promptTemplate)
                .build();
    }
}
