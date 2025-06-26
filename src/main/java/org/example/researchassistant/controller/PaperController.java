package org.example.researchassistant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.researchassistant.dto.PaperDTO;
import org.example.researchassistant.dto.PaperSummaryDTO;
import org.example.researchassistant.entity.Paper;
import org.example.researchassistant.service.PaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/papers")
//@CrossOrigin(origins = "*")
public class PaperController {

    @Autowired
    private PaperService paperService;

    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);

    /**
     * 分页获取论文列表
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页数量，默认10
     * @return 分页结果
     */
    @GetMapping
    public Page<PaperSummaryDTO> getPapersSummary(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        QueryWrapper<Paper> qw = new QueryWrapper<>();
        qw.select("paper_id", "title", "authors", "submission_date")
                .orderByDesc("submission_date");

        Page<Paper> paperPage = paperService.page(
                new Page<>(pageNum, pageSize, false),   // ❗跳过 COUNT(*)
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

    @GetMapping("/fast")
    public Map<String, Object> getPapersFast(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        // 多查一条，判断是否还有下一页
        Page<Paper> paperPage = new Page<>(pageNum, pageSize + 1);
        paperPage.setSearchCount(false); // 不查 COUNT(*)
        Page<Paper> resultPage = paperService.page(paperPage, new QueryWrapper<>());

        List<Paper> records = resultPage.getRecords();
        boolean hasNextPage = records.size() > pageSize;

        // 去掉多查的那一条
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


    @GetMapping("/search")
    public Page<PaperSummaryDTO> searchPapers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

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


    @GetMapping("/{id}")
    public Paper getPaperById(@PathVariable Long id) {
        return paperService.getById(id);
    }

    @PostMapping
    public boolean addPaper(@RequestBody Paper paper) {
        return paperService.save(paper);
    }

    @PutMapping
    public boolean updatePaper(@RequestBody Paper paper) {
        return paperService.updateById(paper);
    }

    @DeleteMapping("/{id}")
    public boolean deletePaper(@PathVariable Long id) {
        return paperService.removeById(id);
    }
}