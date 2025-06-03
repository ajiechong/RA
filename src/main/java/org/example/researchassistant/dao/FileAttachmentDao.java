package org.example.researchassistant.dao;

import org.example.researchassistant.domain.FileAttachment;
import java.util.List;

public interface FileAttachmentDao {

    /**
     * 插入文件记录
     */
    void insertFileAttachment(FileAttachment fileAttachment);

    /**
     * 根据文件ID获取文件信息
     */
    FileAttachment getFileAttachmentById(Integer fid);

    /**
     * 根据会话ID获取所有文件
     */
    List<FileAttachment> getFileAttachmentsByCid(Integer cid);

    /**
     * 根据用户ID获取所有文件
     */
    List<FileAttachment> getFileAttachmentsByUid(Integer uid);

    /**
     * 删除文件记录（软删除）
     */
    void deleteFileAttachment(Integer fid);

    /**
     * 更新文件信息
     */
    void updateFileAttachment(FileAttachment fileAttachment);

    /**
     * 根据会话ID获取最近的文件（用于AI上下文）
     */
    List<FileAttachment> getRecentFilesByCid(Integer cid, Integer limit);
}