package org.zhangrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhangrui.mapper.OrderItemMapper;
import org.zhangrui.mapper.OrderMapper;
import org.zhangrui.mapper.ProductMapper;
import org.zhangrui.mapper.UserMapper;
import org.zhangrui.model.entity.Order;
import org.zhangrui.model.entity.OrderItem;
import org.zhangrui.model.vo.DashboardVO;
import org.zhangrui.service.IStatisticsService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计服务实现类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements IStatisticsService {

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public DashboardVO getDashboardData() {
        DashboardVO vo = new DashboardVO();

        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime weekStart = todayStart.minusDays(7);
        LocalDateTime monthStart = todayStart.minusDays(30);

        LambdaQueryWrapper<Order> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.ge(Order::getCreateTime, todayStart);
        todayWrapper.eq(Order::getStatus, 4);
        vo.setTodayOrders(orderMapper.selectCount(todayWrapper));

        LambdaQueryWrapper<Order> todaySalesWrapper = new LambdaQueryWrapper<>();
        todaySalesWrapper.ge(Order::getCreateTime, todayStart);
        todaySalesWrapper.eq(Order::getPayStatus, 1);
        List<Order> todayOrders = orderMapper.selectList(todaySalesWrapper);
        vo.setTodaySales(todayOrders.stream().map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

        LambdaQueryWrapper<Order> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Order::getStatus, 2);
        vo.setPendingOrders(orderMapper.selectCount(pendingWrapper));

        vo.setTotalUsers(userMapper.selectCount(null));

        LambdaQueryWrapper<Order> totalSalesWrapper = new LambdaQueryWrapper<>();
        totalSalesWrapper.eq(Order::getPayStatus, 1);
        List<Order> allPaidOrders = orderMapper.selectList(totalSalesWrapper);
        vo.setTotalSales(allPaidOrders.stream().map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

        LambdaQueryWrapper<Order> weekWrapper = new LambdaQueryWrapper<>();
        weekWrapper.ge(Order::getCreateTime, weekStart);
        weekWrapper.eq(Order::getPayStatus, 1);
        List<Order> weekOrders = orderMapper.selectList(weekWrapper);
        vo.setWeekSales(weekOrders.stream().map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

        LambdaQueryWrapper<Order> monthWrapper = new LambdaQueryWrapper<>();
        monthWrapper.ge(Order::getCreateTime, monthStart);
        monthWrapper.eq(Order::getPayStatus, 1);
        List<Order> monthOrders = orderMapper.selectList(monthWrapper);
        vo.setMonthSales(monthOrders.stream().map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

        return vo;
    }

    @Override
    public List<Map<String, Object>> getSalesTrend(Integer days) {
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime dayStart = now.minusDays(i).with(LocalTime.MIN);
            LocalDateTime dayEnd = dayStart.with(LocalTime.MAX);

            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.ge(Order::getCreateTime, dayStart);
            wrapper.le(Order::getCreateTime, dayEnd);
            wrapper.eq(Order::getPayStatus, 1);
            List<Order> orders = orderMapper.selectList(wrapper);

            BigDecimal sales = orders.stream().map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dayStart.toLocalDate().toString());
            dayData.put("sales", sales);
            dayData.put("orders", orders.size());
            trend.add(dayData);
        }

        return trend;
    }

    @Override
    public List<Map<String, Object>> getProductSalesRanking(Integer limit) {
        List<Map<String, Object>> ranking = new ArrayList<>();

        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(OrderItem::getProductId, OrderItem::getProductName);
        wrapper.groupBy(OrderItem::getProductId, OrderItem::getProductName);

        List<OrderItem> items = orderItemMapper.selectList(wrapper);

        for (OrderItem item : items) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("productId", item.getProductId());
            productData.put("productName", item.getProductName());

            LambdaQueryWrapper<OrderItem> countWrapper = new LambdaQueryWrapper<>();
            countWrapper.eq(OrderItem::getProductId, item.getProductId());
            List<OrderItem> allItems = orderItemMapper.selectList(countWrapper);
            int totalSales = allItems.stream().mapToInt(OrderItem::getQuantity).sum();
            productData.put("sales", totalSales);

            ranking.add(productData);
        }

        ranking.sort((a, b) -> Integer.compare((int) b.get("sales"), (int) a.get("sales")));
        if (ranking.size() > limit) {
            return ranking.subList(0, limit);
        }

        return ranking;
    }

    @Override
    public Map<String, Object> getOrderStatusStats() {
        Map<String, Object> stats = new HashMap<>();

        for (int status = 1; status <= 5; status++) {
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Order::getStatus, status);
            long count = orderMapper.selectCount(wrapper);
            stats.put("status_" + status, count);
        }

        return stats;
    }
}
