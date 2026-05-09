package org.zhangrui.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加购物车DTO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class CartAddDTO implements Serializable {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotNull(message = "数量不能为空")
    private Integer quantity;
}
