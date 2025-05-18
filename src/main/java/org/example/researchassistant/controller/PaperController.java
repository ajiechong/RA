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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/papers")
@CrossOrigin(origins = "*")
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

        Page<Paper> paperPage = paperService.getPapersWithPage(pageNum, pageSize);

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

    @GetMapping("/search")
    public Page<PaperSummaryDTO> searchPapers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        QueryWrapper<Paper> wrapper = new QueryWrapper<>();
        wrapper.like("title", keyword);

        Page<Paper> paperPage = paperService.page(new Page<>(pageNum, pageSize), wrapper);

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
