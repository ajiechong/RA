package org.example.researchassistant.dao;

import org.example.researchassistant.domain.User;
import java.util.List;

public interface UserDao {
    // 获取所有用户记录
    List<User> getAllUsers();

    // 根据用户ID获取特定用户记录
    User getUserById(int uid);

    // 根据用户邮箱获取特定用户记录
    User getUserByEmail(String email);

    // 插入新的用户记录
    void insertUser(User user);

    // 更新用户记录
    void updateUser(User user, String condition);

    // 删除用户记录
    void deleteUser(String condition);
}