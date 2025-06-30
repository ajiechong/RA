package org.example.researchassistant.repository;

import org.example.researchassistant.document.PaperDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperElasticsearchRepository extends ElasticsearchRepository<PaperDocument, String> {

    // 根据标题搜索
    Page<PaperDocument> findByTitleContaining(String title, Pageable pageable);

    // 根据作者搜索
    Page<PaperDocument> findByAuthorsContaining(String authors, Pageable pageable);

    // 根据摘要搜索
    Page<PaperDocument> findByAbstractTextContaining(String abstractText, Pageable pageable);

    // 组合搜索 - 标题或作者包含关键词
    Page<PaperDocument> findByTitleContainingOrAuthorsContaining(String title, String authors, Pageable pageable);
}