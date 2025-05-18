package cn.example.researchassistant.service;

import org.example.researchassistant.entity.ChatHistory;

import java.util.List;

//
public interface ChatHistoryService {
    List<ChatHistory> getUserChatSessions(Integer uid);
    List<ChatHistory> getChatHistoryByCid(Integer cid);
    Integer createNewChatSession(Integer uid, String initialMessage);
    ChatHistory sendMessage(Integer cid, Integer uid, String message, Boolean shouldSave);
    void deleteChatSession(Integer cid);
    void updateChatSessionName(Integer cid, String newName);
}
