package org.zhangrui.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
@TableName("oms_order")
public class Order implements Serializable {

    @TableId
    private Long id;

    private String orderNo;

    private Long userId;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private BigDecimal freight;

    private Integer payType;

    private LocalDateTime payTime;

    private Integer payStatus;

    private Integer orderType;

    private Integer status;

    private Long addressId;

    private String remark;

    private LocalDateTime cancelTime;

    private LocalDateTime completeTime;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
