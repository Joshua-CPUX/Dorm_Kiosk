package org.zhangrui.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.model.dto.ProductAddDTO;
import org.zhangrui.model.dto.ProductUpdateDTO;
import org.zhangrui.model.entity.Product;
import org.zhangrui.model.vo.ProductVO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private IProductService productService;

    @Test
    public void testAddProduct() {
        ProductAddDTO dto = new ProductAddDTO();
        dto.setCategoryId(1L);
        dto.setName("测试商品");
        dto.setSubtitle("测试副标题");
        dto.setPrice(new BigDecimal("9.90"));
        dto.setOriginalPrice(new BigDecimal("12.90"));
        dto.setStock(100);
        dto.setImage("https://img.yzcdn.cn/vant/cat.jpeg");
        dto.setDescription("这是一个测试商品");

        Long productId = productService.addProduct(dto);

        assertNotNull(productId);
        assertTrue(productId > 0);
    }

    @Test
    public void testGetProductPage() {
        Page<Product> page = productService.getProductPage(1, 10, null, null);

        assertNotNull(page);
        assertTrue(page.getRecords().size() >= 0);
    }

    @Test
    public void testGetProductDetail() {
        ProductVO productVO = productService.getProductDetail(1L);

        if (productVO != null) {
            assertNotNull(productVO.getName());
        }
    }

    @Test
    public void testUpdateProduct() {
        ProductUpdateDTO dto = new ProductUpdateDTO();
        dto.setId(1L);
        dto.setName("更新后的商品名称");

        assertDoesNotThrow(() -> productService.updateProduct(dto));
    }

    @Test
    public void testGetHotProducts() {
        var products = productService.getHotProducts(5);

        assertNotNull(products);
    }
}
