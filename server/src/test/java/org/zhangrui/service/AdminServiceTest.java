package org.zhangrui.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.model.dto.AdminLoginDTO;
import org.zhangrui.model.vo.AdminVO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminServiceTest {

    @Autowired
    private IAdminService adminService;

    @Test
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
    public void testGetAdminInfo() {
        AdminVO adminVO = adminService.getAdminInfo(1L);

        if (adminVO != null) {
            assertNotNull(adminVO.getUsername());
        }
    }
}
