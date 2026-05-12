package org.zhangrui.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhangrui.common.result.Result;
import org.zhangrui.model.dto.UserLoginDTO;
import org.zhangrui.model.dto.UserRegisterDTO;
import org.zhangrui.model.dto.WxLoginDTO;
import org.zhangrui.model.vo.UserVO;
import org.zhangrui.service.IUserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/login")
    public Result<UserVO> login(@Valid @RequestBody UserLoginDTO dto) {
        return Result.success(userService.login(dto));
    }

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody UserRegisterDTO dto) {
        return Result.success(userService.register(dto));
    }

    @GetMapping("/info")
    public Result<UserVO> getUserInfo(@RequestParam Long userId) {
        return Result.success(userService.getUserInfo(userId));
    }

    @PostMapping("/wx-login")
    public Result<UserVO> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        return Result.success(userService.wxLogin(dto));
    }

    @PostMapping("/activate-owner")
    public Result<Void> activateOwner(@RequestParam Long userId, @RequestParam String activationCode) {
        userService.activateOwner(userId, activationCode);
        return Result.success("店主激活成功");
    }
}
