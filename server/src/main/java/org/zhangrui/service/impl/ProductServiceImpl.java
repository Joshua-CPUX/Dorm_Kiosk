package org.zhangrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.mapper.CategoryMapper;
import org.zhangrui.mapper.ProductMapper;
import org.zhangrui.model.entity.Category;
import org.zhangrui.model.entity.Product;
import org.zhangrui.model.dto.ProductAddDTO;
import org.zhangrui.model.dto.ProductUpdateDTO;
import org.zhangrui.model.vo.ProductVO;
import org.zhangrui.service.ICategoryService;
import org.zhangrui.service.IProductService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ICategoryService categoryService;

    @Override
    public Page<Product> getProductPage(Integer pageNum, Integer pageSize, Long categoryId, String keyword) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword);
        }
        wrapper.orderByDesc(Product::getSales).orderByDesc(Product::getCreateTime);
        return productMapper.selectPage(page, wrapper);
    }

    @Override
    public ProductVO getProductDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null || product.getStatus() != 1) {
            throw new BusinessException("商品不存在或已下架");
        }
        return convertToVO(product);
    }

    @Override
    public List<ProductVO> getHotProducts(Integer limit) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        wrapper.orderByDesc(Product::getSales);
        wrapper.last("LIMIT " + limit);
        List<Product> products = productMapper.selectList(wrapper);
        return products.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Long addProduct(ProductAddDTO dto) {
        Product product = new Product();
        product.setCategoryId(dto.getCategoryId());
        product.setName(dto.getName());
        product.setSubtitle(dto.getSubtitle());
        product.setPrice(dto.getPrice());
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setStock(dto.getStock());
        product.setImage(dto.getImage());
        product.setImages(dto.getImages());
        product.setDescription(dto.getDescription());
        product.setStatus(1);
        product.setVersion(0);
        productMapper.insert(product);
        return product.getId();
    }

    @Override
    public void updateProduct(ProductUpdateDTO dto) {
        Product product = productMapper.selectById(dto.getId());
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        if (dto.getCategoryId() != null) {
            product.setCategoryId(dto.getCategoryId());
        }
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getSubtitle() != null) {
            product.setSubtitle(dto.getSubtitle());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getOriginalPrice() != null) {
            product.setOriginalPrice(dto.getOriginalPrice());
        }
        if (dto.getStock() != null) {
            product.setStock(dto.getStock());
        }
        if (dto.getSales() != null) {
            product.setSales(dto.getSales());
        }
        if (dto.getImage() != null) {
            product.setImage(dto.getImage());
        }
        if (dto.getImages() != null) {
            product.setImages(dto.getImages());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            product.setStatus(dto.getStatus());
        }
        productMapper.updateById(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public void updateProductStatus(Long id, Integer status) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        product.setStatus(status);
        productMapper.updateById(product);
    }

    @Override
    public void updateStock(Long id, Integer quantity) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }

        if (quantity < 0) {
            int newStock = product.getStock() + quantity;
            if (newStock < 0) {
                throw new BusinessException("库存不足");
            }
            LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Product::getId, id)
                    .eq(Product::getVersion, product.getVersion())
                    .ge(Product::getStock, -quantity)
                    .setSql("stock = stock + " + quantity)
                    .setSql("version = version + 1");

            int updated = productMapper.update(null, wrapper);
            if (updated == 0) {
                throw new BusinessException("库存不足或并发更新，请重试");
            }
        } else {
            LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Product::getId, id)
                    .eq(Product::getVersion, product.getVersion())
                    .setSql("stock = stock + " + quantity)
                    .setSql("version = version + 1");

            int updated = productMapper.update(null, wrapper);
            if (updated == 0) {
                throw new BusinessException("并发更新，请重试");
            }
        }
    }

    @Override
    public Page<Product> getAllProductPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword);
        }
        wrapper.orderByDesc(Product::getCreateTime);
        return productMapper.selectPage(page, wrapper);
    }

    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);

        Category category = categoryMapper.selectById(product.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        return vo;
    }
}
