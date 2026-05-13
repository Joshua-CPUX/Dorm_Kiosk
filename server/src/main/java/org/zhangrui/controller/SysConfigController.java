package org.zhangrui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhangrui.common.result.Result;
import org.zhangrui.service.ISysConfigService;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统配置控制器
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class SysConfigController {

    private final ISysConfigService sysConfigService;

    @GetMapping("/get")
    public Result<Map<String, String>> getConfigs() {
        Map<String, String> configs = new HashMap<>();
        configs.put("announcement", sysConfigService.getConfigValue("announcement", "欢迎光临校园小卖部！满29元起送，支持自取和配送"));
        configs.put("minOrderAmount", sysConfigService.getConfigValue("min_order_amount", "29"));
        configs.put("delivery_fee", sysConfigService.getConfigValue("delivery_fee", "0"));
        return Result.success(configs);
    }
}
