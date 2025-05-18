package org.example.researchassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.researchassistant.entity.User;
import org.example.researchassistant.entity.UserResource;
import org.example.researchassistant.mapper.UserMapper;
import org.example.researchassistant.mapper.UserResourceMapper;
import org.example.researchassistant.service.UserResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserResourceServiceImpl extends ServiceImpl<UserResourceMapper, UserResource> implements UserResourceService {
}
