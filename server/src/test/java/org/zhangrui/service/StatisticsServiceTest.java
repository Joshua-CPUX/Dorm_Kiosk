package org.zhangrui.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.model.vo.DashboardVO;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StatisticsServiceTest {

    @Autowired
    private IStatisticsService statisticsService;

    @Test
    public void testGetDashboardData() {
        DashboardVO dashboardVO = statisticsService.getDashboardData();

        assertNotNull(dashboardVO);
    }

    @Test
    public void testGetSalesTrend() {
        List<Map<String, Object>> trend = statisticsService.getSalesTrend(7);

        assertNotNull(trend);
        assertEquals(7, trend.size());
    }

    @Test
    public void testGetProductRanking() {
        var ranking = statisticsService.getProductSalesRanking(5);

        assertNotNull(ranking);
    }

    @Test
    public void testGetOrderStatusStats() {
        var stats = statisticsService.getOrderStatusStats();

        assertNotNull(stats);
    }
}
