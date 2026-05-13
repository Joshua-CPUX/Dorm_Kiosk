package org.zhangrui.model.vo;

import lombok.Data;
import org.zhangrui.model.enums.OrderStatus;
import org.zhangrui.model.enums.PayStatus;
import org.zhangrui.model.enums.PayType;

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

    private String payTypeName;

    private LocalDateTime payTime;

    private Integer payStatus;

    private String payStatusName;

    private Integer orderType;

    private String orderTypeName;

    private Integer status;

    private String statusName;

    private AddressVO address;

    private String remark;

    private LocalDateTime cancelTime;

    private LocalDateTime completeTime;

    private LocalDateTime createTime;

    private List<OrderItemVO> items;

    public void setStatus(Integer status) {
        this.status = status;
        this.statusName = OrderStatus.getDescriptionByCode(status);
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
        this.payStatusName = PayStatus.getDescriptionByCode(payStatus);
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
        PayType type = PayType.fromCode(payType);
        this.payTypeName = type != null ? type.getDescription() : "未知";
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
        this.orderTypeName = orderType == 1 ? "自取" : "配送";
    }

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
