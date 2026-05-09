package org.zhangrui.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车实体类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
@TableName("oms_cart")
public class Cart implements Serializable {

    @TableId
    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
