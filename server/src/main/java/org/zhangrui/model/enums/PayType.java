package org.zhangrui.model.enums;

/**
 * 支付方式枚举
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public enum PayType {

    WECHAT(1, "微信支付"),
    ALIPAY(2, "支付宝"),
    BALANCE(3, "余额支付");

    private final Integer code;
    private final String description;

    PayType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PayType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PayType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
