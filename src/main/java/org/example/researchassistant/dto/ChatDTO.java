package org.example.researchassistant.dto;

import lombok.Data;

@Data
public class ChatDTO {
    private Long chatId;
    private Long userId;
    private String content;
    private String time;
}
