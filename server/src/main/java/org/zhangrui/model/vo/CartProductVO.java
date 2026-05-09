package org.zhangrui.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车商品VO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class CartProductVO implements Serializable {

    private Long cartId;

    private Long productId;

    private String name;

    private String image;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subtotal;
}
