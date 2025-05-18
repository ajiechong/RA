package cn.example.researchassistant.dao.impl;

import cn.example.researchassistant.utils.JDBCUtils;
import cn.example.researchassistant.dao.ResourceVideoDao;
import cn.example.researchassistant.domain.ResourceVideo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceVideoDaoImpl implements ResourceVideoDao {

    private JDBCUtils jdbcUtils = new JDBCUtils();

    @Override
    public List<ResourceVideo> getAllResourceVideos() {
        List<ResourceVideo> resourceVideoList = new ArrayList<>();
        String sql = "SELECT * FROM resource_video";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ResourceVideo resourceVideo = new ResourceVideo();
                resourceVideo.setRid(resultSet.getInt("Rid"));
                resourceVideo.setRname(resultSet.getString("Rname"));
                resourceVideo.setRTime(resultSet.getString("RTime"));
                resourceVideo.setRurl(resultSet.getString("Rurl"));
                resourceVideo.setRstore(resultSet.getInt("Rstore"));
                resourceVideo.setRtag(resultSet.getString("Rtag"));
                resourceVideo.setRabstract(resultSet.getString("Rabstract"));
                resourceVideo.setRimg(resultSet.getString("vimg")); // 设置图片字段
                resourceVideoList.add(resourceVideo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resourceVideoList;
    }

    @Override
    public ResourceVideo getResourceVideoById(int rid) {
        ResourceVideo resourceVideo = new ResourceVideo();
        String sql = "SELECT * FROM resource_video WHERE Rid =?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    resourceVideo.setRid(resultSet.getInt("Rid"));
                    resourceVideo.setRname(resultSet.getString("Rname"));
                    resourceVideo.setRTime(resultSet.getString("RTime"));
                    resourceVideo.setRurl(resultSet.getString("Rurl"));
                    resourceVideo.setRstore(resultSet.getInt("Rstore"));
                    resourceVideo.setRtag(resultSet.getString("Rtag"));
                    resourceVideo.setRabstract(resultSet.getString("Rabstract"));
                    resourceVideo.setRimg(resultSet.getString("vimg")); // 设置图片字段
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resourceVideo;
    }

    @Override
    public List<ResourceVideo> getResourceVideosByName(String rname) {
        List<ResourceVideo> resourceVideoList = new ArrayList<>();
        String sql = "SELECT * FROM resource_video WHERE Rname LIKE ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, '%' + rname + '%');
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ResourceVideo resourceVideo = new ResourceVideo();
                    resourceVideo.setRid(resultSet.getInt("Rid"));
                    resourceVideo.setRname(resultSet.getString("Rname"));
                    resourceVideo.setRTime(resultSet.getString("RTime"));
                    resourceVideo.setRurl(resultSet.getString("Rurl"));
                    resourceVideo.setRstore(resultSet.getInt("Rstore"));
                    resourceVideo.setRtag(resultSet.getString("Rtag"));
                    resourceVideo.setRabstract(resultSet.getString("Rabstract"));
                    resourceVideo.setRimg(resultSet.getString("vimg")); // 设置图片字段
                    resourceVideoList.add(resourceVideo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resourceVideoList;
    }

    @Override
    public List<ResourceVideo> getResourceVideosByTag(String rtag) {
        List<ResourceVideo> resourceVideoList = new ArrayList<>();
        String sql = "SELECT * FROM resource_video WHERE Rtag LIKE ?";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + rtag + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ResourceVideo resourceVideo = new ResourceVideo();
                    resourceVideo.setRid(resultSet.getInt("Rid"));
                    resourceVideo.setRname(resultSet.getString("Rname"));
                    resourceVideo.setRTime(resultSet.getString("RTime"));
                    resourceVideo.setRurl(resultSet.getString("Rurl"));
                    resourceVideo.setRstore(resultSet.getInt("Rstore"));
                    resourceVideo.setRtag(resultSet.getString("Rtag"));
                    resourceVideo.setRabstract(resultSet.getString("Rabstract"));
                    resourceVideo.setRimg(resultSet.getString("vimg")); // 设置图片字段
                    resourceVideoList.add(resourceVideo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resourceVideoList;
    }

    @Override
    public void insertResourceVideo(ResourceVideo resourceVideo) {
        String sql = "INSERT INTO resource_video (Rname, RTime, Rurl, Rstore, Rtag, Rabstract) VALUES (?,?,?,?,?,?)";
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, resourceVideo.getRname());
            preparedStatement.setString(2, resourceVideo.getRTime());
            preparedStatement.setString(3, resourceVideo.getRurl());
            preparedStatement.setInt(4, resourceVideo.getRstore());
            preparedStatement.setString(5, resourceVideo.getRtag());
            preparedStatement.setString(6, resourceVideo.getRabstract());
            preparedStatement.setString(7, resourceVideo.getRimg());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateResourceVideo(ResourceVideo resourceVideo, String condition) {
        String sql = "UPDATE resource_video SET Rname =?, RTime =?, Rurl =?, Rstore =?, Rtag =?, Rabstract =? WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, resourceVideo.getRname());
            preparedStatement.setString(2, resourceVideo.getRTime());
            preparedStatement.setString(3, resourceVideo.getRurl());
            preparedStatement.setInt(4, resourceVideo.getRstore());
            preparedStatement.setString(5, resourceVideo.getRtag());
            preparedStatement.setString(6, resourceVideo.getRabstract());
            preparedStatement.setString(7, resourceVideo.getRimg());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteResourceVideo(String condition) {
        String sql = "DELETE FROM resource_video WHERE " + condition;
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}