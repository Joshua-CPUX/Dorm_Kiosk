package org.zhangrui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhangrui.model.entity.Product;

/**
 * 商品Mapper接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    Page<Product> selectProductPage(Page<Product> page, @Param("categoryId") Long categoryId,
                                    @Param("keyword") String keyword, @Param("status") Integer status);
}
