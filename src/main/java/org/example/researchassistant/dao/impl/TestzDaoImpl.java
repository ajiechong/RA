package org.example.researchassistant.dao.impl;

import org.example.researchassistant.dao.TestzDao;
import org.example.researchassistant.domain.Testz;
import org.example.researchassistant.utils.JDBCUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestzDaoImpl implements TestzDao {

    private JDBCUtils jdbcUtils = new JDBCUtils();

    @Override
    public List<Testz> getAllTests() {
        List<Testz> testList = new ArrayList<>();
        String sql = "SELECT Tid, Tag, title, description, img, url FROM test";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Testz test = new Testz();
                test.setTid(resultSet.getInt("Tid"));
                test.setTag(resultSet.getString("Tag"));
                test.setTitle(resultSet.getString("title"));
                test.setDescription(resultSet.getString("description"));
                test.setImg(resultSet.getString("img"));
                test.setUrl(resultSet.getString("url"));
                testList.add(test);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return testList;
    }

    @Override
    public Testz getTestById(int Tid) {
        Testz test = new Testz();
        String sql = "SELECT Tid, Tag, title, description, img, url FROM test WHERE Tid =?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Tid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    test.setTid(resultSet.getInt("Tid"));
                    test.setTag(resultSet.getString("Tag"));
                    test.setTitle(resultSet.getString("title"));
                    test.setDescription(resultSet.getString("description"));
                    test.setImg(resultSet.getString("img"));
                    test.setUrl(resultSet.getString("url"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return test;
    }

    @Override
    public List<Testz> getTestsByUid(int Uid) {
        List<Testz> testList = new ArrayList<>();
        String sql = "SELECT Tid, Tag, title, description, img, url FROM test WHERE Uid =?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Uid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Testz test = new Testz();
                    test.setTid(resultSet.getInt("Tid"));
                    test.setTag(resultSet.getString("Tag"));
                    test.setTitle(resultSet.getString("title"));
                    test.setDescription(resultSet.getString("description"));
                    test.setImg(resultSet.getString("img"));
                    test.setUrl(resultSet.getString("url"));
                    testList.add(test);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return testList;
    }

    @Override
    public List<Testz> getTestsByTag(String Tag) {
        List<Testz> testList = new ArrayList<>();
        String sql = "SELECT Tid, Tag, title, description, img, url FROM test WHERE Tag LIKE?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + Tag + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Testz test = new Testz();
                    test.setTid(resultSet.getInt("Tid"));
                    test.setTag(resultSet.getString("Tag"));
                    test.setTitle(resultSet.getString("title"));
                    test.setDescription(resultSet.getString("description"));
                    test.setImg(resultSet.getString("img"));
                    test.setUrl(resultSet.getString("url"));
                    testList.add(test);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return testList;
    }

    @Override
    public void insertTest(Testz test) {
        String sql = "INSERT INTO test (Tid, Tag, title, description, img, url) VALUES (?,?,?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, test.getTid());
            preparedStatement.setString(2, test.getTag());
            preparedStatement.setString(3, test.getTitle());
            preparedStatement.setString(4, test.getDescription());
            preparedStatement.setString(5, test.getImg());
            preparedStatement.setString(6, test.getUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTest(Testz test, String condition) {
        String sql = "UPDATE test SET " +
                "Tid =?, Tag =?, title =?, description =?, img =?, url =? " +
                " WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, test.getTid());
            preparedStatement.setString(2, test.getTag());
            preparedStatement.setString(3, test.getTitle());
            preparedStatement.setString(4, test.getDescription());
            preparedStatement.setString(5, test.getImg());
            preparedStatement.setString(6, test.getUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTest(String condition) {
        String sql = "DELETE FROM test WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}