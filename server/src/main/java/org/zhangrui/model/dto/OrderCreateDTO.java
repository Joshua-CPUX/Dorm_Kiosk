package org.zhangrui.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotEmpty(message = "购物车ID列表不能为空")
    private List<Long> cartIds;

    @NotNull(message = "订单类型不能为空")
    private Integer orderType;

    private Long addressId;

    private String remark;
}
