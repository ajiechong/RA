// main/java/cn/mentalhealth/dao/ChatHistoryDaoImpl.java
package org.example.researchassistant.dao;

import org.example.researchassistant.domain.ChatHistory;
import java.util.List;

public interface ChatHistoryDao {
    // 获取所有聊天记录
    List<ChatHistory> getAllChatHistory();

    // 根据会话ID获取聊天记录
    List<ChatHistory> getChatHistoryByCid(int cid);

    // 根据用户ID获取聊天记录
    List<ChatHistory> getChatHistoryByUid(int uid);

    // 插入新的聊天记录
    void insertChatHistory(ChatHistory chatHistory);

    // 更新聊天记录
    void updateChatHistory(ChatHistory chatHistory, String condition);

    // 删除聊天记录
    void deleteChatHistory(String condition);

    // 获取特定会话的最后一条消息
    ChatHistory getLastMessageByCid(int cid);
}