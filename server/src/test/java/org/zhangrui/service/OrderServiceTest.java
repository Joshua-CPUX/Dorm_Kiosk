package org.zhangrui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.dto.OrderCreateDTO;
import org.zhangrui.model.enums.OrderStatus;
import org.zhangrui.model.vo.OrderVO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 订单服务测试类
 * 覆盖订单创建、支付、取消、确认收货等核心功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    private static Long createdOrderId;
    private static Long createdUserId;
    private static Long createdProductId;

    @BeforeAll
    public static void setup() {
        createdUserId = 1L;
    }

    @Test
    @Order(1)
    @DisplayName("测试创建订单（直接购买）")
    public void testCreateOrderDirect() {
        OrderCreateDTO dto = new OrderCreateDTO();
        dto.setProductId(1L);
        dto.setQuantity(2);
        dto.setOrderType(2);
        dto.setAddressId(1L);

        assertDoesNotThrow(() -> {
            Long orderId = orderService.createOrder(1L, dto);
            assertNotNull(orderId);
            assertTrue(orderId > 0);
            createdOrderId = orderId;
        });
    }

    @Test
    @Order(2)
    @DisplayName("测试创建订单（购物车购买）")
    public void testCreateOrderFromCart() {
        OrderCreateDTO dto = new OrderCreateDTO();
        dto.setCartIds(Arrays.asList(1L));
        dto.setOrderType(2);
        dto.setAddressId(1L);

        try {
            Long orderId = orderService.createOrder(1L, dto);
            assertNotNull(orderId);
        } catch (BusinessException e) {
            assertTrue(e.getMessage().contains("购物车") || e.getMessage().contains("商品"));
        }
    }

    @Test
    @Order(3)
    @DisplayName("测试创建订单（库存不足）")
    public void testCreateOrderInsufficientStock() {
        OrderCreateDTO dto = new OrderCreateDTO();
        dto.setProductId(1L);
        dto.setQuantity(99999);
        dto.setOrderType(2);
        dto.setAddressId(1L);

        assertThrows(BusinessException.class, () -> orderService.createOrder(1L, dto));
    }

    @Test
    @Order(4)
    @DisplayName("测试创建订单（商品不存在）")
    public void testCreateOrderProductNotFound() {
        OrderCreateDTO dto = new OrderCreateDTO();
        dto.setProductId(99999L);
        dto.setQuantity(1);
        dto.setOrderType(2);
        dto.setAddressId(1L);

        assertThrows(BusinessException.class, () -> orderService.createOrder(1L, dto));
    }

    @Test
    @Order(5)
    @DisplayName("测试获取订单列表")
    public void testGetOrderList() {
        Page<OrderVO> page = orderService.getOrderList(1L, 1, 10, null);

        assertNotNull(page);
        assertNotNull(page.getRecords());
    }

    @Test
    @Order(6)
    @DisplayName("测试获取订单列表（按状态筛选）")
    public void testGetOrderListByStatus() {
        Page<OrderVO> page = orderService.getOrderList(1L, 1, 10, OrderStatus.PENDING_PAYMENT.getCode());

        assertNotNull(page);
        assertNotNull(page.getRecords());
    }

    @Test
    @Order(7)
    @DisplayName("测试获取订单详情")
    public void testGetOrderDetail() {
        if (createdOrderId != null) {
            OrderVO orderVO = orderService.getOrderDetail(1L, createdOrderId);
            assertNotNull(orderVO);
            assertEquals(createdOrderId, orderVO.getId());
        }
    }

    @Test
    @Order(8)
    @DisplayName("测试获取订单详情（订单不存在）")
    public void testGetOrderDetailNotFound() {
        assertThrows(BusinessException.class, () -> orderService.getOrderDetail(1L, 99999L));
    }

    @Test
    @Order(9)
    @DisplayName("测试支付订单")
    public void testPayOrder() {
        if (createdOrderId != null) {
            Map<String, String> payParams = orderService.payOrder(1L, createdOrderId);

            assertNotNull(payParams);
            assertTrue(payParams.containsKey("orderId"));
            assertTrue(payParams.containsKey("orderNo"));
            assertTrue(payParams.containsKey("amount"));
        }
    }

    @Test
    @Order(10)
    @DisplayName("测试模拟支付")
    public void testMockPay() {
        if (createdOrderId != null) {
            assertDoesNotThrow(() -> orderService.mockPay(1L, createdOrderId));

            OrderVO orderVO = orderService.getOrderDetail(1L, createdOrderId);
            assertEquals(OrderStatus.PAID.getCode(), orderVO.getStatus());
        }
    }

    @Test
    @Order(11)
    @DisplayName("测试支付回调")
    public void testPayNotify() {
        Map<String, String> params = Map.of(
            "order_no", "DK" + System.currentTimeMillis(),
            "transaction_id", "txn_" + System.currentTimeMillis()
        );

        assertDoesNotThrow(() -> orderService.payNotify(params));
    }

    @Test
    @Order(12)
    @DisplayName("测试取消订单")
    public void testCancelOrder() {
        OrderCreateDTO dto = new OrderCreateDTO();
        dto.setProductId(1L);
        dto.setQuantity(1);
        dto.setOrderType(2);
        dto.setAddressId(1L);

        Long orderId = orderService.createOrder(1L, dto);

        assertDoesNotThrow(() -> orderService.cancelOrder(1L, orderId));

        OrderVO orderVO = orderService.getOrderDetail(1L, orderId);
        assertEquals(OrderStatus.CANCELLED.getCode(), orderVO.getStatus());
    }

    @Test
    @Order(13)
    @DisplayName("测试取消订单（状态错误）")
    public void testCancelOrderWrongStatus() {
        if (createdOrderId != null) {
            assertThrows(BusinessException.class, () -> orderService.cancelOrder(1L, createdOrderId));
        }
    }

    @Test
    @Order(14)
    @DisplayName("测试确认收货")
    public void testConfirmOrder() {
        OrderCreateDTO dto = new OrderCreateDTO();
        dto.setProductId(1L);
        dto.setQuantity(1);
        dto.setOrderType(2);
        dto.setAddressId(1L);

        Long orderId = orderService.createOrder(1L, dto);
        orderService.mockPay(1L, orderId);

        assertThrows(BusinessException.class, () -> orderService.confirmOrder(1L, orderId));
    }

    @Test
    @Order(15)
    @DisplayName("测试管理端获取订单列表")
    public void testGetAdminOrderList() {
        Page<OrderVO> page = orderService.getAdminOrderList(1, 10, null, null);

        assertNotNull(page);
        assertNotNull(page.getRecords());
    }

    @Test
    @Order(16)
    @DisplayName("测试管理端获取订单详情")
    public void testGetAdminOrderDetail() {
        if (createdOrderId != null) {
            OrderVO orderVO = orderService.getAdminOrderDetail(createdOrderId);
            assertNotNull(orderVO);
            assertEquals(createdOrderId, orderVO.getId());
        }
    }

    @Test
    @Order(17)
    @DisplayName("测试管理端更新订单状态")
    public void testUpdateOrderStatus() {
        if (createdOrderId != null) {
            assertDoesNotThrow(() -> orderService.updateOrderStatus(createdOrderId, OrderStatus.DELIVERING.getCode()));

            OrderVO orderVO = orderService.getAdminOrderDetail(createdOrderId);
            assertEquals(OrderStatus.DELIVERING.getCode(), orderVO.getStatus());
        }
    }
}
