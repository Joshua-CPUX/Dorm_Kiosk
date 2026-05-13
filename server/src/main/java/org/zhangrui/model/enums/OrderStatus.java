package org.zhangrui.model.enums;

/**
 * 订单状态枚举
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public enum OrderStatus {

    PENDING_PAYMENT(1, "待支付"),
    PAID(2, "已支付"),
    DELIVERING(3, "配送中"),
    COMPLETED(4, "已完成"),
    CANCELLED(5, "已取消");

    private final Integer code;
    private final String description;

    OrderStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static String getDescriptionByCode(Integer code) {
        OrderStatus status = fromCode(code);
        return status != null ? status.description : "未知";
    }
}
