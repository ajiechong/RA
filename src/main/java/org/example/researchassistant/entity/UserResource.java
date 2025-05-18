package org.example.researchassistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_resource")
public class UserResource {
    @TableId(type = IdType.AUTO)
    private Long resourceId;
    private Long userId;
    private String content;
    private String time;
}
