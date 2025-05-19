package org.example.researchassistant.dao;

import org.example.researchassistant.domain.Quiz;

import java.sql.SQLException;
import java.util.List;

public interface QuizDao {
    // 获取所有测验记录
    List<Quiz> getAllQuizzes();

    // 根据测验ID获取特定测验记录
    Quiz getQuizById(int qid);

    // 根据测验名称获取相关测验记录
    List<Quiz> getQuizzesByName(String qName);

    // 根据测验标签获取相关测验记录
    List<Quiz> getQuizzesByTag(String qtag) throws SQLException;

    // 插入新的测验记录
    void insertQuiz(Quiz quiz);

    // 更新测验记录
    void updateQuiz(Quiz quiz, String condition);

    // 删除测验记录
    void deleteQuiz(String condition);
}