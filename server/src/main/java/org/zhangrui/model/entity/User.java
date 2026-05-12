package org.zhangrui.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
@TableName("sys_user")
public class User implements Serializable {

    @TableId
    private Long id;

    private String openid;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private String phone;

    private BigDecimal balance;

    private Integer role;

    private Integer status;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
