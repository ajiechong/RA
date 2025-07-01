package org.example.researchassistant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.researchassistant.document.PaperDocument;
import org.example.researchassistant.dto.PaperSummaryDTO;
import org.example.researchassistant.entity.Paper;
import org.example.researchassistant.service.PaperElasticsearchService;
import org.example.researchassistant.service.PaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @Autowired(required = false) // 允许ES服务不存在
    private PaperElasticsearchService elasticsearchService;

    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);

    /**
     * 🔥 重要：同步接口要放在 /{id} 路由之前！
     */
    @RequestMapping(value = "/sync", method = {RequestMethod.GET, RequestMethod.POST})
    public String syncToElasticsearch() {
        logger.info("收到数据同步请求");

        try {
            if (elasticsearchService == null) {
                logger.warn("Elasticsearch 服务未启用");
                return "Elasticsearch 服务未启用";
            }

            logger.info("开始执行数据同步...");
            elasticsearchService.indexAllPapers();
            logger.info("数据同步成功完成");
            return "数据同步成功";

        } catch (Exception e) {
            logger.error("数据同步失败", e);
            return "数据同步失败: " + e.getMessage();
        }
    }

    /**
     * 检查ES状态的接口
     */
    @RequestMapping(value = "/es-status", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> checkElasticsearchStatus() {
        logger.info("检查 Elasticsearch 状态");

        Map<String, Object> status = new HashMap<>();

        if (elasticsearchService == null) {
            status.put("available", false);
            status.put("message", "Elasticsearch 服务未注入");
            logger.warn("Elasticsearch 服务未注入");
        } else {
            try {
                status.put("available", true);
                status.put("message", "Elasticsearch 服务可用");
                logger.info("Elasticsearch 服务状态正常");
            } catch (Exception e) {
                status.put("available", false);
                status.put("message", "Elasticsearch 连接失败: " + e.getMessage());
                logger.error("Elasticsearch 连接失败", e);
            }
        }

        return status;
    }


    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String testEndpoint() {
        logger.info("测试接口被调用");
        return "测试接口正常工作！当前时间：" + new java.util.Date();
    }

    /**
     * 分页获取论文列表
     */
    @GetMapping
    public Page<PaperSummaryDTO> getPapersSummary(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        QueryWrapper<Paper> qw = new QueryWrapper<>();
        qw.select("paper_id", "title", "authors", "submission_date")
                .orderByDesc("submission_date");

        Page<Paper> paperPage = paperService.page(
                new Page<>(pageNum, pageSize, false),
                qw);

        Page<PaperSummaryDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(paperPage, dtoPage, "records");
        List<PaperSummaryDTO> summaries = paperPage.getRecords().stream().map(p -> {
            PaperSummaryDTO dto = new PaperSummaryDTO();
            dto.setPaperId(p.getPaperId());
            dto.setTitle(p.getTitle());
            dto.setAuthors(p.getAuthors());
            dto.setSubmissionDate(p.getSubmissionDate());
            return dto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(summaries);
        return dtoPage;
    }

    /**
     * 快速分页接口
     */
    @GetMapping("/fast")
    public Map<String, Object> getPapersFast(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<Paper> paperPage = new Page<>(pageNum, pageSize + 1);
        paperPage.setSearchCount(false);
        Page<Paper> resultPage = paperService.page(paperPage, new QueryWrapper<>());

        List<Paper> records = resultPage.getRecords();
        boolean hasNextPage = records.size() > pageSize;

        if (hasNextPage) {
            records = records.subList(0, pageSize);
        }

        List<PaperSummaryDTO> summaries = records.stream().map(p -> {
            PaperSummaryDTO dto = new PaperSummaryDTO();
            dto.setPaperId(p.getPaperId());
            dto.setTitle(p.getTitle());
            dto.setAuthors(p.getAuthors());
            dto.setSubmissionDate(p.getSubmissionDate());
            return dto;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", summaries);
        result.put("hasNext", hasNextPage);
        result.put("currentPage", pageNum);
        return result;
    }

    /**
     * 🎯 关键修复：搜索接口返回 MyBatis-Plus 兼容格式
     */
    @GetMapping("/search")
    public Page<PaperSummaryDTO> searchPapers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        logger.info("收到搜索请求: keyword={}, pageNum={}, pageSize={}", keyword, pageNum, pageSize);

        // 如果ES服务不可用，回退到数据库搜索
        if (elasticsearchService == null) {
            logger.warn("Elasticsearch 服务不可用，使用数据库搜索");
            return searchPapersFromDatabase(keyword, pageNum, pageSize);
        }

        try {
            // ES分页从0开始，需要转换
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                    Sort.by(Sort.Direction.DESC, "submissionDate"));

            org.springframework.data.domain.Page<PaperDocument> esPage =
                    elasticsearchService.searchPapers(keyword, pageable);

            logger.info("ES搜索结果: 共找到 {} 条记录", esPage.getTotalElements());

            // 🔥 关键：将ES的Page转换为MyBatis-Plus的Page格式
            Page<PaperSummaryDTO> mybatisPlusPage = new Page<>();

            // 设置分页信息（MyBatis-Plus格式）
            mybatisPlusPage.setCurrent(pageNum);  // 当前页（从1开始）
            mybatisPlusPage.setSize(pageSize);    // 每页大小
            mybatisPlusPage.setTotal(esPage.getTotalElements()); // 总记录数
            mybatisPlusPage.setPages((long) Math.ceil((double) esPage.getTotalElements() / pageSize)); // 总页数

            // 转换数据
            List<PaperSummaryDTO> summaries = esPage.getContent().stream().map(doc -> {
                PaperSummaryDTO dto = new PaperSummaryDTO();
                dto.setPaperId(doc.getPaperId());
                dto.setTitle(doc.getTitle());
                dto.setAuthors(doc.getAuthors());
                dto.setSubmissionDate(doc.getSubmissionDate());
                dto.setPdfLink(doc.getPdfLink());
                return dto;
            }).collect(Collectors.toList());

            mybatisPlusPage.setRecords(summaries);

            return mybatisPlusPage;

        } catch (Exception e) {
            logger.error("ES搜索失败，回退到数据库搜索", e);
            return searchPapersFromDatabase(keyword, pageNum, pageSize);
        }
    }

    /**
     * 数据库搜索（备用方案）
     */
    private Page<PaperSummaryDTO> searchPapersFromDatabase(String keyword, Integer pageNum, Integer pageSize) {
        logger.info("使用数据库搜索: keyword={}", keyword);

        QueryWrapper<Paper> wrapper = new QueryWrapper<>();
        wrapper.and(w -> w.like("title", keyword));

        Page<Paper> paperPage = paperService.page(new Page<>(pageNum, pageSize), wrapper);

        Page<PaperSummaryDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(paperPage, dtoPage, "records");

        List<PaperSummaryDTO> summaries = paperPage.getRecords().stream().map(p -> {
            PaperSummaryDTO dto = new PaperSummaryDTO();
            dto.setPaperId(p.getPaperId());
            dto.setTitle(p.getTitle());
            dto.setAuthors(p.getAuthors());
            dto.setSubmissionDate(p.getSubmissionDate());
            dto.setPdfLink(p.getPdfLink());
            return dto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(summaries);
        return dtoPage;
    }

    /**
     * 检查数据库记录数
     */
    @GetMapping("/count")
    public Map<String, Object> getDatabaseCount() {
        Map<String, Object> result = new HashMap<>();
        try {
            long count = paperService.count();
            result.put("database_count", count);
            result.put("status", "success");

            // 获取前5条数据看看
            List<Paper> samples = paperService.list(new QueryWrapper<Paper>().last("LIMIT 5"));
            result.put("sample_titles", samples.stream()
                    .map(Paper::getTitle)
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 检查ES服务状态
     */
    @GetMapping("/check-es")
    public Map<String, Object> checkES() {
        Map<String, Object> result = new HashMap<>();

        result.put("service_injected", elasticsearchService != null);

        if (elasticsearchService != null) {
            result.put("service_class", elasticsearchService.getClass().getName());
            result.put("message", "ES服务已注入");
        } else {
            result.put("message", "ES服务未注入");
        }

        return result;
    }

    /**
     * 强制重新同步所有数据
     */
    @PostMapping("/force-sync")
    public String forceSyncAllData() {
        try {
            logger.info("=== 开始强制同步所有数据 ===");

            if (elasticsearchService == null) {
                return "ERROR: Elasticsearch 服务未启用";
            }

            // 检查数据库数据
            long dbCount = paperService.count();
            logger.info("数据库中共有 {} 条记录", dbCount);

            if (dbCount == 0) {
                return "ERROR: 数据库中没有数据";
            }

            // 执行同步
            elasticsearchService.indexAllPapers();

            logger.info("=== 强制同步完成 ===");
            return "SUCCESS: 强制同步完成，数据库有 " + dbCount + " 条记录";

        } catch (Exception e) {
            logger.error("强制同步失败", e);
            return "ERROR: " + e.getMessage();
        }
    }


    // ===== 其他接口放在最后 =====

    @GetMapping("/{id}")
    public Paper getPaperById(@PathVariable Long id) {
        return paperService.getById(id);
    }

    @PostMapping
    public boolean addPaper(@RequestBody Paper paper) {
        boolean saved = paperService.save(paper);
        if (saved && elasticsearchService != null) {
            try {
                PaperDocument doc = PaperDocument.fromPaper(paper);
                elasticsearchService.indexPaper(doc);
            } catch (Exception e) {
                logger.warn("同步到ES失败", e);
            }
        }
        return saved;
    }

    @PutMapping
    public boolean updatePaper(@RequestBody Paper paper) {
        boolean updated = paperService.updateById(paper);
        if (updated && elasticsearchService != null) {
            try {
                PaperDocument doc = PaperDocument.fromPaper(paper);
                elasticsearchService.indexPaper(doc);
            } catch (Exception e) {
                logger.warn("同步到ES失败", e);
            }
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    public boolean deletePaper(@PathVariable Long id) {
        boolean deleted = paperService.removeById(id);
        if (deleted && elasticsearchService != null) {
            try {
                elasticsearchService.deletePaper(String.valueOf(id));
            } catch (Exception e) {
                logger.warn("从ES删除失败", e);
            }
        }
        return deleted;
    }


    /**
     * 调试：检查数据库中的数据
     */
    @GetMapping("/debug/count")
    public Map<String, Object> debugDatabaseCount() {
        Map<String, Object> result = new HashMap<>();
        try {
            long count = paperService.count();
            result.put("database_count", count);
            result.put("status", "success");

            // 获取前5条数据看看
            List<Paper> samples = paperService.list(new QueryWrapper<Paper>().last("LIMIT 5"));
            result.put("sample_titles", samples.stream()
                    .map(Paper::getTitle)
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 调试：简单的同步接口
     */
    @GetMapping("/debug/sync")
    public String debugSync() {
        try {
            logger.info("=== 开始调试同步 ===");

            if (elasticsearchService == null) {
                logger.error("elasticsearchService 为 null");
                return "ERROR: Elasticsearch 服务未注入";
            }

            logger.info("elasticsearchService 不为 null，开始同步");

            // 先检查数据库数据
            long dbCount = paperService.count();
            logger.info("数据库中共有 {} 条记录", dbCount);

            if (dbCount == 0) {
                return "ERROR: 数据库中没有数据";
            }

            // 执行同步
            elasticsearchService.indexAllPapers();

            logger.info("=== 同步完成 ===");
            return "SUCCESS: 同步完成，数据库有 " + dbCount + " 条记录";

        } catch (Exception e) {
            logger.error("同步失败", e);
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * 调试：检查ES服务状态
     */
    @GetMapping("/debug/es")
    public Map<String, Object> debugES() {
        Map<String, Object> result = new HashMap<>();

        result.put("service_injected", elasticsearchService != null);

        if (elasticsearchService != null) {
            result.put("service_class", elasticsearchService.getClass().getName());
        }

        return result;
    }
}