package org.example.researchassistant.utils;

import org.example.researchassistant.domain.FileAttachment;
import org.example.researchassistant.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 聊天上下文辅助类 - 为AI提供文件内容支持
 */
@Component
public class ChatContextHelper {

    private static final Logger logger = LoggerFactory.getLogger(ChatContextHelper.class);

    @Autowired
    private FileService fileService;

    /**
     * 为AI聊天构建完整的上下文信息
     * @param cid 会话ID
     * @param userMessage 用户消息
     * @param includeFileCount 包含的最近文件数量
     * @return 包含文件内容的完整消息
     */
    public String buildChatContext(Integer cid, String userMessage, Integer includeFileCount) {
        try {
            StringBuilder contextBuilder = new StringBuilder();

            // 获取最近的文件
            List<FileAttachment> recentFiles = fileService.getFilesByCid(cid);
            if (!recentFiles.isEmpty()) {
                // 限制文件数量
                int fileCount = Math.min(includeFileCount != null ? includeFileCount : 3, recentFiles.size());

                contextBuilder.append("=== 用户最近上传的文件信息 ===\n");

                for (int i = 0; i < fileCount; i++) {
                    FileAttachment file = recentFiles.get(i);
                    contextBuilder.append(String.format("\n【文件 %d】\n", i + 1));
                    contextBuilder.append(String.format("文件名: %s\n", file.getFileName()));
                    contextBuilder.append(String.format("文件类型: %s\n", file.getFileType()));
                    contextBuilder.append(String.format("上传时间: %s\n", file.getUploadTime()));

                    if (file.getExtractedText() != null && !file.getExtractedText().trim().isEmpty()) {
                        String content = file.getExtractedText();

                        // 如果是图片文件，特别标注
                        if (isImageFile(file.getFileType())) {
                            contextBuilder.append("文件内容: [这是一张图片，以下是图片信息]\n");
                        } else {
                            contextBuilder.append("文件内容:\n");
                        }

                        // 限制内容长度，避免上下文过长
                        if (content.length() > 2000) {
                            contextBuilder.append(content.substring(0, 2000));
                            contextBuilder.append("\n...[内容已截取，完整内容请查看原文件]\n");
                        } else {
                            contextBuilder.append(content).append("\n");
                        }
                    } else {
                        contextBuilder.append("文件内容: [无法提取内容或内容为空]\n");
                    }
                    contextBuilder.append("--- 文件分隔符 ---\n");
                }

                contextBuilder.append("\n=== 用户当前消息 ===\n");
            }

            contextBuilder.append(userMessage);

            return contextBuilder.toString();

        } catch (Exception e) {
            logger.error("构建聊天上下文失败: {}", e.getMessage(), e);
            return userMessage; // 如果构建失败，返回原始消息
        }
    }

    /**
     * 检查用户消息是否包含对文件的引用
     * @param message 用户消息
     * @return 是否引用了文件
     */
    public boolean containsFileReference(String message) {
        if (message == null) return false;

        String lowerMessage = message.toLowerCase();
        return lowerMessage.contains("图片") ||
                lowerMessage.contains("文件") ||
                lowerMessage.contains("文档") ||
                lowerMessage.contains("上传") ||
                lowerMessage.contains("这个") ||
                lowerMessage.contains("刚才") ||
                lowerMessage.contains("分析") ||
                lowerMessage.contains("总结") ||
                lowerMessage.contains("看看") ||
                lowerMessage.contains("里面") ||
                lowerMessage.contains("内容");
    }

    /**
     * 为图片分析构建特殊提示
     * @param cid 会话ID
     * @return 图片分析提示
     */
    public String buildImageAnalysisPrompt(Integer cid) {
        try {
            List<FileAttachment> files = fileService.getFilesByCid(cid);
            StringBuilder prompt = new StringBuilder();

            boolean hasImages = false;
            for (FileAttachment file : files) {
                if (isImageFile(file.getFileType())) {
                    if (!hasImages) {
                        prompt.append("用户上传了以下图片，请分析其内容：\n\n");
                        hasImages = true;
                    }

                    prompt.append(String.format("图片: %s\n", file.getFileName()));
                    if (file.getExtractedText() != null) {
                        prompt.append(file.getExtractedText()).append("\n\n");
                    }
                }
            }

            if (hasImages) {
                prompt.append("请基于上述图片信息，为用户提供详细的分析和解答。");
                prompt.append("如果用户询问图片中的具体内容，请根据图片信息进行回答。");
            }

            return prompt.toString();

        } catch (Exception e) {
            logger.error("构建图片分析提示失败: {}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * 获取会话中最新的文件信息摘要
     * @param cid 会话ID
     * @param limit 文件数量限制
     * @return 文件摘要
     */
    public String getFilesSummary(Integer cid, Integer limit) {
        try {
            List<FileAttachment> files = fileService.getFilesByCid(cid);
            if (files.isEmpty()) {
                return "当前会话中没有上传的文件。";
            }

            StringBuilder summary = new StringBuilder();
            summary.append(String.format("会话中共有 %d 个文件：\n", files.size()));

            int count = Math.min(limit != null ? limit : 5, files.size());
            for (int i = 0; i < count; i++) {
                FileAttachment file = files.get(i);
                summary.append(String.format("%d. %s (%s, %.1f KB, %s)\n",
                        i + 1,
                        file.getFileName(),
                        file.getFileType().toUpperCase(),
                        file.getFileSize() / 1024.0,
                        file.getUploadTime().toString().substring(0, 16)
                ));
            }

            if (files.size() > count) {
                summary.append(String.format("... 还有 %d 个文件未显示\n", files.size() - count));
            }

            return summary.toString();

        } catch (Exception e) {
            logger.error("获取文件摘要失败: {}", e.getMessage(), e);
            return "获取文件信息时出现错误。";
        }
    }

    /**
     * 检查文件类型是否为图片
     * @param fileType 文件类型
     * @return 是否为图片
     */
    private boolean isImageFile(String fileType) {
        if (fileType == null) return false;
        String lower = fileType.toLowerCase();
        return lower.equals("jpg") || lower.equals("jpeg") ||
                lower.equals("png") || lower.equals("gif") ||
                lower.equals("bmp") || lower.equals("webp");
    }

    /**
     * 为AI提供文件操作指导
     * @param userMessage 用户消息
     * @return 操作指导
     */
    public String getFileOperationGuidance(String userMessage) {
        if (userMessage == null) return "";

        String lowerMessage = userMessage.toLowerCase();

        if (lowerMessage.contains("看不到") || lowerMessage.contains("无法") || lowerMessage.contains("不能")) {
            return "\n\n💡 提示：如果您希望我分析文件内容，请确保：\n" +
                    "1. 文件已成功上传（页面显示上传成功）\n" +
                    "2. 对于图片文件，我可以分析图片的基本信息和特征\n" +
                    "3. 对于文档文件，我可以分析其中的文字内容\n" +
                    "4. 请具体描述您想了解文件中的什么内容";
        }

        if (lowerMessage.contains("分析") || lowerMessage.contains("总结")) {
            return "\n\n📋 我可以帮您：\n" +
                    "• 分析文档中的文字内容和结构\n" +
                    "• 总结图片的视觉特征和可能内容\n" +
                    "• 解读表格和数据信息\n" +
                    "• 回答关于文件内容的具体问题";
        }

        return "";
    }
}