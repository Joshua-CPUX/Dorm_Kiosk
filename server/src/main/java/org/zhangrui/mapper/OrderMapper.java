package org.zhangrui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhangrui.model.entity.Order;

/**
 * 订单Mapper接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    Page<Order> selectOrderPage(Page<Order> page, @Param("userId") Long userId,
                                @Param("status") Integer status);

    Page<Order> selectAdminOrderPage(Page<Order> page, @Param("status") Integer status);
}
