package cn.example.researchassistant.dao;

import cn.example.researchassistant.domain.ResourceVideo;

import java.util.List;

public interface ResourceVideoDao {
    // 获取所有资源视频记录
    List<ResourceVideo> getAllResourceVideos();

    // 根据资源视频ID获取特定资源视频记录
    ResourceVideo getResourceVideoById(int rid);

    // 根据资源视频名称获取相关资源视频记录
    List<ResourceVideo> getResourceVideosByName(String rname);

    // 根据资源视频标签获取相关资源视频记录
    List<ResourceVideo> getResourceVideosByTag(String rtag);

    // 插入新的资源视频记录
    void insertResourceVideo(ResourceVideo resourceVideo);

    // 更新资源视频记录
    void updateResourceVideo(ResourceVideo resourceVideo, String condition);

    // 删除资源视频记录
    void deleteResourceVideo(String condition);
}
