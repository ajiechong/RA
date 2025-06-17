package org.example.researchassistant.controller;

import org.example.researchassistant.dao.ChatHistoryDao;
import org.example.researchassistant.dao.impl.ChatHistoryDaoImpl;
import org.example.researchassistant.entity.ChatHistory;
import org.example.researchassistant.entity.RagResponse;
import org.example.researchassistant.entity.RagResult;
import org.example.researchassistant.service.FastAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal.executorService;

@RestController
@RequestMapping("/api")
public class FastAPIClientController {
    private final FastAPIService fastAPIService;
    private ChatHistoryDao chatHistoryDao = new ChatHistoryDaoImpl();

    public FastAPIClientController(FastAPIService fastAPIService,
                                   ChatHistoryDao chatHistoryDao) {
        this.fastAPIService = fastAPIService;
        this.chatHistoryDao = chatHistoryDao;
    }

    @PostMapping("/chat/{role}")
    public ResponseEntity<Map<String, Object>> chat(
            @PathVariable String role,
            @RequestBody Map<String, String> request,
            @RequestParam int chatId,
            @RequestParam int userId
    ) throws ExecutionException, InterruptedException {
        String query = request.get("query");
        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "query 参数为必填项"
            ));
        }

        // 并发任务异常处理优化
        // 1. 构建上下文（查询最近6条对话并压缩）
        CompletableFuture<String> contextFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return buildContext(Math.toIntExact(chatId), Math.toIntExact(userId), query);
            } catch (Exception e) {
                // 记录异常日志
                System.err.println("构建上下文失败: " + e.getMessage());
                return ""; // 返回默认空字符串
            }
        }, executorService);
        // 2. 执行RAG查询获取原始文献数据
        CompletableFuture<RagResponse> ragFuture = CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<RagResponse> ragResponseEntity = fastAPIService.queryRagStructured(query, null, null);
                if (!ragResponseEntity.getStatusCode().is2xxSuccessful() || !ragResponseEntity.hasBody()) {
                    throw new RuntimeException("RAG查询失败: " + ragResponseEntity.getStatusCode());
                }
                return ragResponseEntity.getBody();
            } catch (Exception e) {
                System.err.println("RAG查询失败: " + e.getMessage());
                return new RagResponse(); // 返回空结果对象
            }
        }, executorService);

        // 等待任务完成并处理结果
        CompletableFuture.allOf(contextFuture, ragFuture).join();
        // 处理并发的数据
        String context = contextFuture.get();
        RagResponse ragResponse = ragFuture.get();
        List<RagResult> results = ragResponse.getResults();

        // 3. 构建传递给Agent的请求参数（包含压缩后的context和RAG结果）
        Map<String, Object> fastAPIRequest = new HashMap<>();
        fastAPIRequest.put("query", query);
        fastAPIRequest.put("context", context); // 使用大模型压缩后的上下文
        fastAPIRequest.put("results", results);
        fastAPIRequest.put("chatId", chatId);
        fastAPIRequest.put("userId", userId);

        // 4. 调用Agent获取生成的响应内容
        Map<String, Object> agentResponse = fastAPIService.chatWithAgent(role, fastAPIRequest);
        if (!agentResponse.containsKey("response")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Agent响应缺少'response'字段"));
        }

        // 5. 从Agent响应中提取原始文本，并追加参考文献
        StringBuilder responseContent = new StringBuilder((String) agentResponse.get("response"));
        responseContent.append("\n\n## 参考文献\n");

        // 过滤并添加有效文献（排除可能的空数据）
        results.stream()
                .filter(Objects::nonNull)
                .filter(result -> !StringUtils.isEmpty(result.getTitle()) && !StringUtils.isEmpty(result.getPdf()))
                .forEach(result -> {
                    responseContent.append("- [")
                            .append(result.getTitle())
                            .append("](")
                            .append(result.getPdf())
                            .append(")\n");
                });

        // 6. 更新响应内容并返回
        agentResponse.put("response", responseContent.toString());
        agentResponse.put("chatId", chatId);
        agentResponse.put("userId", userId);
        System.out.println(ResponseEntity.ok(agentResponse));
        return ResponseEntity.ok(agentResponse);
    }

    private String buildContext(int chatId, int userId, String query) {
        // 查询最近5条对话记录
        List<ChatHistory> recentChats = chatHistoryDao.findFirst5ByCidAndUidOrderByHtimeDesc(chatId, userId);

        // 构建提示词
        StringBuilder prompt = new StringBuilder();
        // 添加空值检查
        if (recentChats == null) {
            recentChats = Collections.emptyList(); // 初始化为空列表而非null
        }
        else{
            // 添加历史对话
            for (ChatHistory chat : recentChats) {
                prompt.append(chat.getRole()).append(": ").append(chat.getContent()).append("\n");
            }
        }

        // 添加当前查询
        prompt.append("\n当前问题: ").append(query);

        // 调用FastAPI接口压缩上下文
        return fastAPIService.callFastAPIContextCompression(prompt.toString());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return fastAPIService.healthCheck();
    }

//    @PostMapping("/rag/query")   rag直接查询
//    public ResponseEntity<Map<String, Object>> queryRag(
//            @RequestBody Map<String, Object> requestBody  // 接收整个 JSON 请求体
//    ) {
//        // 提取必填参数 query
//        String query = (String) requestBody.get("query");
//        if (query == null || query.isEmpty()) {
//            return badRequest().body(Map.of(
//                    "error", "query 参数为必填项"
//            ));
//        }
//
//        // 提取可选参数 fields（默认 null）
//        List<String> fields = (List<String>) requestBody.get("fields");
//        // 提取可选参数 top_n（默认 null）
//        Integer top_n = (Integer) requestBody.get("top_n");
//
//        // 调用服务层，传递参数
//        return fastAPIService.queryRag(query, fields, top_n);
//    }
}
