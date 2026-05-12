package org.zhangrui.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.mapper.*;
import org.zhangrui.model.dto.OrderCreateDTO;
import org.zhangrui.model.entity.*;
import org.zhangrui.model.vo.AddressVO;
import org.zhangrui.model.vo.CartProductVO;
import org.zhangrui.model.vo.OrderVO;
import org.zhangrui.service.IAddressService;
import org.zhangrui.service.ICartService;
import org.zhangrui.service.IOrderService;
import org.zhangrui.service.IProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    private final AddressMapper addressMapper;
    private final ICartService cartService;
    private final IProductService productService;
    private final IAddressService addressService;

    @Override
    @Transactional
    public Long createOrder(Long userId, OrderCreateDTO dto) {
        List<CartProductVO> selectedProducts = new ArrayList<>();
        List<Long> cartIdsToClear = new ArrayList<>();

        if (dto.getProductId() != null && dto.getQuantity() != null) {
            Product product = productMapper.selectById(dto.getProductId());
            if (product == null) {
                throw new BusinessException("商品不存在");
            }
            if (product.getStock() < dto.getQuantity()) {
                throw new BusinessException("商品库存不足");
            }
            CartProductVO cartProductVO = new CartProductVO();
            cartProductVO.setProductId(product.getId());
            cartProductVO.setName(product.getName());
            cartProductVO.setImage(product.getImage());
            cartProductVO.setPrice(product.getPrice());
            cartProductVO.setQuantity(dto.getQuantity());
            cartProductVO.setSubtotal(product.getPrice().multiply(new java.math.BigDecimal(dto.getQuantity())));
            selectedProducts.add(cartProductVO);
        } else if (dto.getCartIds() != null && !dto.getCartIds().isEmpty()) {
            List<CartProductVO> cartProducts = cartService.getCartList(userId);
            if (cartProducts.isEmpty()) {
                throw new BusinessException("购物车为空");
            }

            selectedProducts = cartProducts.stream()
                    .filter(cp -> dto.getCartIds().contains(cp.getCartId()))
                    .collect(Collectors.toList());

            if (selectedProducts.isEmpty()) {
                throw new BusinessException("请选择要结算的商品");
            }

            cartIdsToClear.addAll(dto.getCartIds());
        } else {
            throw new BusinessException("请选择商品");
        }

        for (CartProductVO product : selectedProducts) {
            if (product.getQuantity() > product.getProductId()) {
                Product p = productMapper.selectById(product.getProductId());
                if (p.getStock() < product.getQuantity()) {
                    throw new BusinessException("商品[" + product.getName() + "]库存不足");
                }
            }
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setOrderType(dto.getOrderType());
        order.setAddressId(dto.getAddressId());
        order.setRemark(dto.getRemark());
        order.setStatus(1);
        order.setPayStatus(0);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartProductVO cp : selectedProducts) {
            totalAmount = totalAmount.add(cp.getSubtotal());
        }
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setFreight(BigDecimal.ZERO);

        orderMapper.insert(order);

        for (CartProductVO cp : selectedProducts) {
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(cp.getProductId());
            item.setProductName(cp.getName());
            item.setProductImage(cp.getImage());
            item.setPrice(cp.getPrice());
            item.setQuantity(cp.getQuantity());
            item.setTotalPrice(cp.getSubtotal());
            orderItemMapper.insert(item);

            productService.updateStock(cp.getProductId(), -cp.getQuantity());
        }

        if (!cartIdsToClear.isEmpty()) {
            cartService.clearCart(userId, cartIdsToClear);
        }

        return order.getId();
    }

    @Override
    public Map<String, String> payOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("订单状态不正确");
        }

        Map<String, String> payParams = new HashMap<>();
        payParams.put("orderId", orderId.toString());
        payParams.put("orderNo", order.getOrderNo());
        payParams.put("amount", order.getPayAmount().toString());

        return payParams;
    }

    @Override
    @Transactional
    public void mockPay(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(2);
        order.setPayStatus(1);
        order.setPayTime(LocalDateTime.now());
        order.setPayType(1);
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void payNotify(Map<String, String> params) {
        String orderNo = params.get("order_no");
        String transactionId = params.get("transaction_id");

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);

        if (order != null && order.getPayStatus() == 0) {
            order.setPayStatus(1);
            order.setStatus(2);
            order.setPayTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public Page<OrderVO> getOrderList(Long userId, Integer pageNum, Integer pageSize, Integer status) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> orderPage = orderMapper.selectPage(page, wrapper);

        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        List<OrderVO> voList = orderPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public OrderVO getOrderDetail(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        return convertToVO(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("订单状态不正确，无法取消");
        }

        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> items = orderItemMapper.selectList(wrapper);

        for (OrderItem item : items) {
            productService.updateStock(item.getProductId(), item.getQuantity());
        }

        order.setStatus(5);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void confirmOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 4) {
            throw new BusinessException("订单状态不正确");
        }
        order.setStatus(4);
        order.setCompleteTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public Page<OrderVO> getAdminOrderList(Integer pageNum, Integer pageSize, Integer status, String orderNo) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        if (orderNo != null && !orderNo.isBlank()) {
            wrapper.like(Order::getOrderNo, orderNo);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> orderPage = orderMapper.selectPage(page, wrapper);

        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        List<OrderVO> voList = orderPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public OrderVO getAdminOrderDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return convertToVO(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, Integer status) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(status);
        if (status == 4) {
            order.setCompleteTime(LocalDateTime.now());
        }
        orderMapper.updateById(order);
    }

    private String generateOrderNo() {
        return "DK" + System.currentTimeMillis() + IdUtil.randomUUID().substring(0, 6).toUpperCase();
    }

    private OrderVO convertToVO(Order order) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPayAmount(order.getPayAmount());
        vo.setFreight(order.getFreight());
        vo.setPayType(order.getPayType());
        vo.setPayTime(order.getPayTime());
        vo.setPayStatus(order.getPayStatus());
        vo.setOrderType(order.getOrderType());
        vo.setStatus(order.getStatus());
        vo.setRemark(order.getRemark());
        vo.setCancelTime(order.getCancelTime());
        vo.setCompleteTime(order.getCompleteTime());
        vo.setCreateTime(order.getCreateTime());

        vo.setStatusName(getStatusName(order.getStatus()));
        vo.setOrderTypeName(order.getOrderType() == 1 ? "自取" : "配送");

        if (order.getAddressId() != null) {
            Address address = addressMapper.selectById(order.getAddressId());
            if (address != null) {
                AddressVO addressVO = new AddressVO();
                addressVO.setId(address.getId());
                addressVO.setConsignee(address.getConsignee());
                addressVO.setPhone(address.getPhone());
                addressVO.setProvince(address.getProvince());
                addressVO.setCity(address.getCity());
                addressVO.setDistrict(address.getDistrict());
                addressVO.setDetail(address.getDetail());
                addressVO.setFullAddress((address.getProvince() != null ? address.getProvince() : "")
                        + (address.getCity() != null ? address.getCity() : "")
                        + (address.getDistrict() != null ? address.getDistrict() : "")
                        + address.getDetail());
                vo.setAddress(addressVO);
            }
        }

        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        List<OrderVO.OrderItemVO> itemVOs = items.stream().map(item -> {
            OrderVO.OrderItemVO itemVO = new OrderVO.OrderItemVO();
            itemVO.setId(item.getId());
            itemVO.setProductId(item.getProductId());
            itemVO.setProductName(item.getProductName());
            itemVO.setProductImage(item.getProductImage());
            itemVO.setPrice(item.getPrice());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setTotalPrice(item.getTotalPrice());
            return itemVO;
        }).collect(Collectors.toList());
        vo.setItems(itemVOs);

        return vo;
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 1 -> "待支付";
            case 2 -> "已支付";
            case 3 -> "配送中";
            case 4 -> "已完成";
            case 5 -> "已取消";
            default -> "未知";
        };
    }
}
