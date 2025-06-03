package org.example.researchassistant.dao;

import org.example.researchassistant.domain.FavoriteChat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteChatDao {
    // 添加收藏
    int insertFavoriteChat(FavoriteChat favoriteChat);

    // 取消收藏
    int deleteFavoriteChat(Integer uid, Integer cid);

    // 获取用户所有收藏的对话
    List<FavoriteChat> getFavoriteChatsByUid(Integer uid);

    // 检查是否已收藏
    boolean checkFavoriteChat(Integer uid, Integer cid);
}