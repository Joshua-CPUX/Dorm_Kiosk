package org.zhangrui.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单VO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class OrderVO implements Serializable {

    private Long id;

    private String orderNo;

    private Long userId;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private BigDecimal freight;

    private Integer payType;

    private LocalDateTime payTime;

    private Integer payStatus;

    private Integer orderType;

    private Integer status;

    private String statusName;

    private String orderTypeName;

    private AddressVO address;

    private String remark;

    private LocalDateTime cancelTime;

    private LocalDateTime completeTime;

    private LocalDateTime createTime;

    private List<OrderItemVO> items;

    @Data
    public static class OrderItemVO implements Serializable {
        private Long id;
        private Long productId;
        private String productName;
        private String productImage;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal totalPrice;
    }
}
