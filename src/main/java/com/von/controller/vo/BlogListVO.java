package com.von.controller.vo;

import java.io.Serializable;
//首页博客列表和分页功能
public class BlogListVO implements Serializable {
    //返回数据的格式
    private Long blogId;

    private String blogTitle;

    private String blogCoverImage;

    private Integer blogCategoryId;

    private String blogCategoryIcon;

    private String blogCategoryName;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogCoverImage() {
        return blogCoverImage;
    }

    public void setBlogCoverImage(String blogCoverImage) {
        this.blogCoverImage = blogCoverImage;
    }

    public Integer getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(Integer blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public String getBlogCategoryIcon() {
        return blogCategoryIcon;
    }

    public void setBlogCategoryIcon(String blogCategoryIcon) {
        this.blogCategoryIcon = blogCategoryIcon;
    }

    public String getBlogCategoryName() {
        return blogCategoryName;
    }

    public void setBlogCategoryName(String blogCategoryName) {
        this.blogCategoryName = blogCategoryName;
    }
}
