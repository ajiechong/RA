package org.example.researchassistant.dao.impl;

import org.example.researchassistant.dao.IntroductionDao;
import org.example.researchassistant.domain.Introduction;
import org.example.researchassistant.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IntroductionDaoImpl implements IntroductionDao {

    private JDBCUtils jdbcUtils = new JDBCUtils();

    @Override
    public List<Introduction> getAllIntroductions() {
        List<Introduction> introductionList = new ArrayList<>();
        String sql = "SELECT * FROM introduction";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Introduction introduction = new Introduction();
                introduction.setIid(resultSet.getInt("Iid"));
                introduction.setType(resultSet.getString("Type"));
                introduction.setSymptom(resultSet.getString("Symptom"));
                introduction.setCauses(resultSet.getString("Causes"));
                introduction.setTreatment(resultSet.getString("Treatment"));
                introduction.setPicture(resultSet.getString("Picture"));
                introductionList.add(introduction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return introductionList;
    }

    @Override
    public Introduction getIntroductionById(int iid) {
        Introduction introduction = new Introduction();
        String sql = "SELECT * FROM introduction WHERE Iid =?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, iid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    introduction.setIid(resultSet.getInt("Iid"));
                    introduction.setType(resultSet.getString("Type"));
                    introduction.setSymptom(resultSet.getString("Symptom"));
                    introduction.setCauses(resultSet.getString("Causes"));
                    introduction.setTreatment(resultSet.getString("Treatment"));
                    introduction.setPicture(resultSet.getString("Picture"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return introduction;
    }

    @Override
    public List<Introduction> getIntroductionsByType(String type) {
        List<Introduction> introductionList = new ArrayList<>();
        String sql = "SELECT * FROM introduction WHERE Type =?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, type);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Introduction introduction = new Introduction();
                    introduction.setIid(resultSet.getInt("Iid"));
                    introduction.setType(resultSet.getString("Type"));
                    introduction.setSymptom(resultSet.getString("Symptom"));
                    introduction.setCauses(resultSet.getString("Causes"));
                    introduction.setTreatment(resultSet.getString("Treatment"));
                    introduction.setPicture(resultSet.getString("Picture"));
                    introductionList.add(introduction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return introductionList;
    }

    @Override
    public List<Introduction> searchIntroductionsBySymptom(String symptomName) {
        List<Introduction> introductionList = new ArrayList<>();
        // 使用 LIKE 进行模糊查询
        String sql = "SELECT * FROM introduction WHERE Type LIKE ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // 模糊查询：在查询参数中使用 % 进行模糊匹配
            preparedStatement.setString(1, "%" + symptomName + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Introduction introduction = new Introduction();
                    introduction.setIid(resultSet.getInt("Iid"));
                    introduction.setType(resultSet.getString("Type"));
                    introduction.setSymptom(resultSet.getString("Symptom"));
                    introduction.setCauses(resultSet.getString("Causes"));
                    introduction.setTreatment(resultSet.getString("Treatment"));
                    introduction.setPicture(resultSet.getString("Picture"));
                    introductionList.add(introduction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return introductionList;
    }

    @Override
    public void insertIntroduction(Introduction introduction) {
        String sql = "INSERT INTO introduction (Type, Symptom, Causes, Treatment, Picture) VALUES (?,?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, introduction.getType());
            preparedStatement.setString(2, introduction.getSymptom());
            preparedStatement.setString(3, introduction.getCauses());
            preparedStatement.setString(4, introduction.getTreatment());
            preparedStatement.setString(5, introduction.getPicture());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateIntroduction(Introduction introduction, String condition) {
        String sql = "UPDATE introduction SET " +
                "Type =?, Symptom =?, Causes =?, Treatment =?, Picture =? " +
                " WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, introduction.getType());
            preparedStatement.setString(2, introduction.getSymptom());
            preparedStatement.setString(3, introduction.getCauses());
            preparedStatement.setString(4, introduction.getTreatment());
            preparedStatement.setString(5, introduction.getPicture());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteIntroduction(String condition) {
        String sql = "DELETE FROM introduction WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}