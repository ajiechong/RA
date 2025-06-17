package org.example.researchassistant.service;

import org.example.researchassistant.domain.ChatHistory;

import java.util.List;

public interface ChatHistoryService {
    List<ChatHistory> getUserChatSessions(Integer uid);
    List<ChatHistory> getChatHistoryByCid(Integer cid);
    Integer createNewChatSession(Integer uid, String initialMessage);
    ChatHistory sendMessage(Integer cid, Integer uid, String message, Boolean shouldSave);
    void deleteChatSession(Integer cid);
    void updateChatSessionName(Integer cid, String newName);

    List<org.example.researchassistant.entity.ChatHistory> findFirst5ByCidAndUidOrderByHtimeDesc(int cid, int uid);
}
