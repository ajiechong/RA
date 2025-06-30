package org.example.researchassistant.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Data;

@Data
@Document(indexName = "papers")
public class PaperDocument {

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long paperId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String authors;

    @Field(type = FieldType.Text)
    private String abstractText;

    @Field(type = FieldType.Keyword)
    private String submissionDate;

    @Field(type = FieldType.Keyword)
    private String pdfLink;

    public static PaperDocument fromPaper(org.example.researchassistant.entity.Paper paper) {
        PaperDocument doc = new PaperDocument();
        doc.setId(String.valueOf(paper.getPaperId()));
        doc.setPaperId(paper.getPaperId());
        doc.setTitle(paper.getTitle());
        doc.setAuthors(paper.getAuthors());
        doc.setAbstractText(paper.getAbstractText());
        doc.setSubmissionDate(paper.getSubmissionDate());
        doc.setPdfLink(paper.getPdfLink());
        return doc;
    }
}