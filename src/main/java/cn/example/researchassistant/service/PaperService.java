package cn.example.researchassistant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.researchassistant.entity.Paper;

public interface PaperService extends IService<Paper> {
    Page<Paper> getPapersWithPage(Integer pageNum, Integer pageSize);
}
