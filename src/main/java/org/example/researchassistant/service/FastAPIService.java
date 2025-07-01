package org.example.researchassistant.service;
import org.example.researchassistant.dao.ChatHistoryDao;
import org.example.researchassistant.dao.impl.ChatHistoryDaoImpl;
import org.example.researchassistant.domain.ChatHistory;
import org.example.researchassistant.entity.RagResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FastAPIService {
    private static final String FASTAPI_BASE_URL = "http://127.0.0.1:8000";
    private final RestTemplate restTemplate = new RestTemplate();
    private ChatHistoryDao chatHistoryDao = new ChatHistoryDaoImpl();

    public FastAPIService(ChatHistoryDao chatHistoryDao) {
        this.chatHistoryDao = chatHistoryDao;
    }

    public Map<String, Object> chatWithAgent(String role,
                                             Map<String, Object> request) {
            // 构造请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 构建请求实体
            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(request, headers);
            //System.out.println(entity);
            // 发送请求
            String url = FASTAPI_BASE_URL + "/chat/" + role;
            ResponseEntity<Map<String, Object>> remoteResponse = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            //System.out.println(remoteResponse);

            // 记录AI回复
            if (remoteResponse.getStatusCode().is2xxSuccessful() && remoteResponse.getBody() != null) {
                String aiResponse = String.valueOf(remoteResponse.getBody().get("response"));

                ChatHistory aiChat = new ChatHistory();
                aiChat.setHtime(LocalDateTime.now());
                aiChat.setContent(aiResponse);
                aiChat.setRole("ai"); // 标记为AI回复
                aiChat.setHName(aiResponse.length() > 6 ? aiResponse.substring(0, 6) : aiResponse);
                chatHistoryDao.insertChatHistory(aiChat);
            }
            System.out.println(remoteResponse.getBody());
            // 返回远程响应的 body
            return remoteResponse.getBody();
    }

    // 健康检查
    public ResponseEntity<Map<String, Object>> healthCheck() {
        String url = FASTAPI_BASE_URL + "/health";
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
    }

    public ResponseEntity<Map<String, Object>> queryRag(
            String query,
            List<String> fields,
            Integer top_n
    ) {
        try {
            // 构造请求体，处理可选参数
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("query", query);

            // 若 fields 不为空，添加到请求体
            if (fields != null && !fields.isEmpty()) {
                requestBody.put("fields", fields);
            }

            // 若 top_n 有效，添加到请求体
            if (top_n != null && top_n > 0) {
                requestBody.put("top_n", top_n);
            }

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 构建请求实体
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 发送请求到 FastAPI
            String url = FASTAPI_BASE_URL + "/rag/query";
            return restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

        } catch (Exception e) {
            // 处理异常，记录日志
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "RAG 查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
