package org.zhangrui.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 收货地址VO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class AddressVO implements Serializable {

    private Long id;

    private String consignee;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String detail;

    private String fullAddress;

    private Integer isDefault;
}
