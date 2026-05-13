package org.zhangrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhangrui.mapper.ActivationCodeMapper;
import org.zhangrui.model.entity.ActivationCode;
import org.zhangrui.service.IActivationCodeService;

/**
 * 激活码Service实现类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class ActivationCodeServiceImpl implements IActivationCodeService {

    private final ActivationCodeMapper activationCodeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validateAndUse(String code) {
        // 1. 查询激活码
        LambdaUpdateWrapper<ActivationCode> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(ActivationCode::getCode, code)
                .eq(ActivationCode::getIsUsed, 0);
        ActivationCode activationCode = activationCodeMapper.selectOne(queryWrapper);
        
        if (activationCode == null) {
            // 激活码不存在或已使用
            return false;
        }
        
        // 2. 标记为已使用
        LambdaUpdateWrapper<ActivationCode> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ActivationCode::getId, activationCode.getId())
                .set(ActivationCode::getIsUsed, 1);
        int updated = activationCodeMapper.update(null, updateWrapper);
        
        return updated > 0;
    }
}
