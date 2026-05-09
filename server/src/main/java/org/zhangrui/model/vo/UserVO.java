package org.zhangrui.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户VO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class UserVO implements Serializable {

    private Long id;

    private String username;

    private String nickname;

    private String token;

    private String avatar;

    private String phone;

    private BigDecimal balance;

    private Integer status;

}
