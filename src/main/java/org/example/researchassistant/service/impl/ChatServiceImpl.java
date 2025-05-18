package org.example.researchassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.researchassistant.entity.User;
import org.example.researchassistant.mapper.ChatHistoryMapper;
import org.example.researchassistant.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {
}
