package org.example.researchassistant.dto;

import lombok.Data;

@Data
public class PaperDTO {
    private Long paperId;
    private String title;
    private String authors;
    private String abstractText;
    private String submissionDate;
    private String pdfLink;
}
