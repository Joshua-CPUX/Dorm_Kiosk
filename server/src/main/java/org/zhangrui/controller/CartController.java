package org.zhangrui.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zhangrui.common.result.Result;
import org.zhangrui.model.dto.CartAddDTO;
import org.zhangrui.model.dto.CartUpdateDTO;
import org.zhangrui.model.vo.CartProductVO;
import org.zhangrui.service.ICartService;

import java.util.List;

/**
 * 购物车控制器
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    /**
     * 获取购物车列表
     */
    @GetMapping("/list")
    public Result<List<CartProductVO>> getCartList(@RequestParam Long userId) {
        List<CartProductVO> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }

    /**
     * 添加购物车
     */
    @PostMapping("/add")
    public Result<Void> addCart(@RequestParam Long userId, @Valid @RequestBody CartAddDTO dto) {
        cartService.addCart(userId, dto.getProductId(), dto.getQuantity());
        return Result.success();
    }

    /**
     * 更新购物车数量
     */
    @PutMapping("/update")
    public Result<Void> updateCart(@RequestParam Long userId, @Valid @RequestBody CartUpdateDTO dto) {
        cartService.updateCartQuantity(userId, dto.getId(), dto.getQuantity());
        return Result.success();
    }

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCart(@RequestParam Long userId, @PathVariable Long id) {
        cartService.deleteCart(userId, id);
        return Result.success();
    }
}
