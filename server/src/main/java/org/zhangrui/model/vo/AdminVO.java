package org.zhangrui.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员VO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class AdminVO implements Serializable {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String token;
}
