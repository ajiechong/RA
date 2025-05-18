package org.example.researchassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.researchassistant.entity.User;
import org.example.researchassistant.mapper.UserMapper;
import org.example.researchassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
