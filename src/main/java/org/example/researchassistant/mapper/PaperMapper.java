package org.example.researchassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.researchassistant.entity.Paper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.researchassistant.entity.User;

@Mapper
public interface PaperMapper extends BaseMapper<Paper> {
}
