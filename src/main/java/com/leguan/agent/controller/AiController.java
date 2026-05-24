package com.leguan.agent.controller;

import com.leguan.agent.agent.LeguanManus;
import com.leguan.agent.app.LeGuanLoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {


    @Resource
    private LeGuanLoveApp leGuanLoveApp;


    /**
     * 同步调用AI恋爱大师应用
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/love_app/chat/sync")
    public String doChatWithLoveAppSync(String message, String chatId) {
        return leGuanLoveApp.doChat(message, chatId);
    }


    /**
     * SSE 流式调用AI恋爱大师应用
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String chatId) {
        return leGuanLoveApp.doChatByStream(message, chatId);
    }


    /**
     * SSE 流式调用AI恋爱大师应用
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/server_send_event")
    public Flux<ServerSentEvent<String>> doChatWithLoveAppServerSentEvent(String message, String chatId) {
        return leGuanLoveApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }


    /**
     * SSE 流式调用AI恋爱大师应用
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/sse_emitter")
    public SseEmitter doChatWithLoveAppServerSseEmitter(String message, String chatId) {
        // 创建 SSEEmitter 超时时间较长的对象
        SseEmitter sseEmitter = new SseEmitter(180000L);

        // 获取Flux响应式数据流并且直接通过订阅推送给sseEmitter
        leGuanLoveApp.doChatByStream(message, chatId).subscribe(chunk ->{
            try {
                sseEmitter.send(chunk);
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
        }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    @Resource
    private ToolCallback[] toolCallbacks;

    @Resource
    private ChatModel openAiChatModel;

    /**
     * 流式调用manus智能体
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        LeguanManus leguanManus = new LeguanManus(toolCallbacks, openAiChatModel);
        return leguanManus.runStream(message);
    }
}
