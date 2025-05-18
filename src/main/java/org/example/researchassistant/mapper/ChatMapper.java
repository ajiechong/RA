package org.example.researchassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    // 不需要写任何方法，常见的增删改查 MyBatis-plus 都自动支持
}
