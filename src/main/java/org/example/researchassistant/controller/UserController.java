package org.example.researchassistant.controller;
import org.example.researchassistant.domain.User;
import org.example.researchassistant.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:8081")  // 前端地址
@RestController
@RequestMapping("/api/login")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // 正则表达式检查是否为有效邮箱
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    @Autowired
    private JavaMailSender mailSender;  // 用于发送邮件

    // 用于存储邮箱和验证码的映射，通常可以存储在数据库中
    private static final Map<String, String> emailVerificationCache = new HashMap<>();


    //处理用户邮箱验证发送验证码的请求
    @PostMapping("/register")
    public String sendVerificationCode(@RequestParam("email") String email) {
        // 验证邮箱格式
        if (!isValidEmailFormat(email)) {
            logger.error("无效的邮箱格式: {}", email);
            return "{\"success\": false, \"message\": \"无效的邮箱格式\"}";
        }

        // 生成随机验证码
        String verificationCode = generateVerificationCode();
        logger.info("为邮箱 {} 生成验证码: {}", email, verificationCode);

        // 保存验证码到缓存（通常保存到数据库中）
        emailVerificationCache.put(email, verificationCode);

        // 发送验证码到用户邮箱
        try {
            sendEmail(email, "邮箱验证", "您的验证码是：" + verificationCode);
            return "{\"success\": true, \"message\": \"验证码已发送，请检查您的邮箱\"}";
        } catch (Exception e) {
            logger.error("发送验证码邮件失败: {}", e.getMessage());
            return "{\"success\": false, \"message\": \"发送验证码失败，请稍后再试\"}";
        }
    }


    //用于发送邮件

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1728109414@qq.com");  // 发件人邮箱
        message.setTo(to);  // 收件人邮箱
        message.setSubject(subject);
        message.setText(text);

        // 发送邮件
        mailSender.send(message);
    }

    //用于验证邮箱格式是否有效

    private boolean isValidEmailFormat(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // 生成一个随机的验证码

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);  // 生成六位数验证码
        return String.valueOf(code);
    }
    @PostMapping("/login-by-code")
    public String loginByCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        // 1. 验证邮箱格式
        if (!isValidEmailFormat(email)) {
            logger.error("无效的邮箱格式: {}", email);
            return "{\"success\": false, \"message\": \"无效的邮箱格式\"}";
        }

        // 2. 验证验证码
        String storedCode = emailVerificationCache.get(email);

        if (storedCode == null) {
            return "{\"success\": false, \"message\": \"未发送验证码或验证码已过期\"}";
        }

        // 3. 检查验证码是否正确
        if (!storedCode.equals(code)) {
            return "{\"success\": false, \"message\": \"验证码错误\"}";
        }

        // 4. 根据邮箱获取用户信息，使用 fetchUser 方法
        User user = fetchUser(email); // 使用 fetchUser 方法来获取用户信息
        if (user.getUid()==0) {
            // 如果用户不存在，进行自动注册
            logger.info("用户 {} 不存在，开始自动注册", email);

            // 自动生成一个用户名（例如：基于邮箱生成一个默认用户名，或者也可以让用户设置）
            String defaultUsername = "user_" + System.currentTimeMillis(); // 生成一个默认的用户名

            // 创建新的用户对象
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUName(defaultUsername);  // 使用默认用户名
            newUser.setAvatarUser("https://tse3-mm.cn.bing.net/th/id/OIP-C.yp-D-KHI3e2nN4eMBJcEVAAAAA?rs=1&pid=ImgDetMain");
            newUser.setUserPSW("123456"); // 设置默认密码为 123456

            // 添加新用户
//            userService.addUser(newUser);

            User savedUser = userService.addUser(newUser);

            // 返回注册成功的消息
            return "{\"success\": true, \"message\": \"验证码验证成功，用户不存在，已自动注册\", \"user\": {\"uid\": " + savedUser.getUid() + ", \"username\": \"" + savedUser.getUName() + "\", \"email\": \"" + savedUser.getEmail() + "\"}}";
        }

        // 5. 如果用户存在，返回成功并包含用户信息
        return "{\"success\": true, \"message\": \"登录成功\", \"user\": {\"uid\": " + user.getUid() + ", \"username\": \"" + user.getUName() + "\", \"email\": \"" + user.getEmail() + "\"}}";
    }

    // 处理获取用户信息的 GET 请求

    @GetMapping("/profile")
    public User getUserByGet(@RequestParam("identifier") String identifier) {
        logger.info("GET 请求: 获取用户信息，传入标识符: {}", identifier);
        return fetchUser(identifier);
    }

    // 处理获取用户信息的 POST 请求

    @PostMapping("/profile")
    public User getUserByPost(@RequestParam("identifier") String identifier) {
        logger.info("POST 请求: 获取用户信息，传入标识符: {}", identifier);
        return fetchUser(identifier);
    }

    // 统一的获取用户信息逻辑

    private User fetchUser(String identifier) {
        User user = null;

        try {
            if (isValidEmail(identifier)) {
                // 使用邮箱验证
                logger.info("标识符识别为邮箱，开始根据邮箱获取用户信息，邮箱: {}", identifier);
                user = userService.getUserByEmail(identifier);
            } else {
                // 使用用户ID验证
                int userId = Integer.parseInt(identifier); // 确保 identifier 可转为整数
                logger.info("标识符识别为用户ID，开始根据用户ID获取用户信息，用户ID: {}", userId);
                user = userService.getUserById(userId);
            }

            if (user == null) {
                logger.error("未找到用户，标识符: {}", identifier);
                throw new RuntimeException("用户不存在");
            }

            // 打印用户信息，确保所有字段（如 UName）都包含在内
            logger.info("用户信息：Uid = {}, UName = {}, Email = {}, Avatar = {}", user.getUid(), user.getUName(), user.getEmail(), user.getAvatarUser());

        } catch (NumberFormatException e) {
            logger.error("无效的用户ID格式: {}", identifier, e);
            throw new RuntimeException("无效的用户ID格式");
        } catch (Exception e) {
            logger.error("获取用户信息失败，标识符: {}", identifier, e);
            throw new RuntimeException("获取用户信息失败");
        }

        return user;
    }

    // 用于验证邮箱格式是否有效

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    //用户密码验证接口（这个部分与获取用户信息的部分无关，但也涉及到用户名和密码验证）

    @PostMapping("/verify-password")
    public String verifyPassword(@RequestParam("identifier") String identifier,
                                 @RequestParam("password") String password) {

        logger.info("开始验证密码，传入标识符: {}", identifier);
        logger.info("开始验证密码，传入密码: {}", password);

        try {
            User user;

            // 判断是否为有效邮箱
            if (isValidEmail(identifier)) {
                // 使用邮箱验证密码
                logger.info("识别为邮箱，开始根据邮箱验证密码，邮箱: {}", identifier);
                user = userService.getUserByEmail(identifier);
                logger.info("用户名: {}", user.getUName());
            } else {
                // 使用用户ID验证密码
                try {
                    int userId = Integer.parseInt(identifier);
                    logger.info("识别为用户ID，开始根据用户ID验证密码，用户ID: {}", userId);
                    user = userService.getUserById(userId);
                } catch (NumberFormatException e) {
                    logger.error("无效的标识符: {}", identifier);
                    return "{\"success\": false, \"message\": \"无效的标识符\"}";
                }
            }

            if (user == null) {
                logger.error("未找到用户，标识符: {}", identifier);
                return "{\"success\": false, \"message\": \"用户不存在\"}";
            }

            String storedPassword = user.getUserPSW();
            logger.debug("从数据库中获取的存储密码: {}", storedPassword);

            // 比较输入的密码和存储的密码
            if (storedPassword != null && storedPassword.equals(password)) {
                logger.info("密码验证成功，标识符: {}", identifier);
                return "{\"success\": true, \"message\": \"密码验证成功\"}";
            } else {
                logger.warn("密码验证失败，标识符: {}, 提供的密码: {}", identifier, password);
                return "{\"success\": false, \"message\": \"密码错误\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("密码验证过程中出错，标识符: {}", identifier, e);
            return "{\"success\": false, \"message\": \"验证过程中出错\"}";
        }
    }

    // 更新用户名称和头像
    @PostMapping("/change-password")
    public String changePassword(@RequestParam(value = "uid") int uid,
                                 @RequestParam(value = "oldPassword", required = false) String oldPassword,
                                 @RequestParam(value = "newPassword", required = false) String newPassword,
                                 HttpServletRequest request) {
        try {
            // 调用 userService 中的 updateUserPassword 方法进行密码更新
            return userService.updateUserPassword(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"更改密码时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }
    @PostMapping("/update")
    public String updateUser(@RequestParam(value = "uid")int uid,
                             @RequestParam(value = "newUserName", required = false) String newUserName,
                             @RequestParam(value = "newAvatarUrl", required = false) String newAvatarUrl,
                             HttpServletRequest request) {
        try {
            // 创建结果响应对象
            StringBuilder responseMessage = new StringBuilder("{");
            System.out.println("头像链接: "+newAvatarUrl);
            boolean nameUpdated = false;
            boolean avatarUpdated = false;
            String avatarUrl = null;

            // 如果新用户名不为空，则更新用户名
            if (newUserName != null && !newUserName.isEmpty()) {
                String nameUpdateResponse = userService.updateUserName(request);
                nameUpdated = nameUpdateResponse.contains("用户名称更新成功");
                responseMessage.append("\"nameUpdated\": ").append(nameUpdated).append(",");
            }

            // 如果新头像 URL 不为空，则更新头像
            if (newAvatarUrl != null && !newAvatarUrl.isEmpty()) {
                // 调用 updateUserAvatar 来更新头像
                String avatarUpdateResponse = userService.updateUserAvatar(request);

                // 判断头像是否更新成功
                avatarUpdated = avatarUpdateResponse.contains("用户头像地址更新成功");

                // 获取更新后的头像 URL
                if (avatarUpdated) {
                    avatarUrl = newAvatarUrl;
                }

                responseMessage.append("\"avatarUpdated\": ").append(avatarUpdated).append(",");
            }

            // 去除多余的逗号
            if (responseMessage.charAt(responseMessage.length() - 1) == ',') {
                responseMessage.deleteCharAt(responseMessage.length() - 1);
            }

            responseMessage.append("}");

            // 如果头像更新成功，返回新的头像 URL
            if (avatarUpdated) {
                responseMessage.append(", \"avatarUrl\": \"").append(avatarUrl).append("\"");
            }

            return responseMessage.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"更新用户信息时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    @Value("${api.base-url}")  // 从配置文件读取
    private String apiBaseUrl;

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatarFile") MultipartFile file,
                                          @RequestParam("uid") int uid) {
        try {
            // 设置上传目录为 src/front_end/src/assets/imgs/avatar
            String uploadDir = "src/front_end/src/assets/imgs/avatar/"; // 设置新的上传目录

            // 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(originalFilename); // 获取文件扩展名

            // 使用 UID + 当前时间来生成唯一的文件名
            String fileName = uid + "-" + System.currentTimeMillis() + "." + fileExtension;

            // 创建目标路径
            Path path = Paths.get(uploadDir + fileName);

            // 确保目标目录存在，如果不存在则创建
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // 创建目录
            }

            // 获取当前用户的旧头像路径
            String oldAvatarUrl = userService. getAvatarPathByUserId(uid);  // 调用 UserService 方法获取头像路径


            // 打印旧头像路径，检查是否正确
            System.out.println("Old Avatar URL: " + oldAvatarUrl);

            // 如果旧头像存在，删除它
            if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                // 获取旧头像文件路径（假设旧头像路径为 http://localhost:8080/uploads/xxxx.jpg）
                String oldAvatarPath = "src/front_end/src/assets/imgs/avatar/" + oldAvatarUrl.substring(oldAvatarUrl.lastIndexOf("/") + 1);
                System.out.println("Old Avatar Path: " + oldAvatarPath); // 打印旧头像文件路径，检查是否正确
                File oldAvatarFile = new File(oldAvatarPath);
                if (oldAvatarFile.exists()) {
                    boolean deleted = oldAvatarFile.delete();  // 删除旧头像
                    if (deleted) {
                        System.out.println("Old avatar deleted successfully.");
                    } else {
                        System.out.println("Failed to delete old avatar.");
                    }
                } else {
                    System.out.println("Old avatar file not found.");
                }
            }

            // 将新头像文件保存到指定路径
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 返回文件的 URL
            String fileUrl = apiBaseUrl+"/uploads/" + fileName;

            // 使用 HashMap 来存储返回的数据
            Map<String, String> response = new HashMap<>();
            response.put("fileUrl", fileUrl);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("文件上传失败");
        }
    }

    @PostMapping("/verify-code")
    public String changEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
        // 1. 验证邮箱格式
        if (!isValidEmailFormat(email)) {
            logger.error("无效的邮箱格式: {}", email);
            return "{\"success\": false, \"message\": \"无效的邮箱格式\"}";
        }

        // 2. 验证验证码
        String storedCode = emailVerificationCache.get(email);

        if (storedCode == null) {
            return "{\"success\": false, \"message\": \"未发送验证码或验证码已过期\"}";
        }

        // 3. 检查验证码是否正确
        if (!storedCode.equals(code)) {
            return "{\"success\": false, \"message\": \"验证码错误\"}";
        }

        return "{\"success\": true, \"message\": \"验证码通过\"}";
    }

    // 更新用户名称和头像
    @PostMapping("/change-email")
    public String changeEmail(@RequestParam(value = "uid") String uid,
                              @RequestParam(value = "newemail", required = false) String newemail,
                              HttpServletRequest request) {
        try {

            // 调用 userService 中的 updateUserPassword 方法进行密码更新
            return userService.updateUserEmail(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"更改邮箱时出错\", \"details\": \"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/verify-email")
    public String getEmail(@RequestParam(value = "newemail") String newemail){
        User user = fetchUser( newemail); // 使用 fetchUser 方法来获取用户信息
        if(user.getUid()!=0){
            return "{\"success\": false, \"message\": \"邮箱已经注册\"}";
        }
        return "{\"success\": true, \"message\": \"邮箱已经注册\"}";
    }
}
