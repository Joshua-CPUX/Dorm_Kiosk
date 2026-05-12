package org.zhangrui.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建订单DTO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class OrderCreateDTO implements Serializable {

    private List<Long> cartIds;

    private Long productId;

    private Integer quantity;

    private Integer orderType;

    private Long addressId;

    private String remark;
}
