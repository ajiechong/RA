package org.example.researchassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.researchassistant.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
