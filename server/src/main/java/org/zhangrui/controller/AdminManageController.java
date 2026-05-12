package org.zhangrui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhangrui.common.result.Result;
import org.zhangrui.model.dto.ProductAddDTO;
import org.zhangrui.model.dto.ProductUpdateDTO;
import org.zhangrui.model.entity.Category;
import org.zhangrui.model.entity.Product;
import org.zhangrui.model.vo.DashboardVO;
import org.zhangrui.model.vo.UserVO;
import org.zhangrui.service.ICategoryService;
import org.zhangrui.service.IProductService;
import org.zhangrui.service.IStatisticsService;
import org.zhangrui.service.IUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 管理端控制器
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminManageController {

    private final IProductService productService;
    private final ICategoryService categoryService;
    private final IStatisticsService statisticsService;
    private final IUserService userService;

    /**
     * 获取商品列表
     */
    @GetMapping("/product/list")
    public Result<List<Product>> getProductList(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Product> page =
                productService.getProductPage(1, 100, categoryId, keyword);
        return Result.success(page.getRecords());
    }

    /**
     * 添加商品
     */
    @PostMapping("/product/add")
    public Result<Long> addProduct(@RequestBody ProductAddDTO dto) {
        Long productId = productService.addProduct(dto);
        return Result.success(productId);
    }

    /**
     * 更新商品
     */
    @PutMapping("/product/update")
    public Result<Void> updateProduct(@RequestBody ProductUpdateDTO dto) {
        productService.updateProduct(dto);
        return Result.success();
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/product/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }

    /**
     * 更新商品状态
     */
    @PutMapping("/product/status")
    public Result<Void> updateProductStatus(@RequestParam Long id, @RequestParam Integer status) {
        productService.updateProductStatus(id, status);
        return Result.success();
    }

    /**
     * 获取分类列表
     */
    @GetMapping("/category/list")
    public Result<List<Category>> getCategoryList() {
        List<Category> categories = categoryService.getCategoryList();
        return Result.success(categories);
    }

    /**
     * 添加分类
     */
    @PostMapping("/category/add")
    public Result<Long> addCategory(
            @RequestParam String name,
            @RequestParam(required = false) String icon,
            @RequestParam(required = false) Integer sort) {
        Long categoryId = categoryService.addCategory(name, icon, sort);
        return Result.success(categoryId);
    }

    /**
     * 更新分类
     */
    @PutMapping("/category/update")
    public Result<Void> updateCategory(
            @RequestParam Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String icon,
            @RequestParam(required = false) Integer sort) {
        categoryService.updateCategory(id, name, icon, sort);
        return Result.success();
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/category/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 获取仪表盘数据
     */
    @GetMapping("/statistics/dashboard")
    public Result<DashboardVO> getDashboardData() {
        DashboardVO data = statisticsService.getDashboardData();
        return Result.success(data);
    }

    /**
     * 获取销售趋势
     */
    @GetMapping("/statistics/sales")
    public Result<List<Map<String, Object>>> getSalesTrend(@RequestParam(defaultValue = "7") Integer days) {
        List<Map<String, Object>> trend = statisticsService.getSalesTrend(days);
        return Result.success(trend);
    }

    /**
     * 获取商品销量排行
     */
    @GetMapping("/statistics/ranking")
    public Result<List<Map<String, Object>>> getProductRanking(@RequestParam(defaultValue = "10") Integer limit) {
        List<Map<String, Object>> ranking = statisticsService.getProductSalesRanking(limit);
        return Result.success(ranking);
    }

    /**
     * 获取订单状态统计
     */
    @GetMapping("/statistics/order-status")
    public Result<Map<String, Object>> getOrderStatusStats() {
        Map<String, Object> stats = statisticsService.getOrderStatusStats();
        return Result.success(stats);
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/user/list")
    public Result<Page<UserVO>> getUserList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<UserVO> users = userService.getUserList(pageNum, pageSize, keyword);
        return Result.success(users);
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/user/status")
    public Result<Void> updateUserStatus(@RequestParam Long id, @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return Result.success();
    }
}
