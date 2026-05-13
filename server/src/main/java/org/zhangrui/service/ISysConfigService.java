package org.zhangrui.service;

import org.zhangrui.model.entity.SysConfig;

import java.util.List;

/**
 * 系统配置服务接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface ISysConfigService {

    /**
     * 获取所有系统配置
     */
    List<SysConfig> getAllConfigs();

    /**
     * 根据key获取配置值
     */
    String getConfigValue(String key);

    /**
     * 根据key获取配置值，如果不存在返回默认值
     */
    String getConfigValue(String key, String defaultValue);
}
