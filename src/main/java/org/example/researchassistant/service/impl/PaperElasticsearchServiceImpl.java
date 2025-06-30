package org.example.researchassistant.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.researchassistant.document.PaperDocument;
import org.example.researchassistant.entity.Paper;
import org.example.researchassistant.repository.PaperElasticsearchRepository;
import org.example.researchassistant.service.PaperElasticsearchService;
import org.example.researchassistant.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PaperElasticsearchServiceImpl implements PaperElasticsearchService {

    @Autowired
    private PaperElasticsearchRepository elasticsearchRepository;

    @Autowired
    private PaperService paperService;

    @Override
    public PaperDocument indexPaper(PaperDocument paperDocument) {
        return elasticsearchRepository.save(paperDocument);
    }

    @Override
    public void indexAllPapers() {
        try {
            log.info("开始从数据库同步数据到Elasticsearch");
            List<Paper> allPapers = paperService.list();

            for (Paper paper : allPapers) {
                PaperDocument doc = PaperDocument.fromPaper(paper);
                elasticsearchRepository.save(doc);
            }

            log.info("数据同步完成，共同步{}条记录", allPapers.size());
        } catch (Exception e) {
            log.error("数据同步失败", e);
            throw new RuntimeException("数据同步失败", e);
        }
    }

    @Override
    public Page<PaperDocument> searchPapers(String keyword, Pageable pageable) {
        // 使用组合搜索：标题或作者包含关键词
        return elasticsearchRepository.findByTitleContainingOrAuthorsContaining(keyword, keyword, pageable);
    }

    @Override
    public Page<PaperDocument> advancedSearchPapers(String keyword, Pageable pageable) {
        // 高级搜索，可以搜索摘要
        return elasticsearchRepository.findByAbstractTextContaining(keyword, pageable);
    }

    @Override
    public Map<String, Object> searchPapersFast(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize + 1,
                Sort.by(Sort.Direction.DESC, "submissionDate"));

        Page<PaperDocument> page = elasticsearchRepository.findByTitleContainingOrAuthorsContaining(keyword, keyword, pageable);
        List<PaperDocument> records = page.getContent();

        boolean hasNextPage = records.size() > pageSize;

        if (hasNextPage) {
            records = records.subList(0, pageSize);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("hasNext", hasNextPage);
        result.put("currentPage", pageNum);
        return result;
    }

    @Override
    public PaperDocument getPaperById(String id) {
        Optional<PaperDocument> optional = elasticsearchRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void deletePaper(String id) {
        elasticsearchRepository.deleteById(id);
    }
}