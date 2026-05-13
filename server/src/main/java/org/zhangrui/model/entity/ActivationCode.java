package org.zhangrui.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 激活码实体类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
@TableName("sys_activation_code")
public class ActivationCode implements Serializable {

    @TableId
    private Long id;

    private String code;

    private Integer isUsed;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
