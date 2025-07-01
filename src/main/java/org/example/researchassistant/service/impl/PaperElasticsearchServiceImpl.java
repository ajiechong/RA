package org.example.researchassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.util.*;

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

    // 优化后的 indexAllPapers() 方法 - 改用范围查询而不是分页

    @Override
    public void indexAllPapers() {
        try {
            log.info("=== 开始批量写入优化同步 ===");

            long totalCount = paperService.count();
            log.info("数据库中共有 {} 条记录", totalCount);

            if (totalCount == 0) {
                log.warn("数据库中没有数据");
                return;
            }

            int batchSize = 500;
            long processedCount = 0;
            long successCount = 0;
            long errorCount = 0;
            long startTime = System.currentTimeMillis();

            Long lastProcessedId = 0L;
            int batchNum = 1;

            log.info("开始批量写入同步，每批 {} 条", batchSize);

            while (processedCount < totalCount) {
                try {
                    long batchStartTime = System.currentTimeMillis();
                    log.info("处理第 {} 批次，从ID {} 开始...", batchNum, lastProcessedId);

                    // 查询数据
                    QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
                    queryWrapper.gt("paper_id", lastProcessedId)
                            .orderByAsc("paper_id")
                            .last("LIMIT " + batchSize);

                    List<Paper> papers = paperService.list(queryWrapper);
                    long queryTime = System.currentTimeMillis() - batchStartTime;

                    if (papers.isEmpty()) {
                        log.info("没有更多数据，同步完成");
                        break;
                    }

                    log.info("第 {} 批获取到 {} 条记录，查询耗时: {}ms", batchNum, papers.size(), queryTime);
                    lastProcessedId = papers.get(papers.size() - 1).getPaperId();

                    // ✅ 批量转换
                    List<PaperDocument> batchDocuments = new ArrayList<>();
                    int conversionErrors = 0;

                    for (Paper paper : papers) {
                        try {
                            PaperDocument doc = PaperDocument.fromPaper(paper);
                            batchDocuments.add(doc);
                        } catch (Exception e) {
                            conversionErrors++;
                            log.error("转换记录失败 [ID: {}]: {}", paper.getPaperId(), e.getMessage());
                        }
                    }

                    // ✅ 批量写入ES
                    long esStartTime = System.currentTimeMillis();
                    try {
                        elasticsearchRepository.saveAll(batchDocuments);
                        long esTime = System.currentTimeMillis() - esStartTime;

                        successCount += batchDocuments.size();
                        errorCount += conversionErrors;
                        processedCount += papers.size();

                        log.info("第 {} 批ES写入: {} 条，耗时 {}ms，速度: {:.0f} 条/秒",
                                batchNum, batchDocuments.size(), esTime,
                                batchDocuments.size() * 1000.0 / esTime);

                    } catch (Exception e) {
                        log.error("批量写入ES失败: {}", e.getMessage());
                        errorCount += batchDocuments.size();
                        processedCount += papers.size();
                    }

                    // 进度统计
                    double progress = (double) processedCount / totalCount * 100;
                    long batchTime = System.currentTimeMillis() - batchStartTime;
                    long totalElapsed = System.currentTimeMillis() - startTime;

                    log.info("第 {} 批完成: 成功 {}, 失败 {}, 总耗时 {}ms",
                            batchNum, batchDocuments.size(), conversionErrors, batchTime);
                    log.info("总进度: {}/{} ({:.1f}%) - 最后ID: {}",
                            processedCount, totalCount, progress, lastProcessedId);

                    // 速度估算
                    if (batchNum > 3) {
                        double avgSpeed = (double) processedCount / (totalElapsed / 1000.0);
                        long remainingRecords = totalCount - processedCount;
                        long estimatedSeconds = (long) (remainingRecords / avgSpeed);
                        log.info("当前速度: {:.0f} 记录/秒，预计剩余时间: {} 分钟",
                                avgSpeed, estimatedSeconds / 60);
                    }

                    // 适当休息
                    Thread.sleep(100);
                    batchNum++;

                } catch (Exception e) {
                    log.error("处理第 {} 批次时出错: {}", batchNum, e.getMessage());
                    errorCount += batchSize;
                    lastProcessedId += batchSize;
                    batchNum++;
                }
            }

            long totalTime = System.currentTimeMillis() - startTime;
            log.info("=== 批量写入同步完成 ===");
            log.info("总计处理: {} 条记录", processedCount);
            log.info("成功同步: {} 条", successCount);
            log.info("失败记录: {} 条", errorCount);
            log.info("成功率: {:.1f}%", processedCount > 0 ? (double) successCount / processedCount * 100 : 0);
            log.info("总耗时: {:.1f} 分钟", totalTime / 60000.0);
            log.info("平均速度: {:.0f} 记录/分钟", processedCount > 0 ? (double) processedCount / (totalTime / 60000.0) : 0);

        } catch (Exception e) {
            log.error("批量写入同步过程中发生严重错误", e);
            throw new RuntimeException("同步失败: " + e.getMessage(), e);
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