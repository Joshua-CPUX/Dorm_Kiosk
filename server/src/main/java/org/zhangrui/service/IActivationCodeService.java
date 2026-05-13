package org.zhangrui.service;

/**
 * 激活码Service接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface IActivationCodeService {

    /**
     * 验证并使用激活码
     * @param code 激活码
     * @return 是否验证成功
     */
    boolean validateAndUse(String code);
}
