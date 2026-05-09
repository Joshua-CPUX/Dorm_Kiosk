package org.zhangrui.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 收货地址DTO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class AddressDTO implements Serializable {

    private Long id;

    @NotBlank(message = "收货人不能为空")
    private String consignee;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    private String province;

    private String city;

    private String district;

    @NotBlank(message = "详细地址不能为空")
    private String detail;

    private Integer isDefault;
}
