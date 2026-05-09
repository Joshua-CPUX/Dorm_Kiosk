package org.zhangrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.mapper.AddressMapper;
import org.zhangrui.model.dto.AddressDTO;
import org.zhangrui.model.entity.Address;
import org.zhangrui.model.vo.AddressVO;
import org.zhangrui.service.IAddressService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收货地址服务实现类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

    private final AddressMapper addressMapper;

    @Override
    public List<AddressVO> getAddressList(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.orderByDesc(Address::getIsDefault);
        wrapper.orderByDesc(Address::getCreateTime);
        List<Address> addresses = addressMapper.selectList(wrapper);
        return addresses.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public AddressVO getDefaultAddress(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.eq(Address::getIsDefault, 1);
        Address address = addressMapper.selectOne(wrapper);
        return address != null ? convertToVO(address) : null;
    }

    @Override
    @Transactional
    public Long addAddress(Long userId, AddressDTO dto) {
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }

        Address address = new Address();
        address.setUserId(userId);
        address.setConsignee(dto.getConsignee());
        address.setPhone(dto.getPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetail(dto.getDetail());
        address.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 0);
        addressMapper.insert(address);
        return address.getId();
    }

    @Override
    @Transactional
    public void updateAddress(Long userId, AddressDTO dto) {
        Address address = addressMapper.selectById(dto.getId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在");
        }

        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }

        address.setConsignee(dto.getConsignee());
        address.setPhone(dto.getPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetail(dto.getDetail());
        if (dto.getIsDefault() != null) {
            address.setIsDefault(dto.getIsDefault());
        }
        addressMapper.updateById(address);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressMapper.selectById(addressId);
        if (address != null && address.getUserId().equals(userId)) {
            addressMapper.deleteById(addressId);
        }
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        clearDefaultAddress(userId);
        Address address = addressMapper.selectById(addressId);
        if (address != null && address.getUserId().equals(userId)) {
            address.setIsDefault(1);
            addressMapper.updateById(address);
        }
    }

    private void clearDefaultAddress(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.eq(Address::getIsDefault, 1);
        List<Address> addresses = addressMapper.selectList(wrapper);
        addresses.forEach(address -> {
            address.setIsDefault(0);
            addressMapper.updateById(address);
        });
    }

    private AddressVO convertToVO(Address address) {
        AddressVO vo = new AddressVO();
        vo.setId(address.getId());
        vo.setConsignee(address.getConsignee());
        vo.setPhone(address.getPhone());
        vo.setProvince(address.getProvince());
        vo.setCity(address.getCity());
        vo.setDistrict(address.getDistrict());
        vo.setDetail(address.getDetail());
        vo.setIsDefault(address.getIsDefault());

        String fullAddress = "";
        if (address.getProvince() != null) {
            fullAddress += address.getProvince();
        }
        if (address.getCity() != null) {
            fullAddress += address.getCity();
        }
        if (address.getDistrict() != null) {
            fullAddress += address.getDistrict();
        }
        fullAddress += address.getDetail();
        vo.setFullAddress(fullAddress);

        return vo;
    }
}
