package com.von.controller.vo;

import java.io.Serializable;
//VO,通常用于业务层之间的数据传递
public class SimpleBlogListVO implements Serializable {

    private Long blogId;
    private String blogTitle;

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
}