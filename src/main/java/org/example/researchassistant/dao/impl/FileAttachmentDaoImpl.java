package org.example.researchassistant.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.researchassistant.dao.FileAttachmentDao;
import org.example.researchassistant.domain.FileAttachment;
import org.example.researchassistant.utils.JDBCUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class FileAttachmentDaoImpl implements FileAttachmentDao {

    @Override
    public void insertFileAttachment(FileAttachment fileAttachment) {
        String sql = "INSERT INTO file_attachments (cid, uid, file_name, file_type, file_size, " +
                "oss_url, extracted_text, upload_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        log.info("准备执行数据库插入，SQL: {}", sql);
        log.info("参数: cid={}, uid={}, fileName={}, fileType={}, fileSize={}",
                fileAttachment.getCid(), fileAttachment.getUid(),
                fileAttachment.getFileName(), fileAttachment.getFileType(),
                fileAttachment.getFileSize());

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, fileAttachment.getCid());
            ps.setInt(2, fileAttachment.getUid());
            ps.setString(3, fileAttachment.getFileName());
            ps.setString(4, fileAttachment.getFileType());
            ps.setLong(5, fileAttachment.getFileSize());
            ps.setString(6, fileAttachment.getOssUrl());
            ps.setString(7, fileAttachment.getExtractedText());
            ps.setObject(8, fileAttachment.getUploadTime());
            ps.setInt(9, fileAttachment.getStatus());

            log.info("开始执行数据库插入");
            int affectedRows = ps.executeUpdate();
            log.info("数据库插入影响行数: {}", affectedRows);

            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    fileAttachment.setFid(generatedId);
                    log.info("生成的文件ID: {}", generatedId);
                }
            } else {
                throw new SQLException("插入文件记录失败，没有影响任何行");
            }
        } catch (SQLException e) {
            log.error("数据库插入异常: {}", e.getMessage(), e);
            throw new RuntimeException("插入文件记录失败: " + e.getMessage(), e);
        }
    }

    @Override
    public FileAttachment getFileAttachmentById(Integer fid) {
        String sql = "SELECT * FROM file_attachments WHERE fid = ? AND status = 1";

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, fid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractFileAttachmentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FileAttachment> getFileAttachmentsByCid(Integer cid) {
        List<FileAttachment> files = new ArrayList<>();
        String sql = "SELECT * FROM file_attachments WHERE cid = ? AND status = 1 ORDER BY upload_time DESC";

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                files.add(extractFileAttachmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    @Override
    public List<FileAttachment> getFileAttachmentsByUid(Integer uid) {
        List<FileAttachment> files = new ArrayList<>();
        String sql = "SELECT * FROM file_attachments WHERE uid = ? AND status = 1 ORDER BY upload_time DESC";

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, uid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                files.add(extractFileAttachmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    @Override
    public void deleteFileAttachment(Integer fid) {
        String sql = "UPDATE file_attachments SET status = 0 WHERE fid = ?";

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, fid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除文件记录失败", e);
        }
    }

    @Override
    public void updateFileAttachment(FileAttachment fileAttachment) {
        String sql = "UPDATE file_attachments SET file_name = ?, file_type = ?, file_size = ?, " +
                "oss_url = ?, extracted_text = ? WHERE fid = ?";

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, fileAttachment.getFileName());
            ps.setString(2, fileAttachment.getFileType());
            ps.setLong(3, fileAttachment.getFileSize());
            ps.setString(4, fileAttachment.getOssUrl());
            ps.setString(5, fileAttachment.getExtractedText());
            ps.setInt(6, fileAttachment.getFid());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新文件记录失败", e);
        }
    }

    @Override
    public List<FileAttachment> getRecentFilesByCid(Integer cid, Integer limit) {
        List<FileAttachment> files = new ArrayList<>();
        String sql = "SELECT * FROM file_attachments WHERE cid = ? AND status = 1 " +
                "ORDER BY upload_time DESC LIMIT ?";

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cid);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                files.add(extractFileAttachmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    private FileAttachment extractFileAttachmentFromResultSet(ResultSet rs) throws SQLException {
        FileAttachment file = new FileAttachment();
        file.setFid(rs.getInt("fid"));
        file.setCid(rs.getInt("cid"));
        file.setUid(rs.getInt("uid"));
        file.setFileName(rs.getString("file_name"));
        file.setFileType(rs.getString("file_type"));
        file.setFileSize(rs.getLong("file_size"));
        file.setOssUrl(rs.getString("oss_url"));
        file.setExtractedText(rs.getString("extracted_text"));
        file.setUploadTime(rs.getObject("upload_time", LocalDateTime.class));
        file.setStatus(rs.getInt("status"));
        return file;
    }
}