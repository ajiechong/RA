package org.example.researchassistant.domain;

import java.time.LocalDateTime;

public class FileAttachment {
    private Integer fid;           // 文件ID
    private Integer cid;           // 会话ID
    private Integer uid;           // 用户ID
    private String fileName;       // 原始文件名
    private String fileType;       // 文件类型
    private Long fileSize;         // 文件大小
    private String ossUrl;         // OSS存储URL
    private String extractedText;  // 提取的文本内容
    private LocalDateTime uploadTime; // 上传时间
    private Integer status;        // 状态

    // 构造函数
    public FileAttachment() {}

    public FileAttachment(Integer cid, Integer uid, String fileName, String fileType,
                          Long fileSize, String ossUrl, String extractedText) {
        this.cid = cid;
        this.uid = uid;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.ossUrl = ossUrl;
        this.extractedText = extractedText;
        this.uploadTime = LocalDateTime.now();
        this.status = 1;
    }

    // Getter和Setter方法
    public Integer getFid() { return fid; }
    public void setFid(Integer fid) { this.fid = fid; }

    public Integer getCid() { return cid; }
    public void setCid(Integer cid) { this.cid = cid; }

    public Integer getUid() { return uid; }
    public void setUid(Integer uid) { this.uid = uid; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getOssUrl() { return ossUrl; }
    public void setOssUrl(String ossUrl) { this.ossUrl = ossUrl; }

    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }

    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    @Override
    public String toString() {
        return "FileAttachment{" +
                "fid=" + fid +
                ", cid=" + cid +
                ", uid=" + uid +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", ossUrl='" + ossUrl + '\'' +
                ", uploadTime=" + uploadTime +
                ", status=" + status +
                '}';
    }
}