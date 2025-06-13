package org.example.researchassistant.service;

import org.example.researchassistant.domain.FileAttachment;

public interface AIService {
    /**
     * 原有的AI对话方法
     */
    String getAIResponse(Integer cid, String userMessage);

    /**
     * 新增：使用通义千问API解析文件内容
     * @param file 文件对象
     * @return 解析后的文本内容
     */
    String parseFileWithQwen(FileAttachment file);

    /**
     * 新增：使用通义千问VL模型解析图片内容
     * @param imageUrl 图片URL
     * @return 图片内容描述
     */
    String parseImageWithQwen(String imageUrl);

    /**
     * 新增：使用通义千问OCR模型提取图片中的文字
     * @param imageUrl 图片URL
     * @return 提取的文字内容
     */
    String extractTextFromImageWithQwen(String imageUrl);
}