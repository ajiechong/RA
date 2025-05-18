package org.example.researchassistant.dto;

import lombok.Data;

@Data
public class UserResourceDTO {
    private Long resourceId;
    private Long userId;
    private String content;
    private String time;
}
