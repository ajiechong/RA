package cn.example.researchassistant.controller;

import cn.example.researchassistant.service.FavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 用于添加收藏
    @PostMapping("/add")
    public Map<String, Object> addFavorite(@RequestParam int Uid,@RequestParam int Rid, @RequestParam int flag) {
        return favoriteService.addFavorite(Uid, Rid, flag);
    }

    // 用于取消收藏
    @PostMapping("/remove")
    public Map<String, Object> removeFavorite(@RequestParam int Uid,@RequestParam int Rid, @RequestParam int flag) {
        return favoriteService.cancelFavorite(Uid, Rid, flag);
    }

    @GetMapping("/list")
    public List<?> listFavorites(@RequestParam int Uid,@RequestParam int flag) {
        return favoriteService.getFavoritesByUidAndFlag(Uid, flag);
    }

    // 用于切换收藏状态（这里简化了参数接收，默认 Uid 为 1）
    @PostMapping("/toggle")
    public String toggleFavorite(@RequestParam int Uid,@RequestParam int Rid, @RequestParam int flag, @RequestParam int status) {
        return favoriteService.toggleFavorite(Uid, Rid, flag, status);
    }

    // 新增接口，用于查询某个资源是否被收藏
    @GetMapping("/check")
    public Map<String, Object> checkFavorite(@RequestParam int Uid, @RequestParam int Rid, @RequestParam int flag) {
        boolean isFavorited = favoriteService.checkFavorite(Uid, Rid, flag);
        Map<String, Object> result = new HashMap<>();
        result.put("isFavorited", isFavorited);
        return result;
    }

    @GetMapping("/resources")
    public List<Map<String, Object>> getFavoriteResources(@RequestParam int Uid, @RequestParam int flag) {
        return favoriteService.getFavoriteResourcesByUidAndFlag(Uid, flag);
    }
}