package org.example.researchassistant.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.researchassistant.entity.Paper;
import org.example.researchassistant.mapper.PaperMapper;
import org.example.researchassistant.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
    @Override
    public Page<Paper> getPapersWithPage(Integer pageNum, Integer pageSize) {
        Page<Paper> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectPage(page, null);
    }
}