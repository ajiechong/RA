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
    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthors() {
        return authors;
    }
    public String getSubmissionDate(){
        return submissionDate;
    }

    public String getPdfLink(){
        return pdfLink;
    }
}
