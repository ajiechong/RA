package org.example.researchassistant.dao.impl;

import org.example.researchassistant.dao.FavoriteChatDao;
import org.example.researchassistant.domain.FavoriteChat;
import org.example.researchassistant.utils.JDBCUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FavoriteChatDaoImpl implements FavoriteChatDao {

    private JDBCUtils jdbcUtils = new JDBCUtils();

    @Override
    public int insertFavoriteChat(FavoriteChat favoriteChat) {
        String sql = "INSERT INTO favoriteChat (uid, cid, createTime) VALUES (?, ?, NOW())";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, favoriteChat.getUid());
            preparedStatement.setInt(2, favoriteChat.getCid());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加收藏时发生数据库错误: " + e.getMessage());
        }
    }

    @Override
    public int deleteFavoriteChat(Integer uid, Integer cid) {
        String sql = "DELETE FROM favoriteChat WHERE uid = ? AND cid = ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, cid);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("取消收藏时发生数据库错误: " + e.getMessage());
        }
    }

    @Override
    public List<FavoriteChat> getFavoriteChatsByUid(Integer uid) {
        List<FavoriteChat> favoriteChats = new ArrayList<>();
        // 修改查询：从 history 表获取最新的聊天名称
        String sql = """
        SELECT f.id, f.uid, f.cid, f.createTime, 
               h.HName as hname, h.Htime as htime
        FROM favoriteChat f 
        LEFT JOIN (
            SELECT Cid, HName, Htime,
                   ROW_NUMBER() OVER (PARTITION BY Cid ORDER BY Htime DESC) as rn
            FROM history 
            WHERE Role != 'system'
        ) h ON f.cid = h.Cid AND h.rn = 1
        WHERE f.uid = ? 
        ORDER BY f.createTime DESC
        """;

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, uid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    FavoriteChat favoriteChat = new FavoriteChat();
                    favoriteChat.setId(resultSet.getInt("id"));
                    favoriteChat.setUid(resultSet.getInt("uid"));
                    favoriteChat.setCid(resultSet.getInt("cid"));
                    favoriteChat.setCreateTime(resultSet.getTimestamp("createTime"));
                    favoriteChat.setHname(resultSet.getString("hname"));
                    favoriteChat.setHtime(resultSet.getTimestamp("htime"));
                    favoriteChats.add(favoriteChat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 如果查询失败，回退到简单查询
            return getFavoriteChatsSimple(uid);
        }
        return favoriteChats;
    }

    // 简单查询作为备用方案
    private List<FavoriteChat> getFavoriteChatsSimple(Integer uid) {
        List<FavoriteChat> favoriteChats = new ArrayList<>();
        String sql = "SELECT * FROM favoriteChat WHERE uid = ? ORDER BY createTime DESC";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, uid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    FavoriteChat favoriteChat = new FavoriteChat();
                    favoriteChat.setId(resultSet.getInt("id"));
                    favoriteChat.setUid(resultSet.getInt("uid"));
                    favoriteChat.setCid(resultSet.getInt("cid"));
                    favoriteChat.setCreateTime(resultSet.getTimestamp("createTime"));
                    favoriteChats.add(favoriteChat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取收藏列表时发生数据库错误: " + e.getMessage());
        }
        return favoriteChats;
    }

    @Override
    public boolean checkFavoriteChat(Integer uid, Integer cid) {
        String sql = "SELECT COUNT(*) FROM favoriteChat WHERE uid = ? AND cid = ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, cid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("检查收藏状态时发生数据库错误: " + e.getMessage());
        }
        return false;
    }
}