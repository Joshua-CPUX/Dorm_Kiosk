package org.zhangrui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhangrui.common.result.Result;
import org.zhangrui.model.entity.Product;
import org.zhangrui.model.vo.OrderVO;
import org.zhangrui.service.IOrderService;
import org.zhangrui.service.IProductService;
import org.zhangrui.service.impl.UserServiceImpl;

/**
 * 店主端控制器（需要 role=1 权限）
 */
@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final IOrderService orderService;
    private final IProductService productService;
    private final UserServiceImpl userService;

    /**
     * 获取所有订单列表
     */
    @GetMapping("/order/list")
    public Result<Page<OrderVO>> getOrderList(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        userService.validateOwner(userId);
        Page<OrderVO> orders = orderService.getAdminOrderList(pageNum, pageSize, status, null);
        return Result.success(orders);
    }

    /**
     * 更新订单状态（发货/完成）
     */
    @PutMapping("/order/status")
    public Result<Void> updateOrderStatus(
            @RequestParam Long userId,
            @RequestParam Long orderId,
            @RequestParam Integer status) {
        userService.validateOwner(userId);
        orderService.updateOrderStatus(orderId, status);
        return Result.success();
    }

    /**
     * 获取所有商品列表（含下架）
     */
    @GetMapping("/product/list")
    public Result<Page<Product>> getProductList(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        userService.validateOwner(userId);
        Page<Product> products = productService.getAllProductPage(pageNum, pageSize, keyword);
        return Result.success(products);
    }

    /**
     * 更新商品上下架状态
     */
    @PutMapping("/product/status")
    public Result<Void> updateProductStatus(
            @RequestParam Long userId,
            @RequestParam Long id,
            @RequestParam Integer status) {
        userService.validateOwner(userId);
        productService.updateProductStatus(id, status);
        return Result.success();
    }
}
