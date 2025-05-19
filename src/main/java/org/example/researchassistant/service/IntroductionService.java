package org.example.researchassistant.service;

import org.example.researchassistant.dao.IntroductionDao;
import org.example.researchassistant.dao.impl.IntroductionDaoImpl;
import org.example.researchassistant.domain.Introduction;
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
public class IntroductionService {

    private IntroductionDao introductionDao = new IntroductionDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    // 初始化ObjectMapper，设置序列化时的一些特性，比如日期格式等（可根据实际需求调整）
    public IntroductionService() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
    // 根据ID获取特定介绍信息，并以JSON报文格式返回
    public Introduction getIntroductionById(int iid) {
        return introductionDao.getIntroductionById(iid);
    }

    // 获取所有介绍信息，并以JSON报文格式返回
    public String getAllIntroductionsAsJson() {
        List<Introduction> introductions = introductionDao.getAllIntroductions();
        try {
            return objectMapper.writeValueAsString(introductions);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"获取所有介绍信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据ID获取特定介绍信息，并以JSON报文格式返回
    public String getIntroductionByIdAsJson(int iid) {
        Introduction introduction = introductionDao.getIntroductionById(iid);
        try {
            if (introduction!= null) {
                return objectMapper.writeValueAsString(introduction);
            } else {
                return "{\"error\": \"未找到ID为 " + iid + " 的介绍信息\", \"details\": \"请检查输入的ID是否正确\"}";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据ID获取介绍信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据症状类型获取相关介绍信息，并以JSON报文格式返回
    public String getIntroductionsByTypeAsJson(HttpServletRequest request) {
        try {
            String type = request.getParameter("type");

            List<Introduction> introductions = introductionDao.getIntroductionsByType(type);
            return objectMapper.writeValueAsString(introductions);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据症状类型获取介绍信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据症状名称模糊查询
    public List<Map<String, Object>> searchSymptomsByName(String symptomName) {
        List<Introduction> introductions = introductionDao.searchIntroductionsBySymptom(symptomName);

        // 将查询结果转换为符合前端需求的格式
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Introduction introduction : introductions) {
            Map<String, Object> articleData = new HashMap<>();
            articleData.put("id", introduction.getIid());
            articleData.put("title", introduction.getType());
            articleData.put("description", introduction.getSymptom());
            articleData.put("image", introduction.getPicture());
            resultList.add(articleData);
        }

        return resultList;
    }

    // 添加新的介绍信息，从报文中获取介绍信息相关参数并添加，返回添加结果的报文
    public String addIntroduction(HttpServletRequest request) {
        try {
            Introduction introduction = new Introduction();
            introduction.setIid(Integer.parseInt(request.getParameter("iid")));
            introduction.setType(request.getParameter("type"));
            introduction.setSymptom(request.getParameter("symptom"));
            introduction.setCauses(request.getParameter("causes"));
            introduction.setTreatment(request.getParameter("treatment"));
            introduction.setPicture(request.getParameter("picture"));
            introductionDao.insertIntroduction(introduction);
            return "{\"success\": true, \"message\": \"介绍信息添加成功\"}";
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"添加介绍信息时出错\"}";
        }
    }

    // 更新介绍信息，从报文中获取更新信息相关参数并更新，返回更新结果的报文
    public String updateIntroduction(HttpServletRequest request) {
        try {
            int iid = Integer.parseInt(request.getParameter("iid"));
            Introduction originalIntroduction = introductionDao.getIntroductionById(iid);

            if (originalIntroduction!= null) {
                originalIntroduction.setIid(Integer.parseInt(request.getParameter("iid")));
                originalIntroduction.setType(request.getParameter("type"));
                originalIntroduction.setSymptom(request.getParameter("symptom"));
                originalIntroduction.setCauses(request.getParameter("causes"));
                originalIntroduction.setTreatment(request.getParameter("treatment"));
                originalIntroduction.setPicture(request.getParameter("picture"));
                introductionDao.updateIntroduction(originalIntroduction, "Iid = " + iid);
                return "{\"success\": true, \"message\": \"介绍信息更新成功\"}";
            } else {
                return "{\"success\": false, \"message\": \"未找到ID为 " + iid + " 的介绍信息，无法更新\"}";
            }
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"更新介绍信息时出错\"}";
        }
    }

    // 删除介绍信息，从报文中获取介绍信息ID并删除，返回删除结果的报文
    public String deleteIntroduction(HttpServletRequest request) {
        try {
            int iid = Integer.parseInt(request.getParameter("iid"));

            introductionDao.deleteIntroduction("Iid = " + iid);
            return "{\"success\": true, \"message\": \"介绍信息删除成功\"}";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"删除介绍信息时出错\"}";
        }
    }

    // 添加新的方法，用于获取符合前端article组件需求格式的数据列表
    public List<Map<String, Object>> getArticlesData() {
        List<Introduction> introductions = introductionDao.getAllIntroductions();
        //List<Introduction> introductions = (List<Introduction>) introductionDao.getIntroductionById(1);
        List<Map<String, Object>> articleDataList = new ArrayList<>();

        for (Introduction introduction : introductions) {
            Map<String, Object> articleData = new HashMap<>();
            articleData.put("id", introduction.getIid());
            System.out.println(introduction.getIid());
            articleData.put("title", introduction.getType());
            System.out.println(introduction.getType());
            articleData.put("description", introduction.getSymptom());
            System.out.println(introduction.getSymptom());
            articleData.put("image", introduction.getPicture());
            System.out.println(introduction.getPicture());
            articleDataList.add(articleData);
        }

        return articleDataList;
    }
}