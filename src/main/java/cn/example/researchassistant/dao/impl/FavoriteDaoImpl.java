
package cn.example.researchassistant.dao.impl;

import cn.example.researchassistant.utils.JDBCUtils;
import cn.example.researchassistant.dao.FavoriteDao;
import cn.example.researchassistant.domain.Favorite;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FavoriteDaoImpl implements FavoriteDao {

    private JDBCUtils jdbcUtils = new JDBCUtils();

    @Override
    public void insertFavorite(Favorite favorite) {
        String sql = "INSERT INTO favorite (Uid, Rid, flag) VALUES (?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, favorite.getUid());
            preparedStatement.setInt(2, favorite.getRid());
            preparedStatement.setInt(3, favorite.getFlag());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("插入收藏记录失败，没有记录被添加");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加收藏时发生数据库错误: " + e.getMessage());
        }
    }

    @Override
    public void deleteFavorite(int Uid, int Rid, int flag) {
        String sql = "DELETE FROM favorite WHERE Uid =? AND Rid =? AND flag =?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Uid);
            preparedStatement.setInt(2, Rid);
            preparedStatement.setInt(3, flag);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Favorite> getFavoritesByUidAndFlag(int Uid, int flag) {
        List<Favorite> favoriteList = new ArrayList<>();
        String sql = "SELECT * FROM favorite WHERE Uid =? AND flag =?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Uid);
            preparedStatement.setInt(2, flag);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Favorite favorite = new Favorite(
                            resultSet.getInt("Uid"),
                            resultSet.getInt("Rid"),
                            resultSet.getInt("flag")
                    );
                    favoriteList.add(favorite);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteList;
    }
}


