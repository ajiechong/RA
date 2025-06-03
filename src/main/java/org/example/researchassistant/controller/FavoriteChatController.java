package org.example.researchassistant.controller;

import org.example.researchassistant.domain.FavoriteChat;
import org.example.researchassistant.service.FavoriteChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chathistories") // 修改为与前端一致的路径
public class FavoriteChatController {

    private final FavoriteChatService favoriteChatService;

    @Autowired
    public FavoriteChatController(FavoriteChatService favoriteChatService) {
        this.favoriteChatService = favoriteChatService;
    }

    // 添加收藏 - 修改为POST请求，接收JSON body
    @PostMapping("/favorites")
    public Map<String, Object> addFavorite(@RequestBody Map<String, Integer> request) {
        Integer uid = request.get("uid");
        Integer cid = request.get("cid");
        return favoriteChatService.addFavoriteChat(uid, cid);
    }

    // 取消收藏 - 修改为DELETE请求，路径参数
    @DeleteMapping("/favorites/{uid}/{cid}")
    public Map<String, Object> cancelFavorite(
            @PathVariable Integer uid,
            @PathVariable Integer cid) {
        return favoriteChatService.cancelFavoriteChat(uid, cid);
    }

    // 切换收藏状态
    @PostMapping("/favorites/toggle")
    public Map<String, Object> toggleFavorite(@RequestBody Map<String, Integer> request) {
        Integer uid = request.get("uid");
        Integer cid = request.get("cid");
        return favoriteChatService.toggleFavorite(uid, cid);
    }

    // 获取用户所有收藏的对话 - 修改为与前端一致的路径
    @GetMapping("/favorites/{uid}")
    public List<FavoriteChat> getFavoriteChats(@PathVariable Integer uid) {
        return favoriteChatService.getFavoriteChatsByUid(uid);
    }

    // 检查是否已收藏
    @GetMapping("/favorites/check")
    public boolean checkFavorite(
            @RequestParam Integer uid,
            @RequestParam Integer cid) {
        return favoriteChatService.checkFavorite(uid, cid);
    }
}