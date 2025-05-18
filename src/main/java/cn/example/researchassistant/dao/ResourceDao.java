package cn.example.researchassistant.dao;

import cn.example.researchassistant.domain.Resource;

import java.util.List;

public interface ResourceDao {
    // 获取所有资源记录
    List<Resource> getAllResources();

    // 根据资源ID获取特定资源记录
    Resource getResourceById(int rid);

    // 根据资源名称获取相关资源记录
    List<Resource> getResourcesByName(String rname);

    // 根据标签获取相关资源记录
    List<Resource> getResourcesByTag(String rtag);

    // 插入新的资源记录
    void insertResource(Resource resource);

    // 更新资源记录
    void updateResource(Resource resource, String condition);

    // 删除资源记录
    void deleteResource(String condition);
}