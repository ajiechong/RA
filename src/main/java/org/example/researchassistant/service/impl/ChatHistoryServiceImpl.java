package org.example.researchassistant.service.impl;

import org.example.researchassistant.controller.ResourceVideoController;
import org.example.researchassistant.dao.ChatHistoryDao;
import org.example.researchassistant.dao.impl.ChatHistoryDaoImpl;
import org.example.researchassistant.domain.ChatHistory;
import org.example.researchassistant.service.AIService;
import org.example.researchassistant.service.ChatHistoryService;
import org.example.researchassistant.service.FileService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private ChatHistoryDao chatHistoryDao = new ChatHistoryDaoImpl();

    @Autowired
    private AIService aiService;

    @Autowired
    private FileService fileService;  // 注入文件服务

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResourceVideoController.class);

    @Override
    public List<ChatHistory> getUserChatSessions(Integer uid) {

        List<ChatHistory> allUserChats = chatHistoryDao.getChatHistoryByUid(uid);
        logger.info("Retrieved chat sessions for user {}: {}", uid, allUserChats);
        return allUserChats.stream()
                .collect(Collectors.groupingBy(ChatHistory::getCid))
                .values()
                .stream()
                .map(chatList -> {
                    // 获取最新的消息而不是第一条消息
                    return chatList.stream()
                            .max(Comparator.comparing(ChatHistory::getHtime))
                            .orElse(chatList.get(0));
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatHistory> getChatHistoryByCid(Integer cid) {
        return chatHistoryDao.getChatHistoryByCid(cid);
    }

    @Override
    public Integer createNewChatSession(Integer uid, String initialMessage) {
        // 生成新的会话ID
        Integer newCid = (int) (System.currentTimeMillis() / 1000);

        // 1. 首先插入系统指令
        ChatHistory systemPrompt = new ChatHistory();
        systemPrompt.setCid(newCid);
        systemPrompt.setUid(uid);
        systemPrompt.setHtime(LocalDateTime.now());
        systemPrompt.setContent("你是一个专业的学术智能助手，名字叫\"RA\"，因为用户都是非常用心学习的学生，因此你要将你的专业性体现出来，完美地回答用户问你的问题");
        systemPrompt.setHName("新对话");
        systemPrompt.setRole("system");  // 使用特殊的role标记系统指令

        chatHistoryDao.insertChatHistory(systemPrompt);

        // 2. 插入AI的欢迎语
        ChatHistory welcomeMessage = new ChatHistory();
        welcomeMessage.setCid(newCid);
        welcomeMessage.setUid(uid);
        welcomeMessage.setHtime(LocalDateTime.now());
        welcomeMessage.setContent("你好！我是你的学术智能助手\"RA\"，很高兴和你一起聊天😊");
        welcomeMessage.setHName("新对话");
        welcomeMessage.setRole("assistant");

        chatHistoryDao.insertChatHistory(welcomeMessage);

        // 3. 如果有初始消息，则创建用户的消息记录
        if (initialMessage != null && !initialMessage.trim().isEmpty()) {
            ChatHistory userMessage = new ChatHistory();
            userMessage.setCid(newCid);
            userMessage.setUid(uid);
            userMessage.setHtime(LocalDateTime.now());
            userMessage.setContent(initialMessage);
            userMessage.setHName(initialMessage.length() > 6 ?
                    initialMessage.substring(0, 6) : initialMessage);
            userMessage.setRole("user");

            chatHistoryDao.insertChatHistory(userMessage);
        }

        return newCid;
    }

    @Override
    public ChatHistory sendMessage(Integer cid, Integer uid, String message, Boolean shouldSave) {
        try {
            // 获取会话中最近的文件上下文（最多3个文件）
            String fileContext = fileService.getRecentFilesContext(cid, 3);

            // 构建包含文件上下文的完整消息
            String messageWithContext = message;
            if (!fileContext.isEmpty()) {
                messageWithContext = fileContext + "\n\n用户问题: " + message +
                        "\n\n请结合上述文件内容回答用户的问题。";
            }

            // 获取AI响应（传入包含文件上下文的消息）
            String aiReply = aiService.getAIResponse(cid, messageWithContext);

            // 如果是错误消息，直接返回但不保存
            if (aiReply.equals("RA出了点小问题，后面再试试吧~😀")) {
                ChatHistory errorResponse = new ChatHistory();
                errorResponse.setContent(aiReply);
                errorResponse.setRole("assistant");
                return errorResponse;
            }

            // 只有在需要保存且不是错误消息时才保存到数据库
            if (shouldSave) {
                // 保存用户消息（保存原始消息，不包含文件上下文）
                ChatHistory userMessage = new ChatHistory();
                userMessage.setCid(cid);
                userMessage.setUid(uid);
                userMessage.setHtime(LocalDateTime.now());
                userMessage.setContent(message);  // 原始消息
                userMessage.setRole("user");

                ChatHistory existingChat = chatHistoryDao.getLastMessageByCid(cid);
                String chatName = existingChat != null ? existingChat.getHName() :
                        (message.length() > 6 ? message.substring(0, 6) : message);
                userMessage.setHName(chatName);

                chatHistoryDao.insertChatHistory(userMessage);

                // 保存AI响应
                ChatHistory aiResponse = new ChatHistory();
                aiResponse.setCid(cid);
                aiResponse.setUid(uid);
                aiResponse.setHtime(LocalDateTime.now());
                aiResponse.setContent(aiReply);
                aiResponse.setHName(chatName);
                aiResponse.setRole("assistant");

                chatHistoryDao.insertChatHistory(aiResponse);
                return aiResponse;
            } else {
                // 不保存到数据库，只返回响应
                ChatHistory aiResponse = new ChatHistory();
                aiResponse.setContent(aiReply);
                aiResponse.setRole("assistant");
                return aiResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException("发送消息失败: " + e.getMessage());
        }
    }

//    @Override
//    public ChatHistory sendMessage(Integer cid, Integer uid, String message, Boolean shouldSave) {
//        try {
//            // 获取AI响应
//            String aiReply = aiService.getAIResponse(cid, message);
//
//            // 如果是错误消息，直接返回但不保存
//            if (aiReply.equals("沙包出了点小问题，后面再试试吧~😀")) {
//                ChatHistory errorResponse = new ChatHistory();
//                errorResponse.setContent(aiReply);
//                errorResponse.setRole("assistant");
//                return errorResponse;
//            }
//
//            // 只有在需要保存且不是错误消息时才保存到数据库
//            if (shouldSave) {
//                // 保存用户消息
//                ChatHistory userMessage = new ChatHistory();
//                userMessage.setCid(cid);
//                userMessage.setUid(uid);
//                userMessage.setHtime(LocalDateTime.now());
//                userMessage.setContent(message);
//                userMessage.setRole("user");
//
//                ChatHistory existingChat = chatHistoryDao.getLastMessageByCid(cid);
//                String chatName = existingChat != null ? existingChat.getHName() :
//                        (message.length() > 6 ? message.substring(0, 6) : message);
//                userMessage.setHName(chatName);
//
//                chatHistoryDao.insertChatHistory(userMessage);
//
//                // 保存AI响应
//                ChatHistory aiResponse = new ChatHistory();
//                aiResponse.setCid(cid);
//                aiResponse.setUid(uid);
//                aiResponse.setHtime(LocalDateTime.now());
//                aiResponse.setContent(aiReply);
//                aiResponse.setHName(chatName);
//                aiResponse.setRole("assistant");
//
//                chatHistoryDao.insertChatHistory(aiResponse);
//                return aiResponse;
//            } else {
//                // 不保存到数据库，只返回响应
//                ChatHistory aiResponse = new ChatHistory();
//                aiResponse.setContent(aiReply);
//                aiResponse.setRole("assistant");
//                return aiResponse;
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("发送消息失败: " + e.getMessage());
//        }
//    }


    @Override
    public void deleteChatSession(Integer cid) {
        chatHistoryDao.deleteChatHistory("Cid = " + cid);
    }

    @Override
    public void updateChatSessionName(Integer cid, String newName) {
        List<ChatHistory> chatHistories = chatHistoryDao.getChatHistoryByCid(cid);
        for (ChatHistory chat : chatHistories) {
            chat.setHName(newName);
            chatHistoryDao.updateChatHistory(chat, "Hid = " + chat.getHid());
        }
    }
    @Override
    public List<org.example.researchassistant.entity.ChatHistory> findFirst5ByCidAndUidOrderByHtimeDesc(int cid, int uid) {
        return chatHistoryDao.findFirst5ByCidAndUidOrderByHtimeDesc(cid, uid);
    }
}
