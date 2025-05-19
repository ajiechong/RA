package org.example.researchassistant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.researchassistant.domain.Introduction;
import org.example.researchassistant.service.IntroductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

// 在类的开头定义日志对象
@RestController
@RequestMapping("/api/introductions")
public class IntroductionController {
    @Autowired
    private IntroductionService introductionService;

    // 添加新的方法，用于处理前端获取文章列表数据的请求
    @GetMapping("/articles")
    public List<Map<String, Object>> getArticlesData() {
        logger.info("开始获取文章列表数据");
        List<Map<String, Object>> data = introductionService.getArticlesData();
        logger.info("获取到的文章列表数据: {}", data);
        return data;
    }

    // 新增方法，用于处理前端获取单个文章详细信息的请求
    @GetMapping("/{id}")
    public Introduction getArticleById(@PathVariable("id") int id) {
        logger.info("开始获取ID为 {} 的文章详细信息", id);
        Introduction article = introductionService.getIntroductionById(id);
        logger.info("获取到的文章详细信息: {}", article);
        return article;
    }

    // 根据症状名称模糊搜索
    @GetMapping("/search")
    public List<Map<String, Object>> searchSymptoms(HttpServletRequest request) {
        String symptomName = request.getParameter("symptomName");
        logger.info("开始根据症状名称进行模糊查询: {}", symptomName);
        List<Map<String, Object>> data = introductionService.searchSymptomsByName(symptomName);
        logger.info("获取到的症状查询结果: {}", data);
        return data;
    }

    private static final Logger logger = LoggerFactory.getLogger(IntroductionController.class);
}