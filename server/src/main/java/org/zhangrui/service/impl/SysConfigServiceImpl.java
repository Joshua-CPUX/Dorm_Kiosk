package org.zhangrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhangrui.mapper.SysConfigMapper;
import org.zhangrui.model.entity.SysConfig;
import org.zhangrui.service.ISysConfigService;

import java.util.List;

/**
 * 系统配置服务实现类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements ISysConfigService {

    private final SysConfigMapper sysConfigMapper;

    @Override
    public List<SysConfig> getAllConfigs() {
        return sysConfigMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public String getConfigValue(String key) {
        return getConfigValue(key, null);
    }

    @Override
    public String getConfigValue(String key, String defaultValue) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigKey, key);
        SysConfig config = sysConfigMapper.selectOne(wrapper);
        return config != null ? config.getConfigValue() : defaultValue;
    }
}
