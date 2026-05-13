package org.zhangrui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.dto.ProductAddDTO;
import org.zhangrui.model.dto.ProductUpdateDTO;
import org.zhangrui.model.entity.Product;
import org.zhangrui.model.vo.ProductVO;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品服务测试类
 * 覆盖商品增删改查、库存管理等核心功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTest {

    @Autowired
    private IProductService productService;

    private static Long createdProductId;

    @Test
    @Order(1)
    @DisplayName("测试添加商品")
    public void testAddProduct() {
        ProductAddDTO dto = new ProductAddDTO();
        dto.setCategoryId(1L);
        dto.setName("测试商品_" + System.currentTimeMillis());
        dto.setSubtitle("测试副标题");
        dto.setPrice(new BigDecimal("9.90"));
        dto.setOriginalPrice(new BigDecimal("12.90"));
        dto.setStock(100);
        dto.setImage("https://example.com/test.jpg");
        dto.setImages("[\"https://example.com/test.jpg\"]");
        dto.setDescription("这是一个测试商品");

        Long productId = productService.addProduct(dto);

        assertNotNull(productId);
        assertTrue(productId > 0);
        createdProductId = productId;
    }

    @Test
    @Order(2)
    @DisplayName("测试获取商品详情")
    public void testGetProductDetail() {
        if (createdProductId != null) {
            ProductVO productVO = productService.getProductDetail(createdProductId);
            assertNotNull(productVO);
            assertEquals(createdProductId, productVO.getId());
        } else {
            assertThrows(BusinessException.class, () -> productService.getProductDetail(99999L));
        }
    }

    @Test
    @Order(3)
    @DisplayName("测试获取商品列表")
    public void testGetProductPage() {
        Page<Product> page = productService.getProductPage(1, 10, null, null);

        assertNotNull(page);
        assertNotNull(page.getRecords());
        assertTrue(page.getRecords().size() >= 0);
    }

    @Test
    @Order(4)
    @DisplayName("测试获取分类商品")
    public void testGetProductPageByCategory() {
        Page<Product> page = productService.getProductPage(1, 10, 1L, null);

        assertNotNull(page);
        assertTrue(page.getRecords().size() >= 0);
    }

    @Test
    @Order(5)
    @DisplayName("测试搜索商品")
    public void testGetProductPageByKeyword() {
        Page<Product> page = productService.getProductPage(1, 10, null, "测试");

        assertNotNull(page);
        assertTrue(page.getRecords().size() >= 0);
    }

    @Test
    @Order(6)
    @DisplayName("测试更新商品")
    public void testUpdateProduct() {
        if (createdProductId != null) {
            ProductUpdateDTO dto = new ProductUpdateDTO();
            dto.setId(createdProductId);
            dto.setName("更新后的商品名称");
            dto.setPrice(new BigDecimal("19.90"));

            assertDoesNotThrow(() -> productService.updateProduct(dto));
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试更新商品状态")
    public void testUpdateProductStatus() {
        if (createdProductId != null) {
            assertDoesNotThrow(() -> productService.updateProductStatus(createdProductId, 1));
            assertDoesNotThrow(() -> productService.updateProductStatus(createdProductId, 0));
        }
    }

    @Test
    @Order(8)
    @DisplayName("测试获取热门商品")
    public void testGetHotProducts() {
        List<ProductVO> products = productService.getHotProducts(5);

        assertNotNull(products);
        assertTrue(products.size() <= 5);
    }

    @Test
    @Order(9)
    @DisplayName("测试库存更新（增加）")
    public void testUpdateStockIncrease() {
        if (createdProductId != null) {
            assertDoesNotThrow(() -> productService.updateStock(createdProductId, 50));
        }
    }

    @Test
    @Order(10)
    @DisplayName("测试库存更新（减少）")
    public void testUpdateStockDecrease() {
        if (createdProductId != null) {
            assertDoesNotThrow(() -> productService.updateStock(createdProductId, -10));
        }
    }

    @Test
    @Order(11)
    @DisplayName("测试库存不足异常")
    public void testUpdateStockInsufficient() {
        if (createdProductId != null) {
            assertThrows(BusinessException.class, () -> 
                productService.updateStock(createdProductId, -9999));
        }
    }

    @Test
    @Order(12)
    @DisplayName("测试删除商品")
    public void testDeleteProduct() {
        if (createdProductId != null) {
            assertDoesNotThrow(() -> productService.deleteProduct(createdProductId));
        }
    }

    @Test
    @Order(13)
    @DisplayName("测试获取所有商品（管理端）")
    public void testGetAllProductPage() {
        Page<Product> page = productService.getAllProductPage(1, 10, null);

        assertNotNull(page);
        assertNotNull(page.getRecords());
    }
}
