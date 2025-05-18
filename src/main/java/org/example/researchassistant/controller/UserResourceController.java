package org.example.researchassistant.controller;

import org.example.researchassistant.entity.UserResource;
import org.example.researchassistant.service.UserResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-resources")
public class UserResourceController {

    @Autowired
    private UserResourceService userResourceService;

    @GetMapping
    public List<UserResource> getAllUserResources() {
        return userResourceService.list();
    }

    @GetMapping("/{id}")
    public UserResource getUserResourceById(@PathVariable Long id) {
        return userResourceService.getById(id);
    }

    @PostMapping
    public boolean addUserResource(@RequestBody UserResource userResource) {
        return userResourceService.save(userResource);
    }

    @PutMapping
    public boolean updateUserResource(@RequestBody UserResource userResource) {
        return userResourceService.updateById(userResource);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUserResource(@PathVariable Long id) {
        return userResourceService.removeById(id);
    }
}
