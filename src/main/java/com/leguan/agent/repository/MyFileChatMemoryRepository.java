package com.leguan.agent.repository;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于文件的 ChatMemoryRepository 实现
 */
public class MyFileChatMemoryRepository implements ChatMemoryRepository {

    private final String BASE_DIR;
    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public MyFileChatMemoryRepository(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    /**
     * 返回所有会话 ID
     */
    @Override
    public List<String> findConversationIds() {
        File baseDir = new File(BASE_DIR);
        File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".kryo"));

        if (files == null) {
            return List.of();
        }

        List<String> ids = new ArrayList<>();
        for (File file : files) {
            String name = file.getName();
            ids.add(name.substring(0, name.length() - ".kryo".length()));
        }
        return ids;
    }

    /**
     * 查询某个会话的全部历史
     */
    @Override
    public List<Message> findByConversationId(String conversationId) {
        return getOrCreateConversation(conversationId);
    }

    /**
     * 保存消息（⚠️ 注意：是「追加语义」）
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }

        List<Message> history = getOrCreateConversation(conversationId);

        // 关键：只追加“尾部新增”
        int historySize = history.size();
        int incomingSize = messages.size();

        if (incomingSize <= historySize) {
            // before / after 重复触发的保护
            return;
        }

        List<Message> toAppend = messages.subList(historySize, incomingSize);
        history.addAll(toAppend);

        saveConversation(conversationId, history);
    }


    /**
     * 删除会话
     */
    @Override
    public synchronized void deleteByConversationId(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    // ================== internal ==================

    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Input input = new Input(new FileInputStream(file))) {
            return kryo.readObject(input, ArrayList.class);
        } catch (Exception e) {
            // 文件损坏兜底
            return new ArrayList<>();
        }
    }

    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save conversation: " + conversationId, e);
        }
    }

    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}
