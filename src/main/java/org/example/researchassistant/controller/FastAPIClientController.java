package org.example.researchassistant.controller;

import org.example.researchassistant.service.FastAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping("/api")
public class FastAPIClientController {
    private final FastAPIService fastAPIService;

    public FastAPIClientController(FastAPIService fastAPIService) {
        this.fastAPIService = fastAPIService;
    }

    @PostMapping("/chat/{role}")
    public ResponseEntity<Map<String, Object>> chat(
            @PathVariable String role,
            @RequestBody Map<String, String> request
    ) throws ExecutionException, InterruptedException {
        String query = request.get("query");
        String context = request.get("context");
        if (query == null || query.isEmpty()) {
            return badRequest().body(Map.of(
                    "error", "query 参数为必填项"
            ));
        }

        Map<String, Object> fastAPIRequest = new HashMap<>();
        fastAPIRequest.put("query", query);
        fastAPIRequest.put("context", context);

        // 4. 调用Agent获取生成的响应内容
        Map<String, Object> agentResponse = fastAPIService.chatWithAgent(role, fastAPIRequest);
        if (!agentResponse.containsKey("response")) {
            return badRequest().body(Map.of("error", "Agent响应缺少'response'字段"));
        }
        System.out.println(ResponseEntity.ok(agentResponse));
        return ResponseEntity.ok(agentResponse);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return fastAPIService.healthCheck();
    }

    @PostMapping("/rag/query")   //rag直接查询
    public ResponseEntity<Map<String, Object>> queryRag(
            @RequestBody Map<String, Object> requestBody  // 接收整个 JSON 请求体
    ) {
        // 提取必填参数 query
        String query = (String) requestBody.get("query");

        if (query == null || query.isEmpty()) {
            return badRequest().body(Map.of(
                    "error", "query 参数为必填项"
            ));
        }

        // 提取可选参数 fields（默认 null）
        List<String> fields = (List<String>) requestBody.get("fields");
        // 提取可选参数 top_n（默认 null）
        Integer top_n = (Integer) requestBody.get("top_n");
        System.out.println(fastAPIService.queryRag(query, fields, top_n));
        // 调用服务层，传递参数
        return fastAPIService.queryRag(query, fields, top_n);
    }
}
