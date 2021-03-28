package com.von.service;

import com.von.entity.BlogTagCount;
import com.von.util.PageQueryUtil;
import com.von.util.PageResult;

import java.util.List;

public interface TagService {

    //查询标签的分页数据
    PageResult getBlogTagPage(PageQueryUtil pageUtil);

    Boolean saveTag(String tagName);

    Boolean deleteBatch(Integer[] ids);

    //查询tag数据并返回给index首页
    List<BlogTagCount> getBlogTagCountForIndex();
}
