package org.example.researchassistant.dao.impl;

import org.example.researchassistant.dao.DocInfoDao;
import org.example.researchassistant.domain.DocInfo;
import org.example.researchassistant.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocInfoDaoImpl implements DocInfoDao {

    //通过JDBC获取数据源和连接
    private JDBCUtils jdbcUtils = new JDBCUtils();

    @Override
    public List<DocInfo> getAllDocInfo() {
        List<DocInfo> docInfoList = new ArrayList<>();
        String sql = "SELECT * FROM doc_information";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                DocInfo docInfo = new DocInfo();
                docInfo.setDocId(resultSet.getInt("doc_id"));
                docInfo.setDocName(resultSet.getString("doc_name"));
                docInfo.setDocSpecialties(resultSet.getString("doc_specialties"));
                docInfo.setDocProfile(resultSet.getString("doc_profile"));
                docInfo.setDocImage(resultSet.getString("doc_image"));
                docInfo.setDocHospital(resultSet.getString("doc_hospital"));
                docInfo.setHosUrl(resultSet.getString("hos_url"));
                docInfo.setTitle(resultSet.getString("title"));
                docInfo.setSpec_symptom(resultSet.getString("spec_symptom"));
                docInfoList.add(docInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docInfoList;
    }

    @Override
    public List<DocInfo> getDocInfoByCondition(String condition) {
        List<DocInfo> docInfoList = new ArrayList<>();
        String sql = "SELECT * FROM doc_information";
        if (condition!= null &&!condition.isEmpty()) {
            sql += " WHERE " + condition;
        }
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                DocInfo docInfo = new DocInfo();
                docInfo.setDocId(resultSet.getInt("doc_id"));
                docInfo.setDocName(resultSet.getString("doc_name"));
                docInfo.setDocSpecialties(resultSet.getString("doc_specialties"));
                docInfo.setDocProfile(resultSet.getString("doc_profile"));
                docInfo.setDocImage(resultSet.getString("doc_image"));
                docInfo.setDocHospital(resultSet.getString("doc_hospital"));
                docInfo.setHosUrl(resultSet.getString("hos_url"));
                docInfoList.add(docInfo);
                docInfo.setTitle(resultSet.getString("title"));
                docInfo.setSpec_symptom(resultSet.getString("spec_symptom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docInfoList;
    }

    @Override
    public void insertDocInfo(DocInfo docInfo) {
        String sql = "INSERT INTO doc_information (doc_name, doc_specialties, doc_profile, " +
                "doc_image, doc_hospital, hos_url) VALUES (?,?,?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, docInfo.getDocName());
            preparedStatement.setString(2, docInfo.getDocSpecialties());
            preparedStatement.setString(3, docInfo.getDocProfile());
            preparedStatement.setString(4, docInfo.getDocImage());
            preparedStatement.setString(5, docInfo.getDocHospital());
            preparedStatement.setString(6, docInfo.getHosUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDocInfo(DocInfo docInfo, String condition) {
        String sql = "UPDATE doc_information SET " +
                "doc_name =?, doc_specialties =?, doc_profile =?, " +
                "doc_image =?, doc_hospital =?, hos_url =? " +
                " WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, docInfo.getDocName());
            preparedStatement.setString(2, docInfo.getDocSpecialties());
            preparedStatement.setString(3, docInfo.getDocProfile());
            preparedStatement.setString(4, docInfo.getDocImage());
            preparedStatement.setString(5, docInfo.getDocHospital());
            preparedStatement.setString(6, docInfo.getHosUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDocInfo(String condition) {
        String sql = "DELETE FROM doc_information WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
