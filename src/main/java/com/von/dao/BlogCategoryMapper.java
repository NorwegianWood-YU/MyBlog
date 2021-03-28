package com.von.dao;

import com.von.entity.BlogCategory;
import com.von.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface BlogCategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(BlogCategory record);

    int insertSelective(BlogCategory record);

    BlogCategory selectByPrimaryKey(Integer categoryId);

    int updateByPrimaryKeySelective(BlogCategory record);

    int updateByPrimaryKey(BlogCategory record);

    //以下为手动添加

    BlogCategory selectByCategoryName(String categoryName);

    //一般的分页也就是使用 limit 关键字实现，获取响应条数的记录和总数之后再进行数据封装，这
    // 个接口就是根据前端传的分页参数进行查询并返回分页数据以供前端页面进行数据渲染。
    List<BlogCategory> findCategoryList(PageQueryUtil pageQueryUtil);

    List<BlogCategory> selectByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

    int getTotalCategories(PageQueryUtil pageQueryUtil);

    int deleteBatch(Integer[] ids);

}