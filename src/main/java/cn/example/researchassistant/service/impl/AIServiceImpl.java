package cn.example.researchassistant.service.impl;

import org.example.researchassistant.entity.ChatHistory;
import org.example.researchassistant.mapper.ChatHistoryMapper;
import org.example.researchassistant.service.AIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIServiceImpl implements AIService {
    private static final Logger log = LoggerFactory.getLogger(AIServiceImpl.class);
    private final RestTemplate restTemplate;
    private final ChatHistoryMapper chatHistoryMapper; // Mapper
    private final String apiKey;
    private final String apiUrl;
    private final String model;

    @Value("${chat.context.message-limit}")
    private Integer messageLimit;  // default saved message limit

    @Autowired
    public AIServiceImpl(
            RestTemplate restTemplate,
            ChatHistoryMapper chatHistoryMapper,
            @Value("${my.custom.openai.api-key}") String apiKey,
            @Value("${openai.api-url}") String apiUrl,
            @Value("${openai.model}") String model) {

        // 确保使用配置文件中的API key
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.chatHistoryMapper = chatHistoryMapper;
        this.apiUrl = apiUrl;
        this.model = model;
        log.info("Initialized OpenAI service with API key from application.properties");
    }

    @Override
    public String getAIResponse(Integer cid, String userMessage) {
        try {
            log.info("准备发送 OpenAI 请求 - cid: {}, message: {}", cid, userMessage);

            // 在这里也打印一下
            log.info("Using API key: {}", apiKey);
            // 获取历史消息
            List<ChatHistory> historyMessages = chatHistoryMapper.getChatHistoryByCid(cid);

            // 只保留最近的n条消息
            if (historyMessages.size() > messageLimit) {
                historyMessages = historyMessages.subList(
                        historyMessages.size() - messageLimit,
                        historyMessages.size()
                );
            }
            // 构建消息列表
            List<Map<String, Object>> messages = new ArrayList<>();
            // 添加历史消息
            for (ChatHistory history : historyMessages) {
                Map<String, Object> message = new HashMap<>();
                message.put("role", history.getRole());
                message.put("content", history.getContent());
                messages.add(message);
            }

            // 添加当前用户消息
            Map<String, Object> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);

            String fullUrl = apiUrl + "/chat/completions";
            log.info("Using fullUrl: {}", fullUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            log.info("收到 API 响应 - Status: {}", responseEntity.getStatusCode());
            Map responseBody = responseEntity.getBody();

            if (responseBody == null) {
                throw new RuntimeException("API returned null response");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null && message.containsKey("content")) {
                    return (String) message.get("content");
                }
            }

            throw new RuntimeException("Invalid response format: " + responseBody);

        } catch (Exception e) {
            log.error("OpenAI API call failed", e);
            return "科研助理出了点小问题，请稍候再试试吧~";
        }
    }
}