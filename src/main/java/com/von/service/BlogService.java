package com.von.service;

import com.von.controller.vo.BlogDetailVO;
import com.von.controller.vo.SimpleBlogListVO;
import com.von.entity.Blog;
import com.von.util.PageQueryUtil;
import com.von.util.PageResult;

import java.util.List;

public interface BlogService {
    String saveBlog(Blog blog);

    //根据id获取详情
    Blog getBlogById(Long blogId);

    //后台修改
    String updateBlog(Blog blog);
/*
    列表接口负责接收前端传来的分页参数，如 page 、limit 等参数，
    之后将数据总数和对应页面的数据列表查询出来并封装为分页数据返回给前端。*/
    PageResult getBlogsPage(PageQueryUtil pageUtil);

    Boolean deleteBatch(Integer[] ids);

    List<SimpleBlogListVO> getBlogListForIndexPage(int type);

    /**
     * 获取首页文章列表
     * @param page
     */
    PageResult getBlogsForIndexPage(int page);

    PageResult getBlogsPageBySearch(String keyword, int page);

    BlogDetailVO getBlogDetail(Long blogId);
}
