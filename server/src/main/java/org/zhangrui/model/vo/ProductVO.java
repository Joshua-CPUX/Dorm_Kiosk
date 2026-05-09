package org.zhangrui.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品VO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class ProductVO implements Serializable {

    private Long id;

    private Long categoryId;

    private String categoryName;

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
