package org.zhangrui.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员实体类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
@TableName("sys_admin")
public class Admin implements Serializable {

    @TableId
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private Integer status;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
