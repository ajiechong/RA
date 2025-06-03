package org.example.researchassistant.service;

import org.example.researchassistant.dao.FavoriteChatDao;
import org.example.researchassistant.domain.FavoriteChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteChatService {

    private final FavoriteChatDao favoriteChatDao;


    @Autowired
    public FavoriteChatService(FavoriteChatDao favoriteChatDao) {
        this.favoriteChatDao = favoriteChatDao;
    }

    // 添加收藏
    public Map<String, Object> addFavoriteChat(Integer uid, Integer cid) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查是否已收藏
            if (favoriteChatDao.checkFavoriteChat(uid, cid)) {
                result.put("success", false);
                result.put("message", "您已收藏过该对话");
                return result;
            }

            FavoriteChat favoriteChat = new FavoriteChat(uid, cid);
            int affectedRows = favoriteChatDao.insertFavoriteChat(favoriteChat);

            if (affectedRows > 0) {
                result.put("success", true);
                result.put("message", "收藏成功");
            } else {
                result.put("success", false);
                result.put("message", "收藏失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "收藏时出错: " + e.getMessage());
        }
        return result;
    }

    // 取消收藏
    public Map<String, Object> cancelFavoriteChat(Integer uid, Integer cid) {
        Map<String, Object> result = new HashMap<>();
        try {
            int affectedRows = favoriteChatDao.deleteFavoriteChat(uid, cid);

            if (affectedRows > 0) {
                result.put("success", true);
                result.put("message", "已取消收藏");
            } else {
                result.put("success", false);
                result.put("message", "取消收藏失败，可能未收藏该对话");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "取消收藏时出错: " + e.getMessage());
        }
        return result;
    }

    // 获取用户所有收藏的对话 - 增强版本，返回完整的对话信息
    public List<FavoriteChat> getFavoriteChatsByUid(Integer uid) {
        try {
            List<FavoriteChat> favoriteChats = favoriteChatDao.getFavoriteChatsByUid(uid);

            return favoriteChats;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取收藏列表失败: " + e.getMessage());
        }
    }

    // 检查是否已收藏
    public boolean checkFavorite(Integer uid, Integer cid) {
        try {
            return favoriteChatDao.checkFavoriteChat(uid, cid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 切换收藏状态
    public Map<String, Object> toggleFavorite(Integer uid, Integer cid) {
        try {
            if (checkFavorite(uid, cid)) {
                return cancelFavoriteChat(uid, cid);
            } else {
                return addFavoriteChat(uid, cid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
            return result;
        }
    }
}