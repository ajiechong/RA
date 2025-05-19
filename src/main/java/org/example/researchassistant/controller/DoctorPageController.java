package org.example.researchassistant.controller;

import org.example.researchassistant.service.DocInfoService;
import org.example.researchassistant.domain.DocInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doc_information")
public class DoctorPageController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorPageController.class);

    // 自动注入 DocInfoService
    @Autowired
    private DocInfoService docInfoService;

    // 获取所有医生信息
    @GetMapping("/all")
    public List<DocInfo> getAllDoctors() {
        logger.info("进入获取所有医生信息的请求处理方法...");
        try {
            // 调用 DocInfoService 获取所有医生信息并返回
            logger.debug("调用 DocInfoService 获取所有医生信息...");
            String responseJson = docInfoService.getAllDocInfoAsJson();
            logger.debug("获取的 JSON 数据: {}", responseJson);

            // 使用 ObjectMapper 将 JSON 字符串解析为 List<DocInfo>
            ObjectMapper objectMapper = new ObjectMapper();
            List<DocInfo> doctors = objectMapper.readValue(responseJson, objectMapper.getTypeFactory().constructCollectionType(List.class, DocInfo.class));

            // 打印每个医生的详细信息
            for (DocInfo doctor : doctors) {
                logger.info("医生信息 - ID: {}, 姓名: {}, 专业: {}, 头像: {}, 医院: {}, 个人简介: {}, 医院链接: {}",
                        doctor.getDocId(),            // docId
                        doctor.getDocName(),          // docName
                        doctor.getDocSpecialties(),   // docSpecialties
                        doctor.getDocImage(),         // docImage
                        doctor.getDocHospital(),      // docHospital
                        doctor.getDocProfile(),       // docProfile
                        doctor.getHosUrl());          // hosUrl
            }

            logger.info("成功获取到 {} 名医生的信息", doctors.size());
            return doctors;
        } catch (Exception e) {
            logger.error("获取医生信息时出错", e);
            return null;  // 你也可以返回适当的错误信息，或者返回一个特定的错误消息
        }
    }

    // 根据医生ID获取医生信息
    @GetMapping("/{id}")
    public DocInfo getDoctorById(@PathVariable("id") int id) {
        logger.info("进入获取指定医生信息的请求处理方法，医生ID：{}", id);
        try {
            // 调用 DocInfoService 获取指定医生信息并返回
            logger.debug("调用 DocInfoService 获取医生ID为 {} 的信息...", id);
            DocInfo docInfo = docInfoService.getDocInfoById(id);

            // 打印医生的详细信息
            logger.info("医生信息 - ID: {}, 姓名: {}, 专业: {}, 头像: {}, 医院: {}, 个人简介: {}, 医院链接: {}",
                    docInfo.getDocId(),          // docId
                    docInfo.getDocName(),        // docName
                    docInfo.getDocSpecialties(), // docSpecialties
                    docInfo.getDocImage(),       // docImage
                    docInfo.getDocHospital(),    // docHospital
                    docInfo.getDocProfile(),     // docProfile
                    docInfo.getspec_symptom(),
                    docInfo.getTitle(),
                    docInfo.getHosUrl());        // hosUrl


            logger.info("成功获取医生ID为 {} 的信息", id);
            return docInfo;
        } catch (Exception e) {
            logger.error("获取医生ID为 {} 的信息时出错", id, e);
            return null;  // 你也可以返回适当的错误信息，或者返回一个特定的错误消息
        }
    }
}
