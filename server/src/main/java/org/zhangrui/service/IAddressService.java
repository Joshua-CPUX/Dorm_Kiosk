package org.zhangrui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhangrui.model.dto.AddressDTO;
import org.zhangrui.model.vo.AddressVO;

import java.util.List;

/**
 * 收货地址服务接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface IAddressService {

    /**
     * 获取地址列表
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<AddressVO> getAddressList(Long userId);

    /**
     * 获取默认地址
     *
     * @param userId 用户ID
     * @return 默认地址
     */
    AddressVO getDefaultAddress(Long userId);

    /**
     * 添加地址
     *
     * @param userId 用户ID
     * @param dto    地址信息
     * @return 地址ID
     */
    Long addAddress(Long userId, AddressDTO dto);

    /**
     * 更新地址
     *
     * @param userId 用户ID
     * @param dto    地址信息
     */
    void updateAddress(Long userId, AddressDTO dto);

    /**
     * 删除地址
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     */
    void deleteAddress(Long userId, Long addressId);

    /**
     * 设置默认地址
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     */
    void setDefaultAddress(Long userId, Long addressId);
}
