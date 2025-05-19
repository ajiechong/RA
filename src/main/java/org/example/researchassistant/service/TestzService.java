package org.example.researchassistant.service;

import org.example.researchassistant.dao.TestzDao;
import org.example.researchassistant.dao.impl.TestzDaoImpl;
import org.example.researchassistant.domain.Testz;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Service
public class TestzService {

    private TestzDao testzDao = new TestzDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    // 初始化ObjectMapper，设置序列化时的一些特性，比如日期格式等（可根据实际需求调整）
    public TestzService() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    // 获取所有测试信息，并以JSON报文格式返回
    public String getAllTestsAsJson() {
        List<Testz> tests = testzDao.getAllTests();
        try {
            return objectMapper.writeValueAsString(tests);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"获取所有测试信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据测试ID获取特定测试信息，并以JSON报文格式返回
    public String getTestByIdAsJson(int Tid) {
        Testz test = testzDao.getTestById(Tid);
        try {
            if (test!= null) {
                return objectMapper.writeValueAsString(test);
            } else {
                return "{\"error\": \"未找到ID为 " + Tid + " 的测试信息\", \"details\": \"请检查输入的ID是否正确\"}";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据ID获取测试信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据用户ID获取该用户的所有测试信息，并以JSON报文格式返回
    public String getTestsByUidAsJson(int Uid) {
        List<Testz> tests = testzDao.getTestsByUid(Uid);
        try {
            return objectMapper.writeValueAsString(tests);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据用户ID获取测试信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据标签获取相关测试信息，并以JSON报文格式返回
    public String getTestsByTagAsJson(HttpServletRequest request) {
        try {
            String Tag = request.getParameter("Tag");

            List<Testz> tests = testzDao.getTestsByTag(Tag);
            return objectMapper.writeValueAsString(tests);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据标签获取测试信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 添加新的测试信息，从报文中获取测试信息相关参数并添加，返回添加结果的报文
    public String addTest(HttpServletRequest request) {
        try {
            Testz test = new Testz();
            test.setTid(Integer.parseInt(request.getParameter("Tid")));
            test.setTag(request.getParameter("Tag"));
            test.setTitle(request.getParameter("title"));
            test.setDescription(request.getParameter("description"));
            test.setImg(request.getParameter("img"));
            test.setUrl(request.getParameter("url"));
            testzDao.insertTest(test);
            return "{\"success\": true, \"message\": \"测试信息添加成功\"}";
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"添加测试信息时出错\"}";
        }
    }

    // 更新测试信息，从报文中获取更新信息相关参数并更新，返回更新结果的报文
    public String updateTest(HttpServletRequest request) {
        try {
            int Tid = Integer.parseInt(request.getParameter("Tid"));
            Testz originalTest = testzDao.getTestById(Tid);

            if (originalTest!= null) {
                originalTest.setTid(Integer.parseInt(request.getParameter("Tid")));
                originalTest.setTag(request.getParameter("Tag"));
                originalTest.setTitle(request.getParameter("title"));
                originalTest.setDescription(request.getParameter("description"));
                originalTest.setImg(request.getParameter("img"));
                originalTest.setUrl(request.getParameter("url"));
                testzDao.updateTest(originalTest, "Tid = " + Tid);
                return "{\"success\": true, \"message\": \"测试信息更新成功\"}";
            } else {
                return "{\"success\": false, \"message\": \"未找到ID为 " + Tid + " 的测试信息，无法更新\"}";
            }
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"更新测试信息时出错\"}";
        }
    }

    // 删除测试信息，从报文中获取测试信息ID并删除，返回删除结果的报文
    public String deleteTest(HttpServletRequest request) {
        try {
            int Tid = Integer.parseInt(request.getParameter("Tid"));

            testzDao.deleteTest("Tid = " + Tid);
            return "{\"success\": true, \"message\": \"测试信息删除成功\"}";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"删除测试信息时出错\"}";
        }
    }

    // 添加新的方法，用于获取符合前端test组件需求格式的数据列表
    public List<Map<String, Object>> getTestsData() {
        List<Testz> tests = testzDao.getAllTests();
        List<Map<String, Object>> testDataList = new ArrayList<>();

        for (Testz test : tests) {
            Map<String, Object> testData = new HashMap<>();
            testData.put("id", test.getTid());
            testData.put("tag", test.getTag());
            testData.put("title", test.getTitle());
            testData.put("description", test.getDescription());
            testData.put("image", test.getImg());
            testData.put("url", test.getUrl());
            testDataList.add(testData);
        }

        return testDataList;
    }
}