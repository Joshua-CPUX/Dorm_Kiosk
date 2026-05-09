package org.zhangrui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhangrui.model.dto.OrderCreateDTO;
import org.zhangrui.model.vo.OrderVO;

import java.util.Map;

/**
 * 订单服务接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface IOrderService {

    /**
     * 创建订单
     *
     * @param userId 用户ID
     * @param dto    订单信息
     * @return 订单ID
     */
    Long createOrder(Long userId, OrderCreateDTO dto);

    /**
     * 支付订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 支付参数
     */
    Map<String, String> payOrder(Long userId, Long orderId);

    /**
     * 模拟支付（测试用）
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     */
    void mockPay(Long userId, Long orderId);

    /**
     * 支付回调
     *
     * @param params 回调参数
     */
    void payNotify(Map<String, String> params);

    /**
     * 获取订单列表
     *
     * @param userId   用户ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param status   订单状态
     * @return 订单分页
     */
    Page<OrderVO> getOrderList(Long userId, Integer pageNum, Integer pageSize, Integer status);

    /**
     * 获取订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderVO getOrderDetail(Long userId, Long orderId);

    /**
     * 取消订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 确认收货
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     */
    void confirmOrder(Long userId, Long orderId);

    /**
     * 管理端：获取订单列表
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param status   订单状态
     * @return 订单分页
     */
    Page<OrderVO> getAdminOrderList(Integer pageNum, Integer pageSize, Integer status);

    /**
     * 管理端：获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderVO getAdminOrderDetail(Long orderId);

    /**
     * 管理端：更新订单状态
     *
     * @param orderId 订单ID
     * @param status  订单状态
     */
    void updateOrderStatus(Long orderId, Integer status);
}
