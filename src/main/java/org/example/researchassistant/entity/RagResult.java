package org.example.researchassistant.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

// RagResult.java
public class RagResult {
    @JsonProperty("Title")
    private String title;

    @JsonProperty("Authors")
    private String authors;

    @JsonProperty("Abstract")
    private String abstractText;  // 避免使用关键字 abstract

    @JsonProperty("Date")
    private String date;

    @JsonProperty("PDF")
    private String pdf;

    @JsonProperty("Score")
    private String score;

    // Getter 和 Setter 方法（使用标准驼峰命名）
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPdf() { return pdf; }
    public void setPdf(String pdf) { this.pdf = pdf; }

    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }
}
