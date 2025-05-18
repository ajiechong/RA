package org.example.researchassistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Chat {
    @TableId(type = IdType.AUTO)
    private Long chatId;
    private Long userId;
    private String content;
    private String time;
}
