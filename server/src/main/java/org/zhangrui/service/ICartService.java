package org.zhangrui.service;

import org.zhangrui.model.vo.CartProductVO;

import java.util.List;

/**
 * 购物车服务接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface ICartService {

    /**
     * 获取购物车列表
     *
     * @param userId 用户ID
     * @return 购物车商品列表
     */
    List<CartProductVO> getCartList(Long userId);

    /**
     * 添加购物车
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @param quantity  数量
     */
    void addCart(Long userId, Long productId, Integer quantity);

    /**
     * 更新购物车数量
     *
     * @param userId   用户ID
     * @param cartId   购物车ID
     * @param quantity 数量
     */
    void updateCartQuantity(Long userId, Long cartId, Integer quantity);

    /**
     * 删除购物车商品
     *
     * @param userId 用户ID
     * @param cartId 购物车ID
     */
    void deleteCart(Long userId, Long cartId);

    /**
     * 清空购物车
     *
     * @param userId  用户ID
     * @param cartIds 购物车ID列表
     */
    void clearCart(Long userId, List<Long> cartIds);
}
