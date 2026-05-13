package org.zhangrui.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.model.vo.DashboardVO;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 统计服务测试类
 * 覆盖统计查询等核心功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatisticsServiceTest {

    @Autowired
    private IStatisticsService statisticsService;

    @Test
    @Order(1)
    @DisplayName("测试获取仪表盘数据")
    public void testGetDashboardData() {
        DashboardVO dashboard = statisticsService.getDashboardData();

        assertNotNull(dashboard);
    }

    @Test
    @Order(2)
    @DisplayName("测试获取销售趋势")
    public void testGetSalesTrend() {
        List<Map<String, Object>> salesTrend = statisticsService.getSalesTrend(7);

        assertNotNull(salesTrend);
    }

    @Test
    @Order(3)
    @DisplayName("测试获取商品销量排行")
    public void testGetProductSalesRanking() {
        List<Map<String, Object>> ranking = statisticsService.getProductSalesRanking(10);

        assertNotNull(ranking);
        assertTrue(ranking.size() <= 10);
    }

    @Test
    @Order(4)
    @DisplayName("测试获取订单状态统计")
    public void testGetOrderStatusStats() {
        Map<String, Object> stats = statisticsService.getOrderStatusStats();

        assertNotNull(stats);
    }
}
