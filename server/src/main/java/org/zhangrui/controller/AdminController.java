package org.zhangrui.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhangrui.common.result.Result;
import org.zhangrui.model.dto.AdminLoginDTO;
import org.zhangrui.model.vo.AdminVO;
import org.zhangrui.service.IAdminService;

/**
 * 管理员控制器
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IAdminService adminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<AdminVO> login(@Valid @RequestBody AdminLoginDTO dto) {
        AdminVO adminVO = adminService.login(dto);
        return Result.success(adminVO);
    }

    /**
     * 获取管理员信息
     */
    @GetMapping("/info")
    public Result<AdminVO> getAdminInfo(@RequestParam Long adminId) {
        AdminVO adminVO = adminService.getAdminInfo(adminId);
        return Result.success(adminVO);
    }
}
