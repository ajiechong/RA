package cn.example.researchassistant.controller;

import cn.example.researchassistant.service.ResourceVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ResourceVideoController {

    @Autowired
    private final ResourceVideoService resourceVideoService;
    private static final Logger logger = LoggerFactory.getLogger(ResourceVideoController.class);


    // 通过构造函数注入ResourceVideoService
    public ResourceVideoController(ResourceVideoService resourceVideoService) {
        this.resourceVideoService = resourceVideoService;
    }

    // 获取所有资源视频记录（返回 JSON 格式）
    @GetMapping("/resourceVideos/all")
    public String getAllResourceVideos() {
        logger.info("开始获取所有资源数据");
        String result = resourceVideoService.getAllResourceVideosAsJson();
        logger.info("获取到的所有资源数据: {}", result);
        return result;
    }

    // 根据ID获取单个资源视频（返回 JSON 格式）
    @GetMapping("/resourceVideos/{id}")
    public String getResourceVideoById(@PathVariable("id") int rid) {
        return resourceVideoService.getResourceVideoByIdAsJson(rid);
    }

    // 根据名称获取资源视频记录（返回 JSON 格式）
    @GetMapping("/resourceVideos/byName")
    public String getResourceVideosByName(HttpServletRequest request) {
        return resourceVideoService.getResourceVideosByNameAsJson(request);
    }

    // 根据标签获取资源视频记录（返回 JSON 格式）
    @GetMapping("/resourceVideos/byTag")
    public String getResourceVideosByTag(HttpServletRequest request) {
        return resourceVideoService.getResourceVideosByTagAsJson(request);
    }

    // 添加新的资源视频记录
    @PostMapping("/resourceVideos")
    public String addResourceVideo(HttpServletRequest request) {
        return resourceVideoService.addResourceVideo(request);
    }

    // 更新资源视频记录
    @PutMapping("/resourceVideos/{id}")
    public String updateResourceVideo(@PathVariable("id") int rid, HttpServletRequest request) {
        // 将请求中的ID传递给服务层
        request.setAttribute("rid", rid); // 将rid放到request中，以便Service层使用
        return resourceVideoService.updateResourceVideo(request);
    }

    // 删除资源视频记录
    @DeleteMapping("/resourceVideos/{id}")
    public String deleteResourceVideo(@PathVariable("id") int rid, HttpServletRequest request) {
        // 在这里你可以将请求参数手动传递给服务层
        // 你可以将 `rid` 通过参数注入到 `HttpServletRequest` 中
        request.setAttribute("rid", rid);
        return resourceVideoService.deleteResourceVideo(request);
    }
}
