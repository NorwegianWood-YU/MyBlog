package com.von.controller.blog;

import com.von.controller.vo.BlogDetailVO;
import com.von.service.BlogService;
import com.von.service.TagService;
import com.von.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class MyBlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    Logger logger = Logger.getLogger(String.valueOf(getClass()));

    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request){
        return this.page(request, 1);
    }



    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum){
        PageResult blogPageResult = blogService.getBlogsForIndexPage(pageNum);
        if(blogPageResult == null){
            return "error/error_404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("pageName", "首页");
        return "blog/index";
    }


    @GetMapping({"/search/{keyword}"})
    String search(HttpServletRequest request, @PathVariable("keyword") String keyword){
        return search(request, keyword, 1);
    }

    /**
     * @Description: 搜索列表页，page 参数不传的话默认为第 1 页，跳转到 list 模板页面进行数据渲染
     * @Param: [request, keyword, page]
     * @return: java.lang.String
     * @Author: YU
     * @Date: 2021/2/11
     */
    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page){
        PageResult blogPageResult = blogService.getBlogsPageBySearch(keyword, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        return "blog/list";
    }


    /**
     * 详情页
     *
     * @return
     */
    @GetMapping("/blog/{blogId}")
    public String detail(HttpServletRequest request, @PathVariable("blogId") Long blogId) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
        }
        request.setAttribute("pageName", "详情");
        return "blog/detail";
    }
}
