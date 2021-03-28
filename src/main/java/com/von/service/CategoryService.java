package com.von.service;

import com.von.entity.BlogCategory;
import com.von.util.PageQueryUtil;
import com.von.util.PageResult;

import java.util.List;

public interface CategoryService {

    // 查询分类的分页数据
    PageResult getBlogCategoryPage(PageQueryUtil pageUtil);

    int getTotalCategories();

    Boolean saveCategory(String categoryName, String categoryIcon);

    Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);

    Boolean deleteBatch(Integer[] ids);

    List<BlogCategory> getAllCategories();

    BlogCategory selectById(Integer id);
}
