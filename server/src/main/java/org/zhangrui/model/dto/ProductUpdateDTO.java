package org.zhangrui.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 更新商品DTO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class ProductUpdateDTO implements Serializable {

    private Long id;

    private Long categoryId;

    private String name;

    private String subtitle;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;

    private Integer sales;

    private String image;

    private String images;

    private String description;

    private Integer status;
}
