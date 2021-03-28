package com.von.dao;

import com.von.entity.Blog;
import com.von.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BlogMapper {
    int deleteByPrimaryKey(Long blogId);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Long blogId);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKeyWithBLOBs(Blog record);

    int updateByPrimaryKey(Blog record);

    //手写sql
    List<Blog> findBlogList(PageQueryUtil pageUtil);

    int getTotalBlogs(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);

    /**
     * 首页侧边栏数据列表
     * type = 0-点击最多, type =  1-最新发布
     * @param type
     */
    List<Blog> findBlogListByType(@Param("type") int type, @Param("limit") int limit);



}