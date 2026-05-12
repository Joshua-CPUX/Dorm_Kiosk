package org.zhangrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhangrui.mapper.CategoryMapper;
import org.zhangrui.model.entity.Category;
import org.zhangrui.service.ICategoryService;

import java.util.List;

/**
 * 商品分类服务实现类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategoryList() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, 1);
        wrapper.orderByAsc(Category::getSort);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public Long addCategory(String name, String icon, Integer sort) {
        Category category = new Category();
        category.setName(name);
        category.setIcon(icon);
        category.setSort(sort != null ? sort : 0);
        category.setStatus(1);
        categoryMapper.insert(category);
        return category.getId();
    }

    @Override
    public void updateCategory(Long id, String name, String icon, Integer sort, Integer status) {
        Category category = categoryMapper.selectById(id);
        if (category != null) {
            if (name != null) {
                category.setName(name);
            }
            if (icon != null) {
                category.setIcon(icon);
            }
            if (sort != null) {
                category.setSort(sort);
            }
            if (status != null) {
                category.setStatus(status);
            }
            categoryMapper.updateById(category);
        }
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }
}
