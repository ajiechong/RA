package org.example.researchassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.researchassistant.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
