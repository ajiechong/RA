package org.example.researchassistant.dto;

import lombok.Data;

@Data
public class PaperSummaryDTO {
    private Long paperId;
    private String title;
    private String authors;
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
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setSubmissionDate(String submissionDate){
        this.submissionDate = submissionDate;
    }

    public void setPdfLink(String pdfLink){
        this.pdfLink = pdfLink;
    }
}
