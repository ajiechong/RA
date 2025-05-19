package org.example.researchassistant.dao.impl;

import org.example.researchassistant.dao.QuizDao;
import org.example.researchassistant.domain.Quiz;
import org.example.researchassistant.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDaoImpl implements QuizDao {

    private JDBCUtils jdbcUtils = new JDBCUtils();

    @Override
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizList = new ArrayList<>();
        String sql = "SELECT * FROM quiz";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Quiz quiz = new Quiz();
                quiz.setQid(resultSet.getInt("Qid"));
                quiz.setQName(resultSet.getString("QName"));
                quiz.setQtag(resultSet.getString("Qtag"));
                quizList.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizList;
    }

    @Override
    public Quiz getQuizById(int qid) {
        Quiz quiz = new Quiz();
        String sql = "SELECT * FROM quiz WHERE Qid =?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, qid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    quiz.setQid(resultSet.getInt("Qid"));
                    quiz.setQName(resultSet.getString("QName"));
                    quiz.setQtag(resultSet.getString("Qtag"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quiz;
    }

    @Override
    public List<Quiz> getQuizzesByName(String qName) {
        List<Quiz> quizList = new ArrayList<>();
        String sql = "SELECT * FROM quiz WHERE QName LIKE '%' +? + '%'";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, qName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Quiz quiz = new Quiz();
                    quiz.setQid(resultSet.getInt("Qid"));
                    quiz.setQName(resultSet.getString("QName"));
                    quiz.setQtag(resultSet.getString("Qtag"));
                    quizList.add(quiz);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizList;
    }

    @Override
    public List<Quiz> getQuizzesByTag(String qtag) {
        List<Quiz> quizList = new ArrayList<>();
        String sql = "SELECT * FROM quiz WHERE Qtag LIKE '%' +? + '%'";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, qtag);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Quiz quiz = new Quiz();
                    quiz.setQid(resultSet.getInt("Qid"));
                    quiz.setQName(resultSet.getString("QName"));
                    quiz.setQtag(resultSet.getString("Qtag"));
                    quizList.add(quiz);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizList;
    }

    @Override
    public void insertQuiz(Quiz quiz) {
        String sql = "INSERT INTO quiz (QName, Qtag) VALUES (?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, quiz.getQName());
            preparedStatement.setString(2, quiz.getQtag());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuiz(Quiz quiz, String condition) {
        String sql = "UPDATE quiz SET QName =?, Qtag =? WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, quiz.getQName());
            preparedStatement.setString(2, quiz.getQtag());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteQuiz(String condition) {
        String sql = "DELETE FROM quiz WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}