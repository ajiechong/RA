package org.example.researchassistant.entity;

import java.util.List;

// RagResponse.java
public class RagResponse {
    private List<RagResult> results;
    private Integer total;

    // Getter 和 Setter
    public List<RagResult> getResults() { return results; }
    public void setResults(List<RagResult> results) { this.results = results; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}
