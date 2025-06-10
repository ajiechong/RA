package org.example.researchassistant.dto;

import lombok.Data;

@Data
public class PaperSummaryDTO {
    private Long paperId;
    private String title;
    private String authors;
    private String submissionDate;
    private String pdfLink;
}