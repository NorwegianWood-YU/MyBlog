package com.von.dao;

import com.von.entity.Blog;
import com.von.entity.BlogTag;
import com.von.entity.BlogTagCount;
import com.von.util.PageQueryUtil;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface BlogTagMapper {
    int deleteByPrimaryKey(Integer tagId);

    int insert(BlogTag record);

    int insertSelective(BlogTag record);

    BlogTag selectByPrimaryKey(Integer tagId);

    int updateByPrimaryKeySelective(BlogTag record);

    int updateByPrimaryKey(BlogTag record);

    //以下为手动加入的方法
    List<BlogTag> findTagList(PageQueryUtil pageUtil);

    BlogTag selectByTagName(String tagName);

    int getTotalTags(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);

    void batchInsertBlogTag(List<BlogTag> tagListForInsert);

    List<BlogTagCount> getTagCount();
}