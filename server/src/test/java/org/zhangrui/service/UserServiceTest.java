package org.zhangrui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.dto.*;
import org.zhangrui.model.entity.Cart;
import org.zhangrui.model.entity.Product;
import org.zhangrui.model.enums.OrderStatus;
import org.zhangrui.model.vo.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试类
 * 覆盖用户注册、登录、查询等核心功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    private static Long createdUserId;

    @Test
    @Order(1)
    @DisplayName("测试用户注册")
    public void testUserRegister() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("test_user_" + System.currentTimeMillis());
        dto.setPassword("password123");
        dto.setPhone("13800138000");
        dto.setNickname("测试用户");

        UserVO userVO = userService.register(dto);

        assertNotNull(userVO);
        assertNotNull(userVO.getId());
        assertEquals(dto.getUsername(), userVO.getUsername());
        assertEquals(dto.getNickname(), userVO.getNickname());
        assertNull(userVO.getToken());
        createdUserId = userVO.getId();
    }

    @Test
    @Order(2)
    @DisplayName("测试用户登录")
    public void testUserLogin() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setOpenid("test_openid_" + System.currentTimeMillis());
        dto.setNickname("微信用户");

        UserVO userVO = userService.login(dto);

        assertNotNull(userVO);
        assertNotNull(userVO.getToken());
        assertEquals(dto.getOpenid(), userVO.getUsername());
    }

    @Test
    @Order(3)
    @DisplayName("测试获取用户信息")
    public void testGetUserInfo() {
        if (createdUserId != null) {
            UserVO userVO = userService.getUserInfo(createdUserId);
            assertNotNull(userVO);
            assertEquals(createdUserId, userVO.getId());
        } else {
            // 如果没有创建用户，测试获取不存在的用户
            assertThrows(BusinessException.class, () -> userService.getUserInfo(99999L));
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试获取用户列表")
    public void testGetUserList() {
        Page<UserVO> page = userService.getUserList(1, 10, null);

        assertNotNull(page);
        assertNotNull(page.getRecords());
        assertTrue(page.getRecords().size() >= 0);
    }

    @Test
    @Order(5)
    @DisplayName("测试微信登录")
    public void testWxLogin() {
        WxLoginDTO dto = new WxLoginDTO();
        dto.setCode("test_wx_code");

        UserVO userVO = userService.wxLogin(dto);

        assertNotNull(userVO);
        assertNotNull(userVO.getToken());
    }

    @Test
    @Order(6)
    @DisplayName("测试更新用户状态")
    public void testUpdateUserStatus() {
        if (createdUserId != null) {
            assertDoesNotThrow(() -> userService.updateUserStatus(createdUserId, 1));
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试激活店主")
    public void testActivateOwner() {
        if (createdUserId != null) {
            assertThrows(BusinessException.class, () -> 
                userService.activateOwner(createdUserId, "invalid_code"));
        }
    }
}
