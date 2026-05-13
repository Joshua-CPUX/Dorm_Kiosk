package org.zhangrui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.zhangrui.model.entity.Cart;

/**
 * 购物车Mapper接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    @Select("SELECT * FROM oms_cart WHERE user_id = #{userId} AND product_id = #{productId} LIMIT 1")
    Cart selectByUserIdAndProductIdIncludingDeleted(Long userId, Long productId);

    @Update("UPDATE oms_cart SET deleted = 0, quantity = #{quantity}, update_time = NOW() WHERE id = #{id}")
    void recoverById(Long id, Integer quantity);
}
