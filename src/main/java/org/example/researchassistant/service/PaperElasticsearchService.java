package org.example.researchassistant.service;

import org.example.researchassistant.document.PaperDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PaperElasticsearchService {

    // 索引单个文档
    PaperDocument indexPaper(PaperDocument paperDocument);

    // 批量索引
    void indexAllPapers();

    // 搜索论文
    Page<PaperDocument> searchPapers(String keyword, Pageable pageable);

    // 高级搜索
    Page<PaperDocument> advancedSearchPapers(String keyword, Pageable pageable);

    // 快速搜索（类似/fast接口，不返回总数）
    Map<String, Object> searchPapersFast(String keyword, Integer pageNum, Integer pageSize);

    // 根据ID获取
    PaperDocument getPaperById(String id);

    // 删除文档
    void deletePaper(String id);

    Page<PaperDocument> multiFieldSearch(String keyword, Pageable pageable);

    Page<PaperDocument> advancedMultiFieldSearch(String keyword, Pageable pageable);

    Page<PaperDocument> crossFieldsSearch(String keyword, Pageable pageable);

    Page<PaperDocument> hybridSearch(String keyword, Pageable pageable);
}