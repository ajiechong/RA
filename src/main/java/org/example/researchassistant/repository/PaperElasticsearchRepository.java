package org.example.researchassistant.repository;

import org.example.researchassistant.document.PaperDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperElasticsearchRepository extends ElasticsearchRepository<PaperDocument, String> {

    // 现有的简单搜索方法
    Page<PaperDocument> findByTitleContaining(String title, Pageable pageable);
    Page<PaperDocument> findByAuthorsContaining(String authors, Pageable pageable);
    Page<PaperDocument> findByAbstractTextContaining(String abstractText, Pageable pageable);
    Page<PaperDocument> findByTitleContainingOrAuthorsContaining(String title, String authors, Pageable pageable);

    // 🔥 新增：真正的多字段全文搜索（带权重）
    @Query("""
        {
          "multi_match": {
            "query": "?0",
            "fields": ["title^3", "authors^2", "abstractText^1"],
            "type": "best_fields",
            "fuzziness": "AUTO"
          }
        }
        """)
    Page<PaperDocument> multiFieldSearch(String keyword, Pageable pageable);

    // 🔥 新增：更高级的多字段搜索（布尔查询）
    @Query("""
        {
          "bool": {
            "should": [
              {
                "match": {
                  "title": {
                    "query": "?0",
                    "boost": 3.0,
                    "fuzziness": "AUTO"
                  }
                }
              },
              {
                "match": {
                  "authors": {
                    "query": "?0",
                    "boost": 2.0
                  }
                }
              },
              {
                "match": {
                  "abstractText": {
                    "query": "?0",
                    "boost": 1.0
                  }
                }
              }
            ],
            "minimum_should_match": 1
          }
        }
        """)
    Page<PaperDocument> advancedMultiFieldSearch(String keyword, Pageable pageable);

    // 🔥 新增：支持多个关键词的搜索
    @Query("""
        {
          "bool": {
            "should": [
              {
                "multi_match": {
                  "query": "?0",
                  "fields": ["title^3", "authors^2"],
                  "type": "cross_fields"
                }
              },
              {
                "match": {
                  "abstractText": "?0"
                }
              }
            ]
          }
        }
        """)
    Page<PaperDocument> crossFieldsSearch(String keyword, Pageable pageable);

    // 🔥 新增：精确短语搜索 + 模糊搜索组合
    @Query("""
        {
          "bool": {
            "should": [
              {
                "multi_match": {
                  "query": "?0",
                  "fields": ["title^5", "authors^3"],
                  "type": "phrase"
                }
              },
              {
                "multi_match": {
                  "query": "?0",
                  "fields": ["title^2", "authors^1", "abstractText^0.5"],
                  "fuzziness": "AUTO"
                }
              }
            ]
          }
        }
        """)
    Page<PaperDocument> hybridSearch(String keyword, Pageable pageable);
}