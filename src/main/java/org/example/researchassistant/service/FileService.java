package org.example.researchassistant.service;

import org.example.researchassistant.domain.FileAttachment;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileService {

    /**
     * 上传文件到OSS并解析内容
     * @param file 上传的文件
     * @param cid 会话ID
     * @param uid 用户ID
     * @return 文件信息
     */
    FileAttachment uploadFile(MultipartFile file, Integer cid, Integer uid);

    /**
     * 获取会话的所有文件
     */
    List<FileAttachment> getFilesByCid(Integer cid);

    /**
     * 获取用户的所有文件
     */
    List<FileAttachment> getFilesByUid(Integer uid);

    /**
     * 删除文件
     */
    void deleteFile(Integer fid);

    /**
     * 根据文件ID获取文件信息
     */
    FileAttachment getFileById(Integer fid);

    /**
     * 提取文件文本内容（供AI使用）
     */
    String extractFileText(Integer fid);

    /**
     * 获取会话中最近的文件内容（用于AI上下文）
     */
    String getRecentFilesContext(Integer cid, Integer limit);
}