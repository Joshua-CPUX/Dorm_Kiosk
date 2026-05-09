package org.zhangrui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhangrui.common.result.Result;
import org.zhangrui.model.dto.OrderCreateDTO;
import org.zhangrui.model.vo.OrderVO;
import org.zhangrui.service.IOrderService;

import java.util.Map;

/**
 * 订单控制器
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<Long> createOrder(@RequestParam Long userId, @Valid @RequestBody OrderCreateDTO dto) {
        Long orderId = orderService.createOrder(userId, dto);
        return Result.success(orderId);
    }

    /**
     * 支付订单
     */
    @PostMapping("/pay")
    public Result<Map<String, String>> payOrder(@RequestParam Long userId, @RequestParam Long orderId) {
        Map<String, String> payParams = orderService.payOrder(userId, orderId);
        return Result.success(payParams);
    }

    /**
     * 模拟支付（测试用）
     */
    @PostMapping("/mock-pay")
    public Result<Void> mockPay(@RequestParam Long userId, @RequestParam Long orderId) {
        orderService.mockPay(userId, orderId);
        return Result.success();
    }

    /**
     * 支付回调
     */
    @PostMapping("/notify")
    public Result<String> payNotify(@RequestBody Map<String, String> params) {
        orderService.payNotify(params);
        return Result.success("SUCCESS");
    }

    /**
     * 获取订单列表
     */
    @GetMapping("/list")
    public Result<Page<OrderVO>> getOrderList(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        Page<OrderVO> orders = orderService.getOrderList(userId, pageNum, pageSize, status);
        return Result.success(orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@RequestParam Long userId, @PathVariable Long id) {
        OrderVO orderVO = orderService.getOrderDetail(userId, id);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel/{id}")
    public Result<Void> cancelOrder(@RequestParam Long userId, @PathVariable Long id) {
        orderService.cancelOrder(userId, id);
        return Result.success();
    }

    /**
     * 确认收货
     */
    @PutMapping("/confirm/{id}")
    public Result<Void> confirmOrder(@RequestParam Long userId, @PathVariable Long id) {
        orderService.confirmOrder(userId, id);
        return Result.success();
    }
}
