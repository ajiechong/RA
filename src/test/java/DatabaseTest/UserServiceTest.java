package DatabaseTest;

import cn.example.researchassistant.dao.impl.UserDaoImpl;
import cn.example.researchassistant.domain.User;
import cn.example.researchassistant.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;
    private UserDaoImpl userDaoImpl;
    private MockHttpServletRequest mockRequest;
    @BeforeEach
    public void setUp() {
        userDaoImpl = new UserDaoImpl();
        userService = new UserService();
        mockRequest = new MockHttpServletRequest();
    }
    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
    }
    @Test
    public void testGetUserById() {
        int uid = 1;
        User user = userService.getUserById(uid);
        if (user != null) {
            assertEquals(uid, user.getUid());
        } else {
            assertNull(user);
        }
    }
    @Test
    public void testGetUserByEmail() {
        String email = "test@example.com";
        User user = userService.getUserByEmail(email);
        if (user != null) {
            assertEquals(email, user.getEmail());
        } else {
            assertNull(user);
        }
    }
    @Test
    public void testAddUser() {
        User newUser = new User();
        newUser.setEmail("newtest@example.com");
        newUser.setUName("New Test User");
        newUser.setAvatarUser("test_avatar.jpg");
        newUser.setUserPSW("testpassword");

        User addedUser = userService.addUser(newUser);
        assertNotNull(addedUser);

        User retrievedUser = userService.getUserByEmail(newUser.getEmail());
        if (retrievedUser != null) {
            assertEquals(newUser.getEmail(), retrievedUser.getEmail());
            assertEquals(newUser.getUName(), retrievedUser.getUName());
            assertEquals(newUser.getAvatarUser(), retrievedUser.getAvatarUser());
        }
    }
    @Test
    public void testUpdateUserName() {
        // 设置请求参数
        mockRequest.setParameter("uid", "1");
        mockRequest.setParameter("newUserName", "Updated Test Name");

        String result = userService.updateUserName(mockRequest);
        assertNotNull(result);
    }
    @Test
    public void testUpdateUserEmail() {
        mockRequest.setParameter("uid", "1");
        mockRequest.setParameter("newemail", "updated@example.com");

        String result = userService.updateUserEmail(mockRequest);
        assertNotNull(result);
    }
    @Test
    public void testUpdateUserAvatar() {
        mockRequest.setParameter("uid", "1");
        mockRequest.setParameter("newAvatarUrl", "new_avatar.jpg");

        String result = userService.updateUserAvatar(mockRequest);
        assertNotNull(result);
    }
    @Test
    public void testUpdateUserPassword() {
        mockRequest.setParameter("uid", "1");
        mockRequest.setParameter("oldPassword", "oldpass");
        mockRequest.setParameter("newPassword", "newpass");

        String result = userService.updateUserPassword(mockRequest);
        assertNotNull(result);
    }
    @Test
    public void testInvalidPasswordUpdate() {
        mockRequest.setParameter("uid", "1");
        mockRequest.setParameter("oldPassword", (String) null);
        mockRequest.setParameter("newPassword", "newpass");

        String result = userService.updateUserPassword(mockRequest);
        assertTrue(result.contains("false"));
    }
    @Test
    public void testGetAvatarPathByUserId() {
        User testUser = new User();// 首先创建一个带有头像的测试用户
        testUser.setEmail("avatartest@example.com");
        testUser.setUName("Avatar Test User");
        testUser.setAvatarUser("test_avatar.jpg");
        User addedUser = userService.addUser(testUser);
        assertNotNull(addedUser);
        String avatarPath = userService.getAvatarPathByUserId(addedUser.getUid());
        assertNotNull(avatarPath);
        assertTrue(avatarPath.contains("test_avatar.jpg") || avatarPath.contains("未设置头像"));
    }
}