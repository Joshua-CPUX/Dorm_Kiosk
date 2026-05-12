package org.zhangrui.service;

import org.zhangrui.model.entity.Category;

import java.util.List;

/**
 * 商品分类服务接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
public interface ICategoryService {

    /**
     * 获取分类列表
     *
     * @return 分类列表
     */
    List<Category> getCategoryList();

    /**
     * 添加分类
     *
     * @param name 分类名称
     * @param icon 图标
     * @param sort 排序
     * @return 分类ID
     */
    Long addCategory(String name, String icon, Integer sort);

    /**
     * 更新分类
     *
     * @param id     分类ID
     * @param name   分类名称
     * @param icon   图标
     * @param sort   排序
     * @param status 状态
     */
    void updateCategory(Long id, String name, String icon, Integer sort, Integer status);

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);
}
