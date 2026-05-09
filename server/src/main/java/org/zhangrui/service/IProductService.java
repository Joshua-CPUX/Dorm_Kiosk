package org.zhangrui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhangrui.model.dto.ProductAddDTO;
import org.zhangrui.model.dto.ProductUpdateDTO;
import org.zhangrui.model.entity.Product;
import org.zhangrui.model.vo.ProductVO;

import java.util.List;

/**
 * 商品服务接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface IProductService {

    /**
     * 分页获取商品列表
     *
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @param categoryId 分类ID
     * @param keyword    关键词
     * @return 分页结果
     */
    Page<Product> getProductPage(Integer pageNum, Integer pageSize, Long categoryId, String keyword);

    /**
     * 获取商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    ProductVO getProductDetail(Long id);

    /**
     * 获取热门商品
     *
     * @param limit 数量
     * @return 商品列表
     */
    List<ProductVO> getHotProducts(Integer limit);

    /**
     * 添加商品
     *
     * @param dto 商品信息
     * @return 商品ID
     */
    Long addProduct(ProductAddDTO dto);

    /**
     * 更新商品
     *
     * @param dto 商品信息
     */
    void updateProduct(ProductUpdateDTO dto);

    /**
     * 删除商品
     *
     * @param id 商品ID
     */
    void deleteProduct(Long id);

    /**
     * 更新商品状态
     *
     * @param id     商品ID
     * @param status 状态
     */
    void updateProductStatus(Long id, Integer status);

    /**
     * 更新库存
     *
     * @param id       商品ID
     * @param quantity 数量变化
     */
    void updateStock(Long id, Integer quantity);
}
