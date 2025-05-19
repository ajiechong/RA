package org.example.researchassistant.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.researchassistant.entity.ChatHistory;

import java.util.List;

@Mapper
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {
    // ====================== 自定义查询方法 ======================
    // 1. 根据会话 ID 查询聊天记录（按时间升序）
    default List<ChatHistory> getChatHistoryByCid(int cid) {
        return this.selectList(
                new QueryWrapper<ChatHistory>()
                        .eq("Cid", cid)
                        .orderByAsc("Htime")
        );
    }

    // 2. 根据用户 ID 查询聊天记录（按时间降序）
    default List<ChatHistory> getChatHistoryByUid(int uid) {
        return this.selectList(
                new QueryWrapper<ChatHistory>()
                        .eq("Uid", uid)
                        .orderByDesc("Htime")
        );
    }

    // 3. 获取特定会话的最后一条消息
    default ChatHistory getLastMessageByCid(int cid) {
        return this.selectOne(
                new QueryWrapper<ChatHistory>()
                        .eq("Cid", cid)
                        .orderByDesc("Htime")
                        .last("LIMIT 1") // 直接拼接 SQL 片段（注意安全）
        );
    }

    // ====================== 通用方法（继承自 BaseMapper）=====================
    // 通用查询：
    // selectList(null) - 获取所有记录（对应原 getAllChatHistory）
    // selectById(chatId) - 根据主键查询
    // insert(chatHistory) - 插入（对应原 insertChatHistory）
    // updateById(chatHistory) - 更新（建议用条件构造器替代原 updateChatHistory）
    // delete(Wrapper) - 删除（建议用条件构造器替代原 deleteChatHistory）
}