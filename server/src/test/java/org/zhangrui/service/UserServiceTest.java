package org.zhangrui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.dto.UserLoginDTO;
import org.zhangrui.model.dto.UserRegisterDTO;
import org.zhangrui.model.dto.WxLoginDTO;
import org.zhangrui.model.vo.UserVO;

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
        UserVO userVO = userService.getUserInfo(1L);

        assertNotNull(userVO);
        assertNotNull(userVO.getId());
    }

    @Test
    @Order(4)
    @DisplayName("测试获取用户信息（不存在）")
    public void testGetUserInfoNotFound() {
        assertThrows(BusinessException.class, () -> userService.getUserInfo(99999L));
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
    @DisplayName("测试用户注册（用户名已存在）")
    public void testUserRegisterDuplicateUsername() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("duplicate_user");
        dto.setPassword("password123");
        dto.setNickname("测试用户1");

        // 第一次注册成功
        UserVO userVO = userService.register(dto);
        assertNotNull(userVO);

        // 第二次注册应失败
        dto.setNickname("测试用户2");
        assertThrows(BusinessException.class, () -> userService.register(dto));
    }
}
