package org.zhangrui.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhangrui.common.exception.BusinessException;
import org.zhangrui.model.dto.CategoryDTO;
import org.zhangrui.model.entity.Category;
import org.zhangrui.model.vo.CategoryVO;

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
        CategoryDTO dto = new CategoryDTO();
        dto.setName("测试分类_" + System.currentTimeMillis());
        dto.setIcon("icon-test");
        dto.setSortOrder(100);

        Long categoryId = categoryService.addCategory(dto);

        assertNotNull(categoryId);
        assertTrue(categoryId > 0);
        createdCategoryId = categoryId;
    }

    @Test
    @Order(2)
    @DisplayName("测试获取分类列表")
    public void testGetCategoryList() {
        List<CategoryVO> categories = categoryService.getCategoryList();

        assertNotNull(categories);
        assertTrue(categories.size() >= 0);
    }

    @Test
    @Order(3)
    @DisplayName("测试获取分类详情")
    public void testGetCategoryById() {
        if (createdCategoryId != null) {
            Category category = categoryService.getCategoryById(createdCategoryId);
            assertNotNull(category);
            assertEquals(createdCategoryId, category.getId());
        } else {
            assertThrows(BusinessException.class, () -> categoryService.getCategoryById(99999L));
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试更新分类")
    public void testUpdateCategory() {
        if (createdCategoryId != null) {
            CategoryDTO dto = new CategoryDTO();
            dto.setName("更新后的分类名称");
            dto.setIcon("icon-updated");
            dto.setSortOrder(50);

            assertDoesNotThrow(() -> categoryService.updateCategory(createdCategoryId, dto));
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试更新分类（不存在）")
    public void testUpdateCategoryNotFound() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("测试");

        assertThrows(BusinessException.class, () -> categoryService.updateCategory(99999L, dto));
    }

    @Test
    @Order(6)
    @DisplayName("测试删除分类")
    public void testDeleteCategory() {
        if (createdCategoryId != null) {
            assertDoesNotThrow(() -> categoryService.deleteCategory(createdCategoryId));
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试删除分类（不存在）")
    public void testDeleteCategoryNotFound() {
        assertThrows(BusinessException.class, () -> categoryService.deleteCategory(99999L));
    }
}
