package org.example.researchassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public List<Chat> getAllChats() {
        return chatService.list();
    }

    @GetMapping("/{id}")
    public Chat getChatById(@PathVariable Long id) {
        return chatService.getById(id);
    }

    @PostMapping
    public boolean addChat(@RequestBody Chat chat) {
        return chatService.save(chat);
    }

    @PutMapping
    public boolean updateChat(@RequestBody Chat chat) {
        return chatService.updateById(chat);
    }

    @DeleteMapping("/{id}")
    public boolean deleteChat(@PathVariable Long id) {
        return chatService.removeById(id);
    }
}
