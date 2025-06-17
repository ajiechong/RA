package org.example.researchassistant.dao.impl;

import org.example.researchassistant.dao.ChatHistoryDao;
import org.example.researchassistant.domain.ChatHistory;
import org.example.researchassistant.service.FileService;
import org.example.researchassistant.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;  // 添加这个导入

@Repository
public class ChatHistoryDaoImpl implements ChatHistoryDao {

    @Override
    public List<ChatHistory> getAllChatHistory() {
        List<ChatHistory> chatHistoryList = new ArrayList<>();
        String sql = "SELECT * FROM history ORDER BY Htime DESC";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                chatHistoryList.add(extractChatHistoryFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatHistoryList;
    }

    @Override
    public List<ChatHistory> getChatHistoryByCid(int cid) {
        List<ChatHistory> chatHistoryList = new ArrayList<>();
        String sql = "SELECT * FROM history WHERE Cid = ? ORDER BY Htime ASC";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, cid);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chatHistoryList.add(extractChatHistoryFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatHistoryList;
    }

    @Override
    public List<ChatHistory> getChatHistoryByUid(int uid) {
        List<ChatHistory> chatHistoryList = new ArrayList<>();
        String sql = "SELECT * FROM history WHERE Uid = ? ORDER BY Htime DESC";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chatHistoryList.add(extractChatHistoryFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatHistoryList;
    }

    @Override
    public void insertChatHistory(ChatHistory chatHistory) {
        String sql = "INSERT INTO history (Cid, Uid, Htime, Content, HName, Role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, chatHistory.getCid());
            preparedStatement.setInt(2, chatHistory.getUid());
            preparedStatement.setObject(3, chatHistory.getHtime());
            preparedStatement.setString(4, chatHistory.getContent());
            preparedStatement.setString(5, chatHistory.getHName());
            preparedStatement.setString(6, chatHistory.getRole());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateChatHistory(ChatHistory chatHistory, String condition) {
        String sql = "UPDATE history SET Cid = ?, Uid = ?, Htime = ?, " +
                "Content = ?, HName = ?, Role = ? WHERE " + condition;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, chatHistory.getCid());
            preparedStatement.setInt(2, chatHistory.getUid());
            preparedStatement.setObject(3, chatHistory.getHtime());
            preparedStatement.setString(4, chatHistory.getContent());
            preparedStatement.setString(5, chatHistory.getHName());
            preparedStatement.setString(6, chatHistory.getRole());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteChatHistory(String condition) {
        String sql = "DELETE FROM history WHERE " + condition;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChatHistory getLastMessageByCid(int cid) {
        String sql = "SELECT * FROM history WHERE Cid = ? ORDER BY Htime DESC LIMIT 1";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, cid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractChatHistoryFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<org.example.researchassistant.entity.ChatHistory> findFirst5ByCidAndUidOrderByHtimeDesc(int cid, int uid) {
        return null;
    }

    // 辅助方法：从ResultSet中提取ChatHistory对象
    private ChatHistory extractChatHistoryFromResultSet(ResultSet rs) throws SQLException {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setHid(rs.getInt("Hid"));
        chatHistory.setCid(rs.getInt("Cid"));
        chatHistory.setUid(rs.getInt("Uid"));
        chatHistory.setHtime(rs.getObject("Htime", LocalDateTime.class));
        chatHistory.setContent(rs.getString("Content"));
        chatHistory.setHName(rs.getString("HName"));
        chatHistory.setRole(rs.getString("Role"));
        return chatHistory;
    }
}
