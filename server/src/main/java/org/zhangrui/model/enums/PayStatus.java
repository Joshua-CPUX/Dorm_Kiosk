package org.zhangrui.model.enums;

/**
 * 支付状态枚举
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public enum PayStatus {

    UNPAID(0, "未支付"),
    PAID(1, "已支付");

    private final Integer code;
    private final String description;

    PayStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PayStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PayStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static String getDescriptionByCode(Integer code) {
        PayStatus status = fromCode(code);
        return status != null ? status.description : "未知";
    }
}
