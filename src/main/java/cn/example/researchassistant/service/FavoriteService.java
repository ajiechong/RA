package cn.example.researchassistant.service;

import cn.example.researchassistant.dao.FavoriteDao;
import cn.example.researchassistant.dao.ResourceDao;
import cn.example.researchassistant.dao.ResourceVideoDao;
import cn.example.researchassistant.dao.impl.FavoriteDaoImpl;
import cn.example.researchassistant.dao.impl.ResourceDaoImpl;
import cn.example.researchassistant.dao.impl.ResourceVideoDaoImpl;
import cn.example.researchassistant.domain.Favorite;
import cn.example.researchassistant.domain.Resource;
import cn.example.researchassistant.domain.ResourceVideo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    private ResourceDao resourceDao = new ResourceDaoImpl();
    private ResourceVideoDao resourceVideoDao = new ResourceVideoDaoImpl();



    public List<Map<String, Object>> getFavoriteResourcesByUidAndFlag(int Uid, int flag) {
        List<Favorite> favorites = favoriteDao.getFavoritesByUidAndFlag(Uid, flag);
        List<Map<String, Object>> favoriteResources = new ArrayList<>();

        for (Favorite favorite : favorites) {
            Map<String, Object> resourceMap = new HashMap<>();
            if (flag == 0) {
                Resource resource = resourceDao.getResourceById(favorite.getRid());
                resourceMap.put("type", "article");
                resourceMap.put("resource", resource);
            } else if (flag == 1) {
                ResourceVideo resourceVideo = resourceVideoDao.getResourceVideoById(favorite.getRid());
                resourceMap.put("type", "video");
                resourceMap.put("resource", resourceVideo);
            }
            favoriteResources.add(resourceMap);
        }
        return favoriteResources;
    }

    public Map<String, Object> addFavorite(int Uid, int Rid, int flag) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            // 检查该资源在当前指定类型下是否已被收藏（区别于之前检查所有类型）
            boolean alreadyFavoritedInThisType = checkFavorite(Uid, Rid, flag);
            if (alreadyFavoritedInThisType) {
                // 如果在当前类型下已收藏，提示相应信息，不进行插入操作
                resultMap.put("success", false);
                resultMap.put("message", "该资源已在当前类型下被收藏过");
                return resultMap;
            }

            Favorite favorite = new Favorite(Uid, Rid, flag);
            favoriteDao.insertFavorite(favorite);
            resultMap.put("success", true);
            resultMap.put("message", "收藏添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("message", "添加收藏时出错");
        }
        return resultMap;
    }


    public Map<String, Object> cancelFavorite(int Uid, int Rid, int flag) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            favoriteDao.deleteFavorite(Uid, Rid, flag);
            resultMap.put("success", true);
            resultMap.put("message", "取消收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("message", "取消收藏时出错");
        }
        return resultMap;
    }

    public String toggleFavorite(int Uid, int Rid, int flag, int status) {
        if (status == 1) {
            return addFavorite(Uid, Rid, flag).toString();
        } else {
            return cancelFavorite(Uid, Rid, flag).toString();
        }
    }


    public boolean checkFavorite(int Uid, int Rid, int flag) {
        List<Favorite> favorites = favoriteDao.getFavoritesByUidAndFlag(Uid, flag);
        for (Favorite favorite : favorites) {
            if (favorite.getRid() == Rid && favorite.getFlag() == flag && favorite.getUid() == Uid) {  // 增加对 flag 的判断
                return true;
            }
        }
        return false;
    }

    public List<Favorite> getFavoritesByUidAndFlag(int Uid, int flag) {
        return favoriteDao.getFavoritesByUidAndFlag(Uid, flag);
    }
}