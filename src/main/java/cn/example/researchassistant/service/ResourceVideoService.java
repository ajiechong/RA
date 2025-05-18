package cn.example.researchassistant.service;

import cn.example.researchassistant.controller.ResourceController;
import cn.example.researchassistant.dao.ResourceVideoDao;
import cn.example.researchassistant.dao.impl.ResourceVideoDaoImpl;
import cn.example.researchassistant.domain.ResourceVideo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Service
public class ResourceVideoService {

    private ResourceVideoDao resourceVideoDao = new ResourceVideoDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    // 初始化ObjectMapper，设置序列化时的一些特性，比如日期格式等（可根据实际需求调整）
    public ResourceVideoService() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    // 获取所有资源视频记录，并以JSON报文格式返回
    public String getAllResourceVideosAsJson() {
        List<ResourceVideo> resourceVideos = resourceVideoDao.getAllResourceVideos();
        try {
            return objectMapper.writeValueAsString(resourceVideos);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"获取所有资源视频记录时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据资源视频ID获取特定资源视频记录，并以JSON报文格式返回
    public String getResourceVideoByIdAsJson(int rid) {
        ResourceVideo resourceVideo = resourceVideoDao.getResourceVideoById(rid);
        try {
            if (resourceVideo!= null) {
                return objectMapper.writeValueAsString(resourceVideo);
            } else {
                return "{\"error\": \"未找到ID为 " + rid + " 的资源视频记录\", \"details\": \"请检查输入的资源视频ID是否正确\"}";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据ID获取资源视频记录时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据资源视频名称获取相关资源视频记录，并以JSON报文格式返回
    public String getResourceVideosByNameAsJson(HttpServletRequest request) {
        try {
            String rname = request.getParameter("rname");

            List<ResourceVideo> resourceVideos = resourceVideoDao.getResourceVideosByName(rname);
            return objectMapper.writeValueAsString(resourceVideos);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据资源视频名称获取资源视频记录时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据资源视频标签获取相关资源视频记录，并以JSON报文格式返回
    public String getResourceVideosByTagAsJson(HttpServletRequest request) {
        try {
            String rtag = request.getParameter("name");
            logger.info("在video中传入的标签: {}", rtag);
            List<ResourceVideo> resourceVideos = resourceVideoDao.getResourceVideosByTag(rtag);
            return objectMapper.writeValueAsString(resourceVideos);
        } catch (NumberFormatException | IOException e) {
            return "{\"error\": \"根据标签获取资源视频记录时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 添加新的资源视频记录，从报文中获取资源视频信息并添加，返回添加结果的报文
    public String addResourceVideo(HttpServletRequest request) {
        try {
            ResourceVideo resourceVideo = new ResourceVideo();
            resourceVideo.setRid(Integer.parseInt(request.getParameter("rid")));
            resourceVideo.setRname(request.getParameter("rname"));
            resourceVideo.setRtag(request.getParameter("rtag"));
            resourceVideo.setRurl(request.getParameter("videoUrl"));
            resourceVideo.setRabstract(request.getParameter("abstract"));
            resourceVideo.setRTime(/* 根据前端传入的时间格式进行解析并设置，这里假设前端传入的是符合ISO 8601格式的字符串，你可根据实际情况调整 */
                    String.valueOf(java.time.LocalDateTime.parse(request.getParameter("createTime"))));

            resourceVideoDao.insertResourceVideo(resourceVideo);
            return "{\"success\": true, \"message\": \"资源视频记录添加成功\"}";
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"添加资源视频记录时出错\"}";
        }
    }

    // 更新资源视频记录，从报文中获取更新信息并更新，返回更新结果的报文
    public String updateResourceVideo(HttpServletRequest request) {
        try {
            int rid = Integer.parseInt(request.getParameter("rid"));
            ResourceVideo originalResourceVideo = resourceVideoDao.getResourceVideoById(rid);

            if (originalResourceVideo!= null) {
                originalResourceVideo.setRname(request.getParameter("rname"));
                originalResourceVideo.setRtag(request.getParameter("rtag"));
                originalResourceVideo.setRurl(request.getParameter("videoUrl"));
                originalResourceVideo.setRabstract(request.getParameter("abstract"));
                originalResourceVideo.setRTime(/* 同样根据前端传入的时间格式进行解析并设置 */
                        String.valueOf(java.time.LocalDateTime.parse(request.getParameter("createTime"))));

                resourceVideoDao.updateResourceVideo(originalResourceVideo, "Rid = " + rid);
                return "{\"success\": true, \"message\": \"资源视频记录更新成功\"}";
            } else {
            }
            return "{\"success\": false, \"message\": \"未找到ID为 " + rid + " 的资源视频记录，无法更新\"}";
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"更新资源视频记录时出错\"}";
        }
    }

    // 删除资源视频记录，从报文中获取资源视频ID并删除，返回删除结果的报文
    public String deleteResourceVideo(HttpServletRequest request) {
        try {
            int rid = Integer.parseInt(request.getParameter("rid"));

            resourceVideoDao.deleteResourceVideo("Rid = " + rid);
            return "{\"success\": true, \"message\": \"资源视频记录删除成功\"}";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"删除资源视频记录时出错\"}";
        }
    }
}