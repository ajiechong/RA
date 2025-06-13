package org.example.researchassistant.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.researchassistant.domain.FileAttachment;
import org.example.researchassistant.dto.QwenResponse;
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
    private final ChatHistoryMapper chatHistoryMapper;
    private final String apiKey;
    private final String apiUrl;
    private final String model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${chat.context.message-limit}")
    private Integer messageLimit;

    // 通义千问API配置
    @Value("${dashscope.api.key:sk-5149b2326b014dc5a3a11cfe4450ea68}")
    private String qwenApiKey;

    @Value("${dashscope.api.url:https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions}")
    private String qwenApiUrl;

    @Autowired
    public AIServiceImpl(
            RestTemplate restTemplate,
            ChatHistoryMapper chatHistoryMapper,
            @Value("${my.custom.openai.api-key}") String apiKey,
            @Value("${openai.api-url}") String apiUrl,
            @Value("${openai.model}") String model) {

        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.chatHistoryMapper = chatHistoryMapper;
        this.apiUrl = apiUrl;
        this.model = model;
        log.info("Initialized AI service with OpenAI and Qwen APIs");
    }

    @Override
    public String getAIResponse(Integer cid, String userMessage) {
        try {
            log.info("准备发送 OpenAI 请求 - cid: {}, message: {}", cid, userMessage);

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

    // ===== 新增的通义千问文件解析功能 =====

    @Override
    public String parseFileWithQwen(FileAttachment file) {
        try {
            String fileType = file.getFileType().toLowerCase();

            switch (fileType) {
                case "pdf":
                case "docx":
                case "doc":
                    return parseDocumentWithQwen(file);
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                case "bmp":
                    return parseImageWithQwen(file.getOssUrl());
                case "txt":
                    return parseTextWithQwen(file);
                default:
                    return "暂不支持该文件类型的AI解析";
            }
        } catch (Exception e) {
            log.error("通义千问文件解析失败: {}", e.getMessage(), e);
            return "AI文件解析失败: " + e.getMessage();
        }
    }

    @Override
    public String parseImageWithQwen(String imageUrl) {
        try {
            log.info("使用通义千问VL解析图片: {}", imageUrl);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-vl-plus");
            requestBody.put("max_tokens", 2000);
            requestBody.put("temperature", 0.1);

            // 构建消息内容
            List<Map<String, Object>> content = new ArrayList<>();

            // 添加图片
            Map<String, Object> imageContent = new HashMap<>();
            imageContent.put("type", "image_url");
            Map<String, String> imageUrl_map = new HashMap<>();
            imageUrl_map.put("url", imageUrl);
            imageContent.put("image_url", imageUrl_map);
            content.add(imageContent);

            // 添加文本提示
            Map<String, Object> textContent = new HashMap<>();
            textContent.put("type", "text");
            textContent.put("text", "请详细描述这张图片的内容，包括图片中的文字、图表、人物、场景等所有可见信息。如果是文档截图，请尽可能准确地提取其中的文字内容。");
            content.add(textContent);

            // 构建消息
            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", content);

            List<Map<String, Object>> messages = new ArrayList<>();
            messages.add(message);
            requestBody.put("messages", messages);

            // 发送请求并直接获取结果
            String result = sendQwenRequest(requestBody);

            if (result != null && !result.trim().isEmpty()) {
                log.info("通义千问VL解析成功，内容长度: {}", result.length());
                return result;
            } else {
                log.error("通义千问VL返回空内容");
                return null;
            }

        } catch (Exception e) {
            log.error("图片AI解析失败", e);
            return null;
        }
    }

    @Override
    public String extractTextFromImageWithQwen(String imageUrl) {
        try {
            log.info("使用通义千问OCR提取图片文字: {}", imageUrl);

            // 使用OCR模型提取文字
            List<Map<String, Object>> messages = new ArrayList<>();
            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");

            List<Map<String, Object>> content = new ArrayList<>();

            // 添加图片
            Map<String, Object> imageContent = new HashMap<>();
            imageContent.put("type", "image_url");
            Map<String, String> imageUrl_obj = new HashMap<>();
            imageUrl_obj.put("url", imageUrl);
            imageContent.put("image_url", imageUrl_obj);
            content.add(imageContent);

            // 添加OCR提示
            Map<String, Object> textContent = new HashMap<>();
            textContent.put("type", "text");
            textContent.put("text", "请提取图片中的所有文字内容，保持原有的格式和结构。");
            content.add(textContent);

            userMessage.put("content", content);
            messages.add(userMessage);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-vl-ocr-latest");
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.01);
            requestBody.put("max_tokens", 2000);

            String response = sendQwenRequest(requestBody);
            //return parseQwenResponse(response);
            return response;

        } catch (Exception e) {
            log.error("图片文字提取失败", e);
            return "图片文字提取失败: " + e.getMessage();
        }
    }

    private String parseDocumentWithQwen(FileAttachment file) {
        try {
            // 对于文档类型，先尝试获取已有的文本内容，然后让AI进行总结和分析
            String existingText = file.getExtractedText();
            if (existingText == null || existingText.trim().isEmpty() || existingText.contains("提取失败")) {
                return "文档内容提取失败，无法进行AI分析";
            }

            // 构建AI分析请求
            List<Map<String, Object>> messages = new ArrayList<>();

            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一个专业的文档分析助手，能够理解和总结各种文档内容。");
            messages.add(systemMessage);

            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", String.format(
                    "请分析以下文档内容，并提供一个结构化的总结：\n\n" +
                            "文档类型：%s\n" +
                            "文档名称：%s\n\n" +
                            "文档内容：\n%s\n\n" +
                            "请提供：\n" +
                            "1. 文档主要内容概述\n" +
                            "2. 关键信息提取\n" +
                            "3. 文档结构分析\n" +
                            "4. 重要数据或结论",
                    file.getFileType(), file.getFileName(), existingText
            ));
            messages.add(userMessage);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-plus");
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.3);
            requestBody.put("max_tokens", 2000);

            String aiAnalysis = sendQwenRequest(requestBody);

            // 检查AI分析结果
            if (aiAnalysis != null && !aiAnalysis.trim().isEmpty()) {
                // 返回原文档内容 + AI分析
                return String.format("=== 文档内容 ===\n%s\n\n=== AI智能分析 ===\n%s",
                        existingText, aiAnalysis);
            } else {
                // 如果AI分析失败，只返回原文档内容
                log.warn("AI分析失败，只返回原文档内容");
                return existingText;
            }

        } catch (Exception e) {
            log.error("文档AI分析失败", e);
            return "文档AI分析失败: " + e.getMessage();
        }
    }

    private String parseTextWithQwen(FileAttachment file) {
        try {
            String textContent = file.getExtractedText();
            if (textContent == null || textContent.trim().isEmpty()) {
                return "文本文件内容为空";
            }

            // 对文本内容进行AI分析
            List<Map<String, Object>> messages = new ArrayList<>();

            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一个文本分析助手，能够总结和分析文本内容。");
            messages.add(systemMessage);

            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", String.format(
                    "请分析以下文本内容：\n\n%s\n\n" +
                            "请提供简洁的内容摘要和要点提取。", textContent
            ));
            messages.add(userMessage);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-plus");
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.3);
            requestBody.put("max_tokens", 1000);

            String aiAnalysis = sendQwenRequest(requestBody);

            // 检查AI分析结果
            if (aiAnalysis != null && !aiAnalysis.trim().isEmpty()) {
                return String.format("=== 原文内容 ===\n%s\n\n=== AI内容分析 ===\n%s",
                        textContent, aiAnalysis);
            } else {
                // 如果AI分析失败，只返回原文内容
                log.warn("文档AI分析失败，只返回原文内容");
                return textContent;
            }

        } catch (Exception e) {
            log.error("文本AI分析失败", e);
            return file.getExtractedText(); // 返回原始内容
        }
    }

    /**
     * 发送通义千问API请求
     */
    private String sendQwenRequest(Map<String, Object> requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(qwenApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            log.info("发送通义千问API请求到: {}", qwenApiUrl);

            // 使用DTO接收响应
            ResponseEntity<QwenResponse> response = restTemplate.exchange(
                    qwenApiUrl,
                    HttpMethod.POST,
                    entity,
                    QwenResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                QwenResponse responseBody = response.getBody();

                if (responseBody.getChoices() != null && !responseBody.getChoices().isEmpty()) {
                    QwenResponse.Choice firstChoice = responseBody.getChoices().get(0);
                    if (firstChoice.getMessage() != null) {
                        String content = firstChoice.getMessage().getContent();
                        log.info("通义千问API响应成功，内容长度: {}", content != null ? content.length() : 0);
                        return content; // 直接返回内容字符串
                    }
                }

                log.error("通义千问API响应格式异常: {}", responseBody);
                return null;
            } else {
                log.error("通义千问API请求失败，状态码: {}", response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            log.error("通义千问API请求异常", e);
            return null;
        }
    }

    /**
     * 解析通义千问API响应
     */
//    private String parseQwenResponse(String response) throws Exception {
//        JsonNode jsonNode = objectMapper.readTree(response);
//
//        if (jsonNode.has("error")) {
//            throw new RuntimeException("通义千问API返回错误: " + jsonNode.get("error").get("message").asText());
//        }
//
//        JsonNode choices = jsonNode.get("choices");
//        if (choices != null && choices.size() > 0) {
//            JsonNode firstChoice = choices.get(0);
//            JsonNode message = firstChoice.get("message");
//            if (message != null && message.has("content")) {
//                return message.get("content").asText();
//            }
//        }
//
//        throw new RuntimeException("无法解析通义千问API响应");
//    }
}