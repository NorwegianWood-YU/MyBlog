package com.von.service.impl;

import com.von.dao.BlogCategoryMapper;
import com.von.entity.BlogCategory;
import com.von.service.CategoryService;
import com.von.util.PageQueryUtil;
import com.von.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    //传入MAP对象pageUtil，它包含两个参数start、limit，根据两个参数取数据
    @Override
    public PageResult getBlogCategoryPage(PageQueryUtil pageUtil) {
        List<BlogCategory> categoryList = blogCategoryMapper.findCategoryList(pageUtil);
        int total = blogCategoryMapper.getTotalCategories(pageUtil);
        PageResult pageResult = new PageResult(categoryList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public int getTotalCategories() {
        //不传入参数（即没有加入start和limit），直接查出所有的数据
        return blogCategoryMapper.getTotalCategories(null);
    }

    //插入一条分类
    @Override
    public Boolean saveCategory(String categoryName, String categoryIcon) {
        BlogCategory tmp = blogCategoryMapper.selectByCategoryName(categoryName);
        if(tmp == null){
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            return blogCategoryMapper.insertSelective(blogCategory) > 0;
        }
        return false;
    }
    //更新一条分类
    @Override
    @Transactional
    public Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        BlogCategory blogCategory = blogCategoryMapper.selectByPrimaryKey(categoryId);
        if(blogCategory != null){
            blogCategory.setCategoryIcon(categoryIcon);
            blogCategory.setCategoryName(categoryName);
            return blogCategoryMapper.updateByPrimaryKeySelective(blogCategory) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteBatch(Integer[] ids) {
        if(ids.length < 1){
            return false;
        }
        //删除分类数据
        return blogCategoryMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<BlogCategory> getAllCategories() {
        return blogCategoryMapper.findCategoryList(null);
    }

    @Override
    public BlogCategory selectById(Integer id) {
        return blogCategoryMapper.selectByPrimaryKey(id);
    }
}
