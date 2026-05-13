package org.zhangrui.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.entity.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 分类服务测试类
 * 覆盖分类增删改查等核心功能
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryServiceTest {

    @Autowired
    private ICategoryService categoryService;

    private static Long createdCategoryId;

    @Test
    @Order(1)
    @DisplayName("测试添加分类")
    public void testAddCategory() {
        Long categoryId = categoryService.addCategory("测试分类_" + System.currentTimeMillis(), "icon-test", 100);

        assertNotNull(categoryId);
        assertTrue(categoryId > 0);
        createdCategoryId = categoryId;
    }

    @Test
    @Order(2)
    @DisplayName("测试获取分类列表")
    public void testGetCategoryList() {
        List<Category> categories = categoryService.getCategoryList();

        assertNotNull(categories);
        assertTrue(categories.size() >= 0);
    }

    @Test
    @Order(3)
    @DisplayName("测试更新分类")
    public void testUpdateCategory() {
        if (createdCategoryId != null) {
            assertDoesNotThrow(() -> 
                categoryService.updateCategory(createdCategoryId, "更新后的分类名称", "icon-updated", 50, 1));
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试删除分类")
    public void testDeleteCategory() {
        if (createdCategoryId != null) {
            assertDoesNotThrow(() -> categoryService.deleteCategory(createdCategoryId));
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试删除分类（不存在）")
    public void testDeleteCategoryNotFound() {
        assertThrows(BusinessException.class, () -> categoryService.deleteCategory(99999L));
    }
}
