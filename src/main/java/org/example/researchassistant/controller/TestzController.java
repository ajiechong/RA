package org.example.researchassistant.controller;

import org.example.researchassistant.service.TestzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestzController {

    @Autowired
    private TestzService testzService;
    private static final Logger logger = LoggerFactory.getLogger(TestzController.class);

    // 获取所有测试记录数据（返回JSON格式）
    @GetMapping("/all")
    public String getAllTests() {
        logger.info("开始获取所有测试数据");
        String result = testzService.getAllTestsAsJson();
        logger.info("获取到的所有测试数据: {}", result);
        return result;
    }

    // 根据ID获取单个测试记录（返回JSON格式）
    @GetMapping("/{id}")
    public String getTestById(@PathVariable("id") int Tid) {
        logger.info("开始获取ID为 {} 的测试记录数据", Tid);
        return testzService.getTestByIdAsJson(Tid);
    }

    // 根据用户ID获取该用户的所有测试记录（返回JSON格式）
    @GetMapping("/by-uid")
    public String getTestsByUid(@RequestParam("uid") int Uid) {
        logger.info("开始根据用户ID {} 获取测试记录数据", Uid);
        return testzService.getTestsByUidAsJson(Uid);
    }

    // 根据标签获取相关测试记录（返回JSON格式）
    @GetMapping("/by-tag")
    public String getTestsByTag(HttpServletRequest request) {
        logger.info("开始根据标签获取测试记录数据");
        return testzService.getTestsByTagAsJson(request);
    }

    // 添加新的测试记录
    @PostMapping("/")
    public String addTest(HttpServletRequest request) {
        logger.info("开始添加新的测试记录");
        return testzService.addTest(request);
    }

    // 更新测试记录
    @PutMapping("/{id}")
    public String updateTest(@PathVariable("id") int Tid, HttpServletRequest request) {
        logger.info("开始更新ID为 {} 的测试记录", Tid);
        request.setAttribute("Tid", Tid);
        return testzService.updateTest(request);
    }

    // 删除测试记录
    @DeleteMapping("/{id}")
    public String deleteTest(@PathVariable("id") int Tid, HttpServletRequest request) {
        logger.info("开始删除ID为 {} 的测试记录", Tid);
        request.setAttribute("Tid", Tid);
        return testzService.deleteTest(request);
    }

    // 获取符合前端组件需求格式的数据列表
    @GetMapping("/data")
    public List<Map<String, Object>> getTestsData() {
        logger.info("开始获取符合前端格式的测试数据");
        return testzService.getTestsData();
    }
}