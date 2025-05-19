package org.example.researchassistant.service;

import org.example.researchassistant.dao.DocInfoDao;
import org.example.researchassistant.dao.impl.DocInfoDaoImpl;
import org.example.researchassistant.domain.DocInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
@Service
public class DocInfoService {

    private DocInfoDao docInfoDao = new DocInfoDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    // 初始化ObjectMapper，设置序列化时的一些特性，比如日期格式等（可根据实际需求调整）
    public DocInfoService() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    // 获取所有医生信息，并以JSON报文格式返回
    public String getAllDocInfoAsJson() {
        List<DocInfo> docInfoList = docInfoDao.getAllDocInfo();
        try {
            return objectMapper.writeValueAsString(docInfoList);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"获取所有医生信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据条件获取医生信息，并以JSON报文格式返回
    public String getDocInfoByConditionAsJson(HttpServletRequest request) {
        try {
            String condition = request.getParameter("condition");

            List<DocInfo> docInfoList = docInfoDao.getDocInfoByCondition(condition);
            return objectMapper.writeValueAsString(docInfoList);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据条件获取医生信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 添加新的医生信息，从报文中获取医生信息并添加，返回添加结果的报文
    public String addDocInfo(HttpServletRequest request) {
        DocInfo docInfo = new DocInfo();
        docInfo.setDocName(request.getParameter("docName"));
        docInfo.setDocSpecialties(request.getParameter("docSpecialties"));
        docInfo.setDocProfile(request.getParameter("docProfile"));
        docInfo.setDocImage(request.getParameter("docImage"));
        docInfo.setDocHospital(request.getParameter("docHospital"));
        docInfo.setHosUrl(request.getParameter("hosUrl"));
        docInfo.setTitle(request.getParameter("title"));
        docInfo.setSpec_symptom(request.getParameter("spec_symptom"));
        docInfoDao.insertDocInfo(docInfo);
        return "{\"success\": true, \"message\": \"医生信息添加成功\"}";
    }

    // 更新医生信息，从报文中获取更新信息并更新，返回更新结果的报文
    public String updateDocInfo(HttpServletRequest request) {
        try {
            int docId = Integer.parseInt(request.getParameter("docId"));
            DocInfo originalDocInfo = getDocInfoById(docId);

            if (originalDocInfo!= null) {
                originalDocInfo.setDocName(request.getParameter("docName"));
                originalDocInfo.setDocSpecialties(request.getParameter("docSpecialties"));
                originalDocInfo.setDocProfile(request.getParameter("docProfile"));
                originalDocInfo.setDocImage(request.getParameter("docImage"));
                originalDocInfo.setDocHospital(request.getParameter("docHospital"));
                originalDocInfo.setHosUrl(request.getParameter("hosUrl"));
                originalDocInfo.setHosUrl(request.getParameter("title"));
                originalDocInfo.setSpec_symptom(request.getParameter("spec_symptom"));
                docInfoDao.updateDocInfo(originalDocInfo, "doc_id = " + docId);
                return "{\"success\": true, \"message\": \"医生信息更新成功\"}";
            } else {
                return "{\"success\": false, \"message\": \"未找到ID为 " + docId + " 的医生信息，无法更新\"}";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"更新医生信息时出错\"}";
        }
    }

    // 根据医生ID获取特定医生信息
    public DocInfo getDocInfoById(int docId) {
        List<DocInfo> docInfoList = docInfoDao.getDocInfoByCondition("doc_id = " + docId);
        if (!docInfoList.isEmpty()) {
            return docInfoList.get(0);
        }
        return null;
    }

    // 删除医生信息，从报文中获取医生信息ID并删除，返回删除结果的报文
    public String deleteDocInfo(HttpServletRequest request) {
        try {
            int docId = Integer.parseInt(request.getParameter("docId"));

            docInfoDao.deleteDocInfo("doc_id = " + docId);
            return "{\"success\": true, \"message\": \"医生信息删除成功\"}";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"删除医生信息时出错\"}";
        }
    }
}