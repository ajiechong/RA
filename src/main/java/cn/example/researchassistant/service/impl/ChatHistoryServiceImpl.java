package cn.example.researchassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.researchassistant.entity.ChatHistory;
import org.example.researchassistant.mapper.ChatHistoryMapper;
import org.example.researchassistant.service.AIService;
import org.example.researchassistant.service.ChatHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    @Autowired
    private ChatHistoryMapper chatHistoryMapper;

    @Autowired
    private AIService aiService;
    //logger
    private Logger logger = LoggerFactory.getLogger(ChatHistoryServiceImpl.class);


    @Override
    public List<ChatHistory> getUserChatSessions(Integer uid) {
        List<ChatHistory> allUserChats = chatHistoryMapper.getChatHistoryByUid(uid);

        logger.info("Retrieved chat sessions for user {}: {}", uid, allUserChats);
        return allUserChats.stream()
                .collect(Collectors.groupingBy(ChatHistory::getCid))
                .values()
                .stream()
                .map(chatList ->
                        chatList.stream()
                                .max(Comparator.comparing(ChatHistory::getHtime))
                                .orElse(chatList.get(0))
                )
                .collect(Collectors.toList());
    }
    @Override
    public List<ChatHistory> getChatHistoryByCid(Integer cid) {
        return  chatHistoryMapper.getChatHistoryByCid(cid);
    }

    @Override
    public Integer createNewChatSession(Integer uid, String initialMessage) {
        Integer newCid = (int) (System.currentTimeMillis() / 1000);

        // 1. 插入系统指令（使用 Mapper 通用插入方法）
        ChatHistory systemPrompt = new ChatHistory();
        systemPrompt.setCid(newCid);
        systemPrompt.setUid(uid);
        systemPrompt.setHtime(LocalDateTime.now());
        systemPrompt.setContent("你是一个用于计算机领域的科研助手...");
        systemPrompt.setHName("新对话");
        systemPrompt.setRole("system");

        chatHistoryMapper.insert(systemPrompt);

        // 2. 插入AI欢迎语（同上）
        ChatHistory welcomeMessage = new ChatHistory();
        welcomeMessage.setCid(newCid);
        welcomeMessage.setUid(uid);
        welcomeMessage.setHtime(LocalDateTime.now());
        welcomeMessage.setContent("你好！我是AI科研助手，希望能帮助你解决更多有关计算机的科研问题...");
        welcomeMessage.setHName("新对话");
        welcomeMessage.setRole("assistant");
        chatHistoryMapper.insert(welcomeMessage);

        // 3. 处理用户初始消息
        if (initialMessage != null && !initialMessage.trim().isEmpty()) {
            ChatHistory userMessage = new ChatHistory();
            userMessage.setCid(newCid);
            userMessage.setUid(uid);
            userMessage.setHtime(LocalDateTime.now());
            userMessage.setContent(initialMessage);
            userMessage.setHName(initialMessage.length() > 6 ?
                    initialMessage.substring(0, 6) : initialMessage);
            userMessage.setRole("user");
            chatHistoryMapper.insert(userMessage);
        }

        return newCid;

    }

    @Override
    public ChatHistory sendMessage(Integer cid, Integer uid, String message, Boolean shouldSave) {
        try {
            String aiReply = aiService.getAIResponse(cid, message);
            if (aiReply.equals("sorry科研助手出了点小问题...")) {
                ChatHistory errorResponse = new ChatHistory();
                errorResponse.setContent(aiReply);
                errorResponse.setRole("assistant");
                return errorResponse;
            }

            if (shouldSave) {
                // 保存用户消息（使用 Mapper 插入）
                ChatHistory userMessage = new ChatHistory();
                userMessage.setCid(cid);
                userMessage.setUid(uid);
                userMessage.setHtime(LocalDateTime.now());
                userMessage.setContent(message);
                userMessage.setRole("user");

                // ✅ 调用 Mapper 自定义方法
                ChatHistory lastMessage = chatHistoryMapper.getLastMessageByCid(cid);
                String chatName = lastMessage != null ? lastMessage.getHName() :
                        (message.length() > 6 ? message.substring(0, 6) : message);
                userMessage.setHName(chatName);
                chatHistoryMapper.insert(userMessage); // 插入用户消息

                // 保存AI响应
                ChatHistory aiResponse = new ChatHistory();
                aiResponse.setCid(cid);
                aiResponse.setUid(uid);
                aiResponse.setHtime(LocalDateTime.now());
                aiResponse.setContent(aiReply);
                aiResponse.setHName(chatName);
                aiResponse.setRole("assistant");
                chatHistoryMapper.insert(aiResponse); // 插入AI响应
                return aiResponse;
            } else {
                ChatHistory errorResponse = new ChatHistory();
                errorResponse.setContent(aiReply);
                errorResponse.setRole("assistant");
                return errorResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException("发送消息失败: " + e.getMessage());
        }
    }
    @Override
    public void deleteChatSession(Integer cid) {
        chatHistoryMapper.delete(new QueryWrapper<ChatHistory>().eq("Cid", cid));
    }

    @Override
    public void updateChatSessionName(Integer cid, String newName) {
        List<ChatHistory> chatHistories = chatHistoryMapper.getChatHistoryByCid(cid);

        for (ChatHistory chat : chatHistories) {
            chat.setHName(newName);
            chatHistoryMapper.update(chat,
                    new QueryWrapper<ChatHistory>().eq("Hid", chat.getHid())
            );
        }
    }
}
