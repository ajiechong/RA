package org.example.researchassistant.controller;

import org.example.researchassistant.entity.User;
import org.example.researchassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public boolean createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping
    public boolean updateUser(@RequestBody User user) {
        return userService.updateById(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.removeById(id);
    }
}
