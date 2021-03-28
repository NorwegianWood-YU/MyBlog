package com.von.dao;

import com.von.entity.BlogTagRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface BlogTagRelationMapper {
    int deleteByPrimaryKey(Long relationId);

    int insert(BlogTagRelation record);

    int insertSelective(BlogTagRelation record);

    BlogTagRelation selectByPrimaryKey(Long relationId);

    int updateByPrimaryKeySelective(BlogTagRelation record);

    int updateByPrimaryKey(BlogTagRelation record);

    //手动加入
    List<Long> selectDistinctTagIds(Integer[] tagIds);

    int batchInsert(@Param("relationList") List<BlogTagRelation> blogTagRelations);

    int deleteByBlogId(Long blogId);
}