package org.zhangrui.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.dto.AdminLoginDTO;
import org.zhangrui.model.dto.AdminUpdateDTO;
import org.zhangrui.model.vo.AdminVO;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理员服务测试类
 * 覆盖管理员登录、查询等核心功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminServiceTest {

    @Autowired
    private IAdminService adminService;

    @Test
    @Order(1)
    @DisplayName("测试管理员登录")
    public void testAdminLogin() {
        AdminLoginDTO dto = new AdminLoginDTO();
        dto.setUsername("admin");
        dto.setPassword("admin123");

        AdminVO adminVO = adminService.login(dto);

        assertNotNull(adminVO);
        assertNotNull(adminVO.getToken());
        assertEquals("admin", adminVO.getUsername());
    }

    @Test
    @Order(2)
    @DisplayName("测试管理员登录（密码错误）")
    public void testAdminLoginWrongPassword() {
        AdminLoginDTO dto = new AdminLoginDTO();
        dto.setUsername("admin");
        dto.setPassword("wrong_password");

        assertThrows(BusinessException.class, () -> adminService.login(dto));
    }

    @Test
    @Order(3)
    @DisplayName("测试管理员登录（用户不存在）")
    public void testAdminLoginUserNotFound() {
        AdminLoginDTO dto = new AdminLoginDTO();
        dto.setUsername("nonexistent");
        dto.setPassword("password");

        assertThrows(BusinessException.class, () -> adminService.login(dto));
    }

    @Test
    @Order(4)
    @DisplayName("测试获取管理员信息")
    public void testGetAdminInfo() {
        AdminVO adminVO = adminService.getAdminInfo(1L);

        if (adminVO != null) {
            assertNotNull(adminVO.getUsername());
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试获取管理员信息（不存在）")
    public void testGetAdminInfoNotFound() {
        assertThrows(BusinessException.class, () -> adminService.getAdminInfo(99999L));
    }

    @Test
    @Order(6)
    @DisplayName("测试更新管理员信息")
    public void testUpdateAdmin() {
        AdminUpdateDTO dto = new AdminUpdateDTO();
        dto.setUsername("admin_updated");
        dto.setPhone("13800138000");

        assertDoesNotThrow(() -> adminService.updateAdmin(1L, dto));
    }

    @Test
    @Order(7)
    @DisplayName("测试更新管理员密码")
    public void testUpdateAdminPassword() {
        assertDoesNotThrow(() -> adminService.updatePassword(1L, "admin123", "new_password"));
    }

    @Test
    @Order(8)
    @DisplayName("测试更新管理员密码（原密码错误）")
    public void testUpdateAdminPasswordWrongOldPassword() {
        assertThrows(BusinessException.class, () -> 
            adminService.updatePassword(1L, "wrong_password", "new_password"));
    }
}
