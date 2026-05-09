package org.zhangrui.service;

import org.zhangrui.model.vo.DashboardVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 统计服务接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface IStatisticsService {

    /**
     * 获取仪表盘数据
     *
     * @return 仪表盘数据
     */
    DashboardVO getDashboardData();

    /**
     * 获取销售趋势
     *
     * @param days 天数
     * @return 销售趋势数据
     */
    List<Map<String, Object>> getSalesTrend(Integer days);

    /**
     * 获取商品销量排行
     *
     * @param limit 数量
     * @return 商品销量排行
     */
    List<Map<String, Object>> getProductSalesRanking(Integer limit);

    /**
     * 获取订单状态统计
     *
     * @return 订单状态统计
     */
    Map<String, Object> getOrderStatusStats();
}
