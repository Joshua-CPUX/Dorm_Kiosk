package org.zhangrui.service;

import org.zhangrui.model.dto.AdminLoginDTO;
import org.zhangrui.model.vo.AdminVO;

/**
 * 管理员服务接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface IAdminService {

    /**
     * 管理员登录
     *
     * @param dto 登录信息
     * @return 管理员信息
     */
    AdminVO login(AdminLoginDTO dto);

    /**
     * 获取管理员信息
     *
     * @param adminId 管理员ID
     * @return 管理员信息
     */
    AdminVO getAdminInfo(Long adminId);
}
