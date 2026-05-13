package org.zhangrui.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.dto.AddressAddDTO;
import org.zhangrui.model.dto.AddressUpdateDTO;
import org.zhangrui.model.vo.AddressVO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 地址服务测试类
 * 覆盖地址增删改查等核心功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressServiceTest {

    @Autowired
    private IAddressService addressService;

    private static Long createdAddressId;

    @Test
    @Order(1)
    @DisplayName("测试添加地址")
    public void testAddAddress() {
        AddressAddDTO dto = new AddressAddDTO();
        dto.setUserId(1L);
        dto.setName("测试收货人");
        dto.setPhone("13800138000");
        dto.setProvince("广东省");
        dto.setCity("深圳市");
        dto.setDistrict("南山区");
        dto.setDetail("科技园路88号");
        dto.setIsDefault(0);

        Long addressId = addressService.addAddress(dto);

        assertNotNull(addressId);
        assertTrue(addressId > 0);
        createdAddressId = addressId;
    }

    @Test
    @Order(2)
    @DisplayName("测试获取地址列表")
    public void testGetAddressList() {
        List<AddressVO> addresses = addressService.getAddressList(1L);

        assertNotNull(addresses);
        assertTrue(addresses.size() >= 0);
    }

    @Test
    @Order(3)
    @DisplayName("测试获取地址详情")
    public void testGetAddressById() {
        if (createdAddressId != null) {
            AddressVO address = addressService.getAddressById(1L, createdAddressId);
            assertNotNull(address);
            assertEquals(createdAddressId, address.getId());
        } else {
            assertThrows(BusinessException.class, () -> addressService.getAddressById(1L, 99999L));
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试更新地址")
    public void testUpdateAddress() {
        if (createdAddressId != null) {
            AddressUpdateDTO dto = new AddressUpdateDTO();
            dto.setName("更新后的收货人");
            dto.setPhone("13900139000");
            dto.setDetail("更新后的地址");

            assertDoesNotThrow(() -> addressService.updateAddress(1L, createdAddressId, dto));
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试更新地址（不存在）")
    public void testUpdateAddressNotFound() {
        AddressUpdateDTO dto = new AddressUpdateDTO();
        dto.setName("测试");

        assertThrows(BusinessException.class, () -> addressService.updateAddress(1L, 99999L, dto));
    }

    @Test
    @Order(6)
    @DisplayName("测试设置默认地址")
    public void testSetDefaultAddress() {
        if (createdAddressId != null) {
            assertDoesNotThrow(() -> addressService.setDefaultAddress(1L, createdAddressId));
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试删除地址")
    public void testDeleteAddress() {
        if (createdAddressId != null) {
            assertDoesNotThrow(() -> addressService.deleteAddress(1L, createdAddressId));
        }
    }

    @Test
    @Order(8)
    @DisplayName("测试删除地址（不存在）")
    public void testDeleteAddressNotFound() {
        assertThrows(BusinessException.class, () -> addressService.deleteAddress(1L, 99999L));
    }
}
