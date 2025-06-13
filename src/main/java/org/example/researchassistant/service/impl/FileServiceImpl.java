package org.example.researchassistant.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.example.researchassistant.dao.FileAttachmentDao;
import org.example.researchassistant.domain.FileAttachment;
import org.example.researchassistant.service.AIService;
import org.example.researchassistant.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileAttachmentDao fileAttachmentDao;

    @Autowired
    private AIService aiService;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    // 支持的文件类型
    private static final List<String> SUPPORTED_TYPES = Arrays.asList(
            "pdf", "docx", "doc", "txt", "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    @Override
    public FileAttachment uploadFile(MultipartFile file, Integer cid, Integer uid) {
        logger.info("开始上传文件: {}, 大小: {}, 类型: {}", file.getOriginalFilename(), file.getSize(), file.getContentType());

        FileAttachment fileAttachment = null;
        String ossUrl = null;

        try {
            // 验证文件
            validateFile(file);
            logger.info("文件验证通过");

            // 生成OSS文件路径
            String ossKey = generateOssKey(file.getOriginalFilename(), uid);
            logger.info("生成OSS路径: {}", ossKey);

            // 上传到OSS
            ossUrl = uploadToOss(file, ossKey);
            logger.info("文件上传到OSS成功: {}", ossUrl);

            // 提取文件内容 - 这里可能会出错
            String extractedText;
            try {
                extractedText = extractTextFromFile(file);
                logger.info("文本提取成功，长度: {}", extractedText != null ? extractedText.length() : 0);
            } catch (Exception e) {
                logger.error("文本提取失败，但继续保存文件信息: {}", e.getMessage());
                extractedText = "文本提取失败: " + e.getMessage();
            }

            // 创建文件记录
            fileAttachment = new FileAttachment(
                    cid, uid, file.getOriginalFilename(),
                    getFileExtension(file.getOriginalFilename()),
                    file.getSize(), ossUrl, extractedText
            );

            logger.info("准备保存文件记录到数据库");

            // 保存到数据库 - 这里也可能会出错
            try {
                fileAttachmentDao.insertFileAttachment(fileAttachment);
                logger.info("文件记录保存到数据库成功，fid: {}", fileAttachment.getFid());
            } catch (Exception e) {
                logger.error("数据库保存失败: {}", e.getMessage(), e);
                throw new RuntimeException("数据库保存失败: " + e.getMessage(), e);
            }

            logger.info("文件上传完成: {}", file.getOriginalFilename());

            enhanceFileContentWithAI(fileAttachment);

            return fileAttachment;

        } catch (Exception e) {
            logger.error("文件上传失败: {}, OSS URL: {}", e.getMessage(), ossUrl, e);

            // 如果数据库保存失败但OSS上传成功，考虑是否需要清理OSS文件
            if (ossUrl != null && fileAttachment == null) {
                logger.warn("OSS文件已上传但数据库保存失败，OSS URL: {}", ossUrl);
                // 这里可以选择删除OSS文件或记录日志用于后续清理
            }

            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FileAttachment> getFilesByCid(Integer cid) {
        return fileAttachmentDao.getFileAttachmentsByCid(cid);
    }

    @Override
    public List<FileAttachment> getFilesByUid(Integer uid) {
        return fileAttachmentDao.getFileAttachmentsByUid(uid);
    }

    @Override
    public void deleteFile(Integer fid) {
        fileAttachmentDao.deleteFileAttachment(fid);
    }

    @Override
    public FileAttachment getFileById(Integer fid) {
        return fileAttachmentDao.getFileAttachmentById(fid);
    }

    @Override
    public String extractFileText(Integer fid) {
        FileAttachment file = fileAttachmentDao.getFileAttachmentById(fid);
        return file != null ? file.getExtractedText() : "";
    }

    @Override
    public String getRecentFilesContext(Integer cid, Integer limit) {
        List<FileAttachment> recentFiles = fileAttachmentDao.getRecentFilesByCid(cid, limit);
        if (recentFiles.isEmpty()) {
            return "";
        }

        StringBuilder context = new StringBuilder();
        context.append("=== 用户上传的文件内容 ===\n");

        for (FileAttachment file : recentFiles) {
            context.append(String.format("\n文件名: %s\n", file.getFileName()));
            context.append(String.format("文件类型: %s\n", file.getFileType()));
            if (file.getExtractedText() != null && !file.getExtractedText().trim().isEmpty()) {
                context.append("文件内容:\n");
                context.append(file.getExtractedText()).append("\n");
            } else {
                context.append("该文件无法提取文本内容\n");
            }
            context.append("--- 文件分隔符 ---\n");
        }

        return context.toString();
    }

    /**
     * 验证文件 - 简化版本
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String extension = getFileExtension(fileName).toLowerCase();
        if (!SUPPORTED_TYPES.contains(extension)) {
            throw new IllegalArgumentException("不支持的文件类型: " + extension + "。支持的类型: " + String.join(", ", SUPPORTED_TYPES));
        }

        // 文件大小限制 (50MB)
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过50MB，当前大小: " + (file.getSize() / 1024 / 1024) + "MB");
        }

        logger.info("文件验证通过: {} ({})", fileName, extension);
    }

    /**
     * 生成OSS存储路径
     */
    private String generateOssKey(String originalFilename, Integer uid) {
        String extension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return String.format("uploads/%d/%s_%s.%s", uid, timestamp, uuid, extension);
    }

    /**
     * 上传文件到OSS
     */
    private String uploadToOss(MultipartFile file, String ossKey) throws IOException {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, ossKey, file.getInputStream()
            );
            ossClient.putObject(putObjectRequest);

            // 返回文件的访问URL
            return String.format("https://%s.%s/%s", bucketName, endpoint, ossKey);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 从文件中提取文本内容 - 增强版本
     */
    private String extractTextFromFile(MultipartFile file) {
        try {
            String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();

            logger.info("开始提取文件文本，文件类型: {}, 文件名: {}", extension, file.getOriginalFilename());

            String basicText = "";

            switch (extension) {
                case "pdf":
                    basicText = extractTextFromPdf(file);
                    break;
                case "docx":
                    basicText = extractTextFromDocx(file);
                    break;
                case "doc":
                    basicText = extractTextFromDoc(file);
                    break;
                case "txt":
                    basicText = new String(file.getBytes(), "UTF-8");
                    break;
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                case "bmp":
                    // 对于图片，直接使用AI解析
                    return "图片文件，正在使用AI解析中..."; // 先返回占位符
                default:
                    return "无法提取此文件类型的文本内容";
            }

            // 如果基础提取成功，返回基础文本
            if (basicText != null && !basicText.trim().isEmpty() && !basicText.contains("提取失败")) {
                return basicText;
            } else {
                return "文本提取失败: 无法读取文件内容";
            }

        } catch (Exception e) {
            logger.error("文本提取失败，文件: {}, 错误: {}", file.getOriginalFilename(), e.getMessage(), e);
            return "文本提取失败: " + e.getMessage();
        }
    }

    private void enhanceFileContentWithAI(FileAttachment fileAttachment) {
        // 在新线程中执行AI增强，避免阻塞上传响应
        new Thread(() -> {
            try {
                logger.info("开始AI增强文件内容，文件ID: {}", fileAttachment.getFid());

                String fileType = fileAttachment.getFileType().toLowerCase();
                String enhancedContent = null;

                // 根据文件类型选择不同的AI处理方式
                switch (fileType) {
                    case "jpg":
                    case "jpeg":
                    case "png":
                    case "gif":
                    case "bmp":
                        // 图片文件使用AI图像解析
                        enhancedContent = aiService.parseImageWithQwen(fileAttachment.getOssUrl());
                        break;
                    case "pdf":
                    case "docx":
                    case "doc":
                    case "txt":
                        // 文档文件使用AI文档分析
                        enhancedContent = aiService.parseFileWithQwen(fileAttachment);
                        break;
                    default:
                        enhancedContent = fileAttachment.getExtractedText(); // 保持原内容
                }

                if (enhancedContent != null) {
                    // 更新数据库中的文件内容
                    fileAttachment.setExtractedText(enhancedContent);
                    fileAttachmentDao.updateFileAttachment(fileAttachment);
                    logger.info("AI内容增强完成，文件ID: {}", fileAttachment.getFid());
                }

            } catch (Exception e) {
                logger.error("AI内容增强失败，文件ID: {}", fileAttachment.getFid(), e);
                try {
                    // 更新为错误状态，但保留原有内容
                    String originalContent = fileAttachment.getExtractedText();
                    String errorContent = originalContent + "\n\n[AI增强失败: " + e.getMessage() + "]";
                    fileAttachment.setExtractedText(errorContent);
                    fileAttachmentDao.updateFileAttachment(fileAttachment);
                } catch (Exception updateException) {
                    logger.error("更新文件错误状态失败", updateException);
                }
            }
        }).start();
    }

    /**
     * 从DOC提取文本（老版本Word）
     */
    private String extractTextFromDoc(MultipartFile file) throws IOException {
        // 这里可以添加对老版本Word文档的支持
        // 需要额外的依赖和处理逻辑
        return "暂不支持.doc格式，请转换为.docx格式";
    }

    /**
     * 从PDF提取文本
     */
    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    /**
     * 从DOCX提取文本 - 完整实现版本
     */
    private String extractTextFromDocx(MultipartFile file) throws IOException {
        logger.info("开始处理DOCX文件: {}, 大小: {}", file.getOriginalFilename(), file.getSize());

        String basicInfo = String.format("Word文档: %s (大小: %.2f KB)\n\n",
                file.getOriginalFilename(), file.getSize() / 1024.0);

        try {
            // 使用字节数组避免流被重复使用的问题
            byte[] fileBytes = file.getBytes();

            try (ByteArrayInputStream byteStream = new ByteArrayInputStream(fileBytes);
                 XWPFDocument document = new XWPFDocument(byteStream)) {

                StringBuilder contentBuilder = new StringBuilder();

                // 方法1：使用XWPFWordExtractor（通常最有效）
                try (XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
                    String extractedText = extractor.getText();
                    if (extractedText != null && !extractedText.trim().isEmpty()) {
                        logger.info("使用XWPFWordExtractor成功提取文本，长度: {}", extractedText.length());
                        return basicInfo + "文档内容:\n" + extractedText.trim();
                    }
                } catch (Exception extractorEx) {
                    logger.warn("XWPFWordExtractor提取失败，尝试手动提取: {}", extractorEx.getMessage());
                }

                // 方法2：手动提取段落文本
                logger.info("开始手动提取段落文本");
                for (int i = 0; i < document.getParagraphs().size(); i++) {
                    String paragraphText = document.getParagraphs().get(i).getText();
                    if (paragraphText != null && !paragraphText.trim().isEmpty()) {
                        contentBuilder.append(paragraphText.trim()).append("\n");
                    }
                }

                // 方法3：提取表格内容
                logger.info("开始提取表格内容");
                if (!document.getTables().isEmpty()) {
                    contentBuilder.append("\n=== 表格内容 ===\n");
                    for (int tableIndex = 0; tableIndex < document.getTables().size(); tableIndex++) {
                        var table = document.getTables().get(tableIndex);
                        contentBuilder.append(String.format("表格 %d:\n", tableIndex + 1));

                        for (int rowIndex = 0; rowIndex < table.getRows().size(); rowIndex++) {
                            var row = table.getRows().get(rowIndex);
                            StringBuilder rowText = new StringBuilder();

                            for (int cellIndex = 0; cellIndex < row.getTableCells().size(); cellIndex++) {
                                String cellText = row.getTableCells().get(cellIndex).getText();
                                if (cellText != null && !cellText.trim().isEmpty()) {
                                    rowText.append(cellText.trim()).append(" | ");
                                }
                            }

                            if (rowText.length() > 0) {
                                contentBuilder.append("  ").append(rowText.toString()).append("\n");
                            }
                        }
                        contentBuilder.append("\n");
                    }
                }

                String finalContent = contentBuilder.toString().trim();
                if (!finalContent.isEmpty()) {
                    logger.info("手动提取成功，文本长度: {}", finalContent.length());
                    return basicInfo + "文档内容:\n" + finalContent;
                } else {
                    logger.warn("文档中未找到可提取的文本内容");
                    return basicInfo + "文档中未找到可读取的文本内容，可能是图片文档或格式特殊";
                }

            } catch (Exception docEx) {
                logger.error("处理XWPFDocument时发生错误: {}", docEx.getMessage(), docEx);
                return basicInfo + "文档解析失败: " + docEx.getMessage();
            }

        } catch (Exception e) {
            logger.error("DOCX文件处理失败: {}", e.getMessage(), e);
            return basicInfo + "文档处理失败，请确保文件格式正确";
        }
    }

    /**
     * 从图片提取文本 - 增强描述版本
     */
    private String extractTextFromImage(MultipartFile file) throws IOException {
        logger.info("开始处理图片文件: {}", file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                return "无法读取图片文件";
            }

            int width = image.getWidth();
            int height = image.getHeight();
            String fileName = file.getOriginalFilename();

            StringBuilder description = new StringBuilder();
            description.append(String.format("图片文件: %s\n", fileName));
            description.append(String.format("尺寸: %d × %d 像素\n", width, height));
            description.append(String.format("文件大小: %.2f KB\n\n", file.getSize() / 1024.0));

            // 图片类型分析
            double aspectRatio = (double) width / height;
            description.append("图片特征分析:\n");

            if (aspectRatio > 3.0) {
                description.append("- 超宽图片，可能是横幅、时间轴或全景图\n");
            } else if (aspectRatio > 1.5) {
                description.append("- 横向图片，可能是截图、图表、演示文稿或风景照\n");
            } else if (aspectRatio < 0.3) {
                description.append("- 超高图片，可能是长截图、信息图或竖版海报\n");
            } else if (aspectRatio < 0.7) {
                description.append("- 竖向图片，可能是文档页面、手机截图或人像照片\n");
            } else {
                description.append("- 接近正方形的图片，可能是头像、图标或方形截图\n");
            }

            // 文件名分析
            String lowerFileName = fileName.toLowerCase();
            description.append("\n文件名提示:\n");
            if (lowerFileName.contains("screenshot") || lowerFileName.contains("screen") || lowerFileName.contains("capture")) {
                description.append("- 文件名表明这是屏幕截图，可能包含界面元素、文字或应用内容\n");
            } else if (lowerFileName.contains("chart") || lowerFileName.contains("graph") || lowerFileName.contains("plot")) {
                description.append("- 文件名表明这是图表，可能包含数据可视化、统计信息或分析结果\n");
            } else if (lowerFileName.contains("document") || lowerFileName.contains("page") || lowerFileName.contains("pdf")) {
                description.append("- 文件名表明这是文档页面，可能包含文字、表格或其他文档内容\n");
            } else if (lowerFileName.contains("diagram") || lowerFileName.contains("flow") || lowerFileName.contains("process")) {
                description.append("- 文件名表明这是流程图或示意图，可能包含步骤说明或概念图解\n");
            } else {
                description.append("- 普通图片文件\n");
            }

            // 颜色分析（简单版本）
            description.append("\n图片内容建议:\n");
            if (width > 1000 && height > 600) {
                description.append("- 高分辨率图片，细节丰富，适合查看详细内容\n");
            }

            // 使用建议
            description.append("\n使用建议:\n");
            description.append("- 如果图片包含重要文字，请手动输入关键文字内容\n");
            description.append("- 如果是图表或数据，请描述主要的数据趋势或结论\n");
            description.append("- 如果是截图，请说明截图的主要内容或目的\n");
            description.append("- 如果是文档页面，建议直接上传PDF或Word文档以获得更好的文本提取效果\n");

            logger.info("图片分析完成: {}", fileName);
            return description.toString();

        } catch (Exception e) {
            logger.error("图片处理失败: {}", e.getMessage(), e);
            return String.format("图片文件: %s\n处理失败: %s\n建议检查图片格式是否正确",
                    file.getOriginalFilename(), e.getMessage());
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}