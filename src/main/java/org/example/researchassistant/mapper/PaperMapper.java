package org.example.researchassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.researchassistant.entity.Paper;

@Mapper
public interface PaperMapper extends BaseMapper<Paper> {
}
