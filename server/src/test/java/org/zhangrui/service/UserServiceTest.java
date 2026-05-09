package org.zhangrui.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.model.dto.UserLoginDTO;
import org.zhangrui.model.vo.UserVO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testUserLogin() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setOpenid("test_openid_123");
        dto.setNickname("测试用户");

        UserVO userVO = userService.login(dto);

        assertNotNull(userVO);
        assertEquals("test_openid_123", userVO.getOpenid());
    }

    @Test
    public void testGetUserInfo() {
        UserVO userVO = userService.getUserInfo(1L);

        if (userVO != null) {
            assertNotNull(userVO.getOpenid());
        }
    }
}
