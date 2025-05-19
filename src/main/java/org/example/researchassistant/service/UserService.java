package org.example.researchassistant.service;

import org.example.researchassistant.dao.UserDao;
import org.example.researchassistant.dao.impl.UserDaoImpl;
import org.example.researchassistant.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
@Service
public class UserService {

    private UserDao userDao = new UserDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    // 初始化ObjectMapper，设置序列化时的一些特性，比如日期格式等（可根据实际需求调整）
    public UserService() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }


    //获取头像
    public String getAvatarPathByUserId(int uid) {
        User user = getUserById(uid);  // 获取用户对象
        if (user != null) {
            // 获取用户的头像路径
            String avatarUrl = user.getAvatarUser(); // 获取用户的头像 URL（假设 avatarUrl 是 User 类中的字段）

            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                // 返回头像路径
                return avatarUrl;
            } else {
                // 如果头像路径为空，返回提示信息
                return "用户 " + uid + " 未设置头像，请上传头像";
            }
        } else {
            // 用户不存在的情况
            return "未找到ID为 " + uid + " 的用户，请检查输入的用户ID是否正确";
        }
    }



    // 获取所有用户记录
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    // 根据用户ID获取特定用户记录
    public User getUserById(int uid) {
        return userDao.getUserById(uid);
    }

    // 根据用户邮箱获取特定用户记录
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    // 添加新的用户记录
    public User addUser(User user) {
        userDao.insertUser(user);
        return user;  // 现在 user 对象已经包含了数据库生成的 uid
    }

    // 更新用户记录
    public void updateUser(User user, String condition) {
        userDao.updateUser(user, condition);
    }


    // 从HttpServletRequest中获取用户ID并更新用户名称，返回操作结果的JSON报文
    public String updateUserName(HttpServletRequest request) {
        try {
            int uid = Integer.parseInt(request.getParameter("uid"));
            String newUserName = request.getParameter("newUserName");

            User user = getUserById(uid);
            if (user!= null) {
                user.setUName(newUserName);
                // 这里可以添加一些额外的逻辑，比如记录操作日志，假设已经有一个log方法用于记录日志
                log("用户 " + uid + " 更新了名称为 " + newUserName, request);
                updateUser(user, "Uid = " + uid);
                return "{\"success\": true, \"message\": \"用户名称更新成功\"}";
            } else {
                return "{\"success\": false, \"error\": \"未找到ID为 " + uid + " 的用户，无法更新名称\", \"details\": \"请检查输入的用户ID是否正确\"}";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"error\": \"更新用户名称时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 从HttpServletRequest中获取用户ID并更新用户邮箱，返回操作结果的JSON报文
    public String updateUserEmail(HttpServletRequest request) {
        try {
            String Uid=request.getParameter("uid");
            log("用户 " + Uid,request);
            int uid = Integer.parseInt(request.getParameter("uid"));

            String newEmail = request.getParameter("newemail");

            User user = getUserById(uid);
            if (user!= null) {
                user.setEmail(newEmail);
                // 记录操作日志
                log("用户 " + uid + " 更新了邮箱为 " + newEmail, request);
                updateUser(user, "Uid = " + uid);
                return "{\"success\": true, \"message\": \"用户邮箱更新成功\"}";
            } else {
                return "{\"success\": false, \"error\": \"未找到ID为 " + uid + " 的用户，无法更新邮箱\", \"details\": \"请检查输入的用户ID是否正确\"}";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"error\": \"更新用户邮箱时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 从HttpServletRequest中获取用户ID并更新用户头像地址，返回操作结果的JSON报文
    public String updateUserAvatar(HttpServletRequest request) {
        try {
            int uid = Integer.parseInt(request.getParameter("uid"));
            String newAvatarUrl = request.getParameter("newAvatarUrl");

            User user = getUserById(uid);
            if (user!= null) {
                user.setAvatarUser(newAvatarUrl);
                // 记录操作日志
                log("用户 " + uid + " 更新了头像地址为 " + newAvatarUrl, request);
                updateUser(user, "Uid = " + uid);
                return "{\"success\": true, \"message\": \"用户头像地址更新成功\"}";
            } else {
                return "{\"success\": false, \"error\": \"未找到ID为 " + uid + " 的用户，无法更新头像地址\", \"details\": \"请检查输入的用户ID是否正确\"}";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"error\": \"更新用户头像地址时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    // 模拟记录操作日志的方法，这里只是简单打印，实际应用中可能需要将日志记录到文件或数据库等
    private void log(String message, HttpServletRequest request) {
        System.out.println("操作日志: " + message + "，来源IP: " + request.getRemoteAddr());
    }
    public String updateUserPassword(HttpServletRequest request) {
        try {
            // 打印所有请求参数
            System.out.println("请求参数: " + request.getParameterMap());

            // 获取用户ID和新旧密码
            String uidParam = request.getParameter("uid");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");

            // 打印获取到的具体参数
            System.out.println("用户ID: " + uidParam);
            System.out.println("旧密码: " + oldPassword);
            System.out.println("新密码: " + newPassword);

            // 校验参数是否为空
            if (uidParam == null || uidParam.isEmpty()) {
                return "{\"success\": false, \"error\": \"用户ID不能为空\", \"details\": \"请提供有效的用户ID\"}";
            }

            if (oldPassword == null || oldPassword.isEmpty()) {
                return "{\"success\": false, \"error\": \"旧密码不能为空\", \"details\": \"请提供旧密码\"}";
            }

            if (newPassword == null || newPassword.isEmpty()) {
                return "{\"success\": false, \"error\": \"新密码不能为空\", \"details\": \"请提供新密码\"}";
            }

            // 将用户ID从字符串转换为整数
            int uid = Integer.parseInt(uidParam);

            // 获取用户对象
            User user = getUserById(uid);
            if (user != null) {
                // 校验旧密码是否正确
                if (user.getUserPSW().equals(oldPassword)) {
                    // 更新密码
                    user.setUserPSW(newPassword);
                    // 记录操作日志
                    log("用户 " + uid + " 更新了密码", request);
                    updateUser(user, "Uid = " + uid);

                    return "{\"success\": true, \"message\": \"用户密码更新成功\"}";
                } else {
                    return "{\"success\": false, \"error\": \"旧密码不正确\", \"details\": \"请检查输入的旧密码\"}";
                }
            } else {
                return "{\"success\": false, \"error\": \"未找到ID为 " + uid + " 的用户，无法更新密码\", \"details\": \"请检查输入的用户ID是否正确\"}";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "{\"success\": false, \"error\": \"用户ID格式错误，无法解析为数字\", \"details\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"error\": \"更新用户密码时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

}