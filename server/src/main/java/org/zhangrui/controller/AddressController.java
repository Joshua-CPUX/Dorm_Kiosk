package org.zhangrui.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhangrui.common.result.Result;
import org.zhangrui.model.dto.AddressDTO;
import org.zhangrui.model.vo.AddressVO;
import org.zhangrui.service.IAddressService;

import java.util.List;

/**
 * 收货地址控制器
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    /**
     * 获取地址列表
     */
    @GetMapping("/list")
    public Result<List<AddressVO>> getAddressList(@RequestParam Long userId) {
        List<AddressVO> addresses = addressService.getAddressList(userId);
        return Result.success(addresses);
    }

    /**
     * 获取默认地址
     */
    @GetMapping("/default")
    public Result<AddressVO> getDefaultAddress(@RequestParam Long userId) {
        AddressVO address = addressService.getDefaultAddress(userId);
        return Result.success(address);
    }

    /**
     * 添加地址
     */
    @PostMapping("/add")
    public Result<Long> addAddress(@RequestParam Long userId, @Valid @RequestBody AddressDTO dto) {
        Long addressId = addressService.addAddress(userId, dto);
        return Result.success(addressId);
    }

    /**
     * 更新地址
     */
    @PutMapping("/update")
    public Result<Void> updateAddress(@RequestParam Long userId, @Valid @RequestBody AddressDTO dto) {
        addressService.updateAddress(userId, dto);
        return Result.success();
    }

    /**
     * 删除地址
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@RequestParam Long userId, @PathVariable Long id) {
        addressService.deleteAddress(userId, id);
        return Result.success();
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/default/{id}")
    public Result<Void> setDefaultAddress(@RequestParam Long userId, @PathVariable Long id) {
        addressService.setDefaultAddress(userId, id);
        return Result.success();
    }
}
