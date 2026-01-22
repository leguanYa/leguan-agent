package com.leguan.agent.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class HttpAiInvoke {
    public static void main(String[] args) {
        String apiKey = TestApiKey.TEST_API_KEY; // 替换为实际的 API Key
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 构建请求体 JSON
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", "qwen-plus");

        JSONObject input = new JSONObject();
        JSONObject systemMessage = new JSONObject();
        systemMessage.set("role", "system");
        systemMessage.set("content", "You are a helpful assistant.");

        JSONObject userMessage = new JSONObject();
        userMessage.set("role", "user");
        userMessage.set("content", "你是谁？");

        input.set("messages", JSONUtil.createArray().set(systemMessage).set(userMessage));
        requestBody.set("input", input);

        JSONObject parameters = new JSONObject();
        parameters.set("result_format", "message");
        requestBody.set("parameters", parameters);

        // 发送 HTTP 请求
        HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .timeout(5000) // 设置超时时间（毫秒）
                .execute();

        // 处理响应
        if (response.isOk()) {
            String responseBody = response.body();
            System.out.println("响应成功: " + responseBody);

            // 如果需要解析 JSON 响应
            JSONObject jsonResponse = JSONUtil.parseObj(responseBody);
            System.out.println("JSON 解析: " + jsonResponse);
        } else {
            System.err.println("请求失败，状态码: " + response.getStatus());
            System.err.println("错误信息: " + response.body());
        }
    }
}