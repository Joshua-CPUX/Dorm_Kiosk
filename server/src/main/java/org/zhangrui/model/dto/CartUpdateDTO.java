package org.zhangrui.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新购物车DTO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class CartUpdateDTO implements Serializable {

    @NotNull(message = "购物车ID不能为空")
    private Long id;

    @NotNull(message = "数量不能为空")
    private Integer quantity;
}
