leguan-agent 是一个用于学习和实践 AI Agent 能力的全栈项目。项目后端基于 Spring Boot 和 Spring AI，集成了 OpenAI 兼容接口、通义千问 DashScope、Ollama、本地/数据库对话记忆、RAG 知识库、向量存储、工具调用和 MCP 客户端等能力。前端基于 Vue 3 + Vite，提供场景化 AI 助手和 AI 超级智能体两个聊天应用入口。

项目主要包含：

- 场景化 AI 助手：支持基于提示词和知识库配置，面向不同业务场景提供问答、建议生成和上下文对话能力。
- AI 超级智能体：基于 ReAct / Tool Calling 思路，能够调用天气、文件操作、网页抓取、资源下载、PDF 生成、终端操作、搜索等工具来完成复杂任务。
- MCP 图片搜索服务：独立的 Spring Boot MCP Server，可通过 Pexels API 搜索网络图片。
- 前后端流式对话：后端通过 SSE 输出流式 AI 回复，前端提供实时聊天体验。

这是一个面向 AI Agent 学习与实验的项目，重点演示 Spring AI 在对话、RAG、工具调用、MCP 和前端交互中的综合应用。