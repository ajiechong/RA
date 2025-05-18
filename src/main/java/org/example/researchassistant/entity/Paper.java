package org.example.researchassistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Paper {
    @TableId(type = IdType.AUTO)
    private Long paperId;
    private String title;
    private String authors;
    private String abstractText;
    private String submissionDate;
    private String pdfLink;
}
