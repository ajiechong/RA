package org.example.researchassistant.service;

import org.example.researchassistant.dao.QuizDao;
import org.example.researchassistant.dao.impl.QuizDaoImpl;
import org.example.researchassistant.domain.Quiz;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class QuizService {

    private QuizDao quizDao = new QuizDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    // 初始化ObjectMapper，设置序列化时的一些特性，比如日期格式等（可根据实际需求调整）
    public QuizService() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    // 获取所有测验记录，并以JSON报文格式返回
    public String getAllQuizzesAsJson() {
        List<Quiz> quizzes = quizDao.getAllQuizzes();
        try {
            return objectMapper.writeValueAsString(quizzes);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"获取所有测验记录时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据测验ID获取特定测验记录，并以JSON报文格式返回
    public String getQuizByIdAsJson(int qid) {
        Quiz quiz = quizDao.getQuizById(qid);
        try {
            if (quiz!= null) {
                return objectMapper.writeValueAsString(quiz);
            } else {
                return "{\"error\": \"未找到ID为 " + qid + " 的测验记录\", \"details\": \"请检查输入的测验ID是否正确\"}";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据ID获取测验记录时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据测验名称获取相关测验记录，并以JSON报文格式返回
    public String getQuizzesByNameAsJson(HttpServletRequest request) {
        try {
            String qName = request.getParameter("qName");

            List<Quiz> quizzes = quizDao.getQuizzesByName(qName);
            return objectMapper.writeValueAsString(quizzes);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            return "{\"error\": \"根据测验名称获取测验记录时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 根据测验标签获取相关测验记录，并以JSON报文格式返回
    public String getQuizzesByTagAsJson(HttpServletRequest request) {
        try {
            String qtag = request.getParameter("qtag");

            List<Quiz> quizzes = quizDao.getQuizzesByTag(qtag);
            return objectMapper.writeValueAsString(quizzes);
        } catch (NumberFormatException | IOException | SQLException e) {
            e.printStackTrace();
            return "{\"error\": \"根据测验标签获取测验记录时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 添加新的测验记录，从报文中获取测验信息并添加，返回添加结果的报文
    public String addQuiz(HttpServletRequest request) {
        try {
            Quiz quiz = new Quiz();
            quiz.setQid(Integer.parseInt(request.getParameter("qid")));
            quiz.setQName(request.getParameter("qName"));
            quiz.setQtag(request.getParameter("qtag"));
            quizDao.insertQuiz(quiz);
            return "{\"success\": true, \"message\": \"测验记录添加成功\"}";
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"添加测验记录时出错\"}";
        }
    }

    // 更新测验记录，从报文中获取更新信息并更新，返回更新结果的报文
    public String updateQuiz(HttpServletRequest request) {
        try {
            int qid = Integer.parseInt(request.getParameter("qid"));
            Quiz originalQuiz = quizDao.getQuizById(qid);

            if (originalQuiz!= null) {
                originalQuiz.setQName(request.getParameter("qName"));
                originalQuiz.setQtag(request.getParameter("qtag"));

                quizDao.updateQuiz(originalQuiz, "Qid = " + qid);
                return "{\"success\": true, \"message\": \"测验记录更新成功\"}";
            } else {
                return "{\"success\": false, \"message\": \"未找到ID为 " + qid + " 的测验记录，无法更新\"}";
            }
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"更新测验记录时出错\"}";
        }
    }

    // 删除测验记录，从报文中获取测验ID并删除，返回删除结果的报文
    public String deleteQuiz(HttpServletRequest request) {
        try {
            int qid = Integer.parseInt(request.getParameter("qid"));

            quizDao.deleteQuiz("Qid = " + qid);
            return "{\"success\": true, \"message\": \"测验记录删除成功\"}";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"删除测验记录时出错\"}";
        }
    }
}