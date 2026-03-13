package com.leguan.agent.rag;

import com.alibaba.cloud.ai.document.TextDocumentParser;
import com.alibaba.cloud.ai.reader.github.GitHubDocumentReader;
import com.alibaba.cloud.ai.reader.github.GitHubResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description：应用文档加载
 * @Author：LeGuan
 * @Package：com.leguan.agent.rag
 * @Date: 2026/2/28
 */
@Component
@Slf4j
public class LoveAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    // 加载多个Markdown文件
    public List<Document> loadMarkDowns() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:/document/*.md");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                // 提取文档倒数第3和第二个字作为源信息
                String status = filename.substring(filename.length() - 6, filename.length() - 4);
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", filename)
                        .withAdditionalMetadata("status", status)
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("Markdown 文件加载失败", e);
        }
        return allDocuments;
    }

    @Value("${leguan.github.token}")
    private String githubToken;

    // 读取GitHub文档
    public List<Document> loadGithubDoc() {

        GitHubResource resource = GitHubResource.builder()
                .owner("alibaba") //作者
                .repo("spring-ai-alibaba") //仓库名称
                .branch("main") // 分支
                .path("README.md")
                .gitHubToken(githubToken)
                .build();
        TextDocumentParser textDocumentParser = new TextDocumentParser();
        GitHubDocumentReader reader = new GitHubDocumentReader(resource, textDocumentParser);
        List<Document> documents = reader.get();
        return documents;
    }
}
