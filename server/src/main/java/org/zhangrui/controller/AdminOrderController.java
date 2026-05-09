package org.zhangrui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhangrui.common.result.Result;
import org.zhangrui.model.vo.OrderVO;
import org.zhangrui.service.IOrderService;

/**
 * 管理端订单控制器
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final IOrderService orderService;

    /**
     * 获取订单列表
     */
    @GetMapping("/list")
    public Result<Page<OrderVO>> getOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        Page<OrderVO> orders = orderService.getAdminOrderList(pageNum, pageSize, status);
        return Result.success(orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.getAdminOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/status")
    public Result<Void> updateOrderStatus(@RequestParam Long orderId, @RequestParam Integer status) {
        orderService.updateOrderStatus(orderId, status);
        return Result.success();
    }
}
