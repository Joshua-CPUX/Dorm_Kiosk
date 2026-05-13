package org.zhangrui.model.enums;

/**
 * 用户角色枚举
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public enum UserRole {

    USER(0, "普通用户"),
    OWNER(1, "店主");

    private final Integer code;
    private final String description;

    UserRole(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static UserRole fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        return null;
    }

    public static String getDescriptionByCode(Integer code) {
        UserRole role = fromCode(code);
        return role != null ? role.description : "未知";
    }

    public static boolean isOwner(Integer code) {
        return OWNER.code.equals(code);
    }
}
