package org.zhangrui.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.model.vo.StatisticsVO;

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
    @DisplayName("测试获取今日统计")
    public void testGetTodayStatistics() {
        StatisticsVO statistics = statisticsService.getTodayStatistics();

        assertNotNull(statistics);
        assertNotNull(statistics.getOrderCount());
        assertNotNull(statistics.getOrderAmount());
        assertNotNull(statistics.getUserCount());
        assertNotNull(statistics.getProductCount());
    }

    @Test
    @Order(2)
    @DisplayName("测试获取销售统计")
    public void testGetSalesStatistics() {
        StatisticsVO statistics = statisticsService.getSalesStatistics(7);

        assertNotNull(statistics);
        assertNotNull(statistics.getOrderCount());
        assertNotNull(statistics.getOrderAmount());
    }

    @Test
    @Order(3)
    @DisplayName("测试获取商品统计")
    public void testGetProductStatistics() {
        StatisticsVO statistics = statisticsService.getProductStatistics();

        assertNotNull(statistics);
        assertNotNull(statistics.getProductCount());
    }

    @Test
    @Order(4)
    @DisplayName("测试获取用户统计")
    public void testGetUserStatistics() {
        StatisticsVO statistics = statisticsService.getUserStatistics(7);

        assertNotNull(statistics);
        assertNotNull(statistics.getUserCount());
    }

    @Test
    @Order(5)
    @DisplayName("测试获取订单统计")
    public void testGetOrderStatistics() {
        StatisticsVO statistics = statisticsService.getOrderStatistics(7);

        assertNotNull(statistics);
        assertNotNull(statistics.getOrderCount());
    }
}
