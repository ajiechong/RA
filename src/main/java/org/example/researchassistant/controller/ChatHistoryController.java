package org.example.researchassistant.controller;
import org.example.researchassistant.domain.ChatHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.researchassistant.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/chathistories")
public class ChatHistoryController {
    private static final Logger logger = LoggerFactory.getLogger(ChatHistoryController.class);
    @Autowired
    private ChatHistoryService chatHistoryService;

    @GetMapping("/sessions/{uid}")
    public List<ChatHistory> getUserSessions(@PathVariable Integer uid) {
        return chatHistoryService.getUserChatSessions(uid);
    }

    @PostMapping("/sessions/new")
    public Integer createNewChatSession(@RequestBody Map<String, Object> request) {
        Integer uid = ((Number) request.get("uid")).intValue();
        String initialMessage = (String) request.get("initialMessage");
        return chatHistoryService.createNewChatSession(uid, initialMessage);
    }
    @GetMapping("/messages/{cid}")
    public List<ChatHistory> getChatMessages(@PathVariable Integer cid) {
        return chatHistoryService.getChatHistoryByCid(cid);
    }


    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> request) {
        try {
            logger.info("收到发送消息请求");
            Integer cid = ((Number) request.get("cid")).intValue();
            Integer uid = ((Number) request.get("uid")).intValue();
            String content = (String) request.get("content");
            Boolean shouldSave = (Boolean) request.getOrDefault("shouldSave", true);

            logger.debug("Message params - cid: {}, uid: {}, content: {}, shouldSave: {}",
                    cid, uid, content, shouldSave);

            ChatHistory response = chatHistoryService.sendMessage(cid, uid, content, shouldSave);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("处理消息时发生错误", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("details", e.getCause() != null ? e.getCause().getMessage() : "未知错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }


    // 在 ChatHistoryController 类中添加新的端点
    @DeleteMapping("/sessions/{cid}")
    public ResponseEntity<?> deleteChatSession(@PathVariable Integer cid) {
        try {
            chatHistoryService.deleteChatSession(cid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting chat session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete chat session");
        }
    }

    @PutMapping("/sessions/{cid}/rename")
    public ResponseEntity<?> renameChatSession(
            @PathVariable Integer cid,
            @RequestBody Map<String, String> request) {
        try {
            String newName = request.get("newName");
            if (newName == null || newName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("New name cannot be empty");
            }
            chatHistoryService.updateChatSessionName(cid, newName.trim());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error renaming chat session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to rename chat session");
        }
    }
}