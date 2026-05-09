package org.zhangrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.mapper.CartMapper;
import org.zhangrui.mapper.ProductMapper;
import org.zhangrui.model.entity.Cart;
import org.zhangrui.model.entity.Product;
import org.zhangrui.model.vo.CartProductVO;
import org.zhangrui.service.ICartService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @Override
    public List<CartProductVO> getCartList(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        List<Cart> carts = cartMapper.selectList(wrapper);

        return carts.stream().map(cart -> {
            Product product = productMapper.selectById(cart.getProductId());
            if (product == null) {
                return null;
            }
            CartProductVO vo = new CartProductVO();
            vo.setCartId(cart.getId());
            vo.setProductId(product.getId());
            vo.setName(product.getName());
            vo.setImage(product.getImage());
            vo.setPrice(product.getPrice());
            vo.setQuantity(cart.getQuantity());
            vo.setSubtotal(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            return vo;
        }).filter(vo -> vo != null).collect(Collectors.toList());
    }

    @Override
    public void addCart(Long userId, Long productId, Integer quantity) {
        Product product = productMapper.selectById(productId);
        if (product == null || !Integer.valueOf(1).equals(product.getStatus())) {
            throw new BusinessException("商品不存在或已下架");
        }
        if (product.getStock() == null || product.getStock() < quantity) {
            throw new BusinessException("库存不足");
        }

        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.eq(Cart::getProductId, productId);
        Cart existCart = cartMapper.selectOne(wrapper);

        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + quantity);
            cartMapper.updateById(existCart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            try {
                cartMapper.insert(cart);
            } catch (Exception e) {
                Cart deletedCart = cartMapper.selectByUserIdAndProductIdIncludingDeleted(userId, productId);
                if (deletedCart != null) {
                    deletedCart.setQuantity(deletedCart.getQuantity() + quantity);
                    deletedCart.setDeleted(0);
                    cartMapper.updateById(deletedCart);
                } else {
                    throw e;
                }
            }
        }
    }

    @Override
    public void updateCartQuantity(Long userId, Long cartId, Integer quantity) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车记录不存在");
        }
        if (quantity <= 0) {
            cartMapper.deleteById(cartId);
        } else {
            cart.setQuantity(quantity);
            cartMapper.updateById(cart);
        }
    }

    @Override
    public void deleteCart(Long userId, Long cartId) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart != null && cart.getUserId().equals(userId)) {
            cartMapper.deleteById(cartId);
        }
    }

    @Override
    public void clearCart(Long userId, List<Long> cartIds) {
        if (cartIds != null && !cartIds.isEmpty()) {
            cartIds.forEach(cartId -> {
                Cart cart = cartMapper.selectById(cartId);
                if (cart != null && cart.getUserId().equals(userId)) {
                    cartMapper.deleteById(cartId);
                }
            });
        }
    }
}
