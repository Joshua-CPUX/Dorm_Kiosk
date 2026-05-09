package org.zhangrui.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 仪表盘统计VO
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
public class DashboardVO implements Serializable {

    private Long todayOrders;

    private BigDecimal todaySales;

    private Long pendingOrders;

    private Long totalUsers;

    private BigDecimal totalSales;

    private BigDecimal weekSales;

    private BigDecimal monthSales;
}
