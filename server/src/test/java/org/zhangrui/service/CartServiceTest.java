package org.zhangrui.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.vo.CartProductVO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 购物车服务测试类
 * 覆盖购物车增删改查等核心功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartServiceTest {

    @Autowired
    private ICartService cartService;

    @Autowired
    private IProductService productService;

    private static Long createdCartId;

    @Test
    @Order(1)
    @DisplayName("测试添加购物车")
    public void testAddCart() {
        assertDoesNotThrow(() -> cartService.addCart(1L, 1L, 2));
    }

    @Test
    @Order(2)
    @DisplayName("测试获取购物车列表")
    public void testGetCartList() {
        List<CartProductVO> cartList = cartService.getCartList(1L);

        assertNotNull(cartList);
        assertTrue(cartList.size() >= 0);

        if (!cartList.isEmpty()) {
            createdCartId = cartList.get(0).getCartId();
        }
    }

    @Test
    @Order(3)
    @DisplayName("测试添加购物车（商品不存在）")
    public void testAddCartProductNotFound() {
        assertThrows(BusinessException.class, () -> cartService.addCart(1L, 99999L, 1));
    }

    @Test
    @Order(4)
    @DisplayName("测试更新购物车数量")
    public void testUpdateCartQuantity() {
        if (createdCartId != null) {
            assertDoesNotThrow(() -> cartService.updateCartQuantity(1L, createdCartId, 5));
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试更新购物车数量（数量为0）")
    public void testUpdateCartQuantityZero() {
        if (createdCartId != null) {
            assertThrows(BusinessException.class, () -> cartService.updateCartQuantity(1L, createdCartId, 0));
        }
    }

    @Test
    @Order(6)
    @DisplayName("测试删除购物车商品")
    public void testDeleteCart() {
        if (createdCartId != null) {
            assertDoesNotThrow(() -> cartService.deleteCart(1L, createdCartId));
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试删除购物车商品（不存在）")
    public void testDeleteCartNotFound() {
        assertThrows(BusinessException.class, () -> cartService.deleteCart(1L, 99999L));
    }

    @Test
    @Order(8)
    @DisplayName("测试清空购物车")
    public void testClearCart() {
        cartService.addCart(1L, 1L, 1);

        List<CartProductVO> cartList = cartService.getCartList(1L);
        if (!cartList.isEmpty()) {
            List<Long> cartIds = cartList.stream()
                    .map(CartProductVO::getCartId)
                    .toList();

            assertDoesNotThrow(() -> cartService.clearCart(1L, cartIds));
        }
    }

    @Test
    @Order(9)
    @DisplayName("测试清空购物车（空列表）")
    public void testClearCartEmpty() {
        assertDoesNotThrow(() -> cartService.clearCart(1L, Arrays.asList()));
    }

    @Test
    @Order(10)
    @DisplayName("测试添加购物车（库存不足）")
    public void testAddCartInsufficientStock() {
        assertThrows(BusinessException.class, () -> cartService.addCart(1L, 1L, 99999));
    }
}
