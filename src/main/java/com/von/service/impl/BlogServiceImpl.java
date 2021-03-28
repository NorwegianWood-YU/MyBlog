package com.von.service.impl;

import com.von.controller.vo.BlogDetailVO;
import com.von.controller.vo.BlogListVO;
import com.von.controller.vo.SimpleBlogListVO;
import com.von.dao.BlogCategoryMapper;
import com.von.dao.BlogMapper;
import com.von.dao.BlogTagMapper;
import com.von.dao.BlogTagRelationMapper;
import com.von.entity.Blog;
import com.von.entity.BlogCategory;
import com.von.entity.BlogTag;
import com.von.entity.BlogTagRelation;
import com.von.service.BlogService;
import com.von.util.MarkDownUtil;
import com.von.util.PageQueryUtil;
import com.von.util.PageResult;
import com.von.util.PatternUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogCategoryMapper categoryMapper;

    @Autowired
    private BlogTagMapper tagMapper;

    @Autowired
    private BlogTagRelationMapper tagRelationMapper;

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if(blogCategory == null){
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        }else{
            //设置博客分类名称
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            //分类的排序值+1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if(tags.length > 6){
            return "标签数量限制为6";
        }
        //保存文章
        if(blogMapper.insertSelective(blog) > 0){
            //新增的tag对象
            List<BlogTag> tagListForInsert = new ArrayList<>();
            //所有的tag对象，用于建立blogTagRelation数据
            List<BlogTag> allTagsList = new ArrayList<>();
            for(int i = 0; i < tags.length; i++){
                BlogTag tag = tagMapper.selectByTagName(tags[i]);
                if(tag == null){
                    //不存在就先保存起来，然后新增
                    BlogTag tmpTag = new BlogTag();
                    tmpTag.setTagName(tags[i]);
                    tagListForInsert.add(tmpTag);
                }else {
                    allTagsList.add(tag);
                }
            }
            //新增标签数据并修改分类排序值
            if(!CollectionUtils.isEmpty(tagListForInsert)){
                tagMapper.batchInsertBlogTag(tagListForInsert);
            }
            //因为blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1)

            categoryMapper.updateByPrimaryKeySelective(blogCategory);
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            //新增关系数据
            allTagsList.addAll(tagListForInsert);
            for(BlogTag tag : allTagsList){
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                //本文章特有的id
                blogTagRelation.setBlogId(blog.getBlogId());
                blogTagRelation.setTagId(tag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            if(tagRelationMapper.batchInsert(blogTagRelations) > 0){
                return "success";
            }
        }
        return "保存失败";
    }


    @Override
    public Blog getBlogById(Long blogId) {
        return blogMapper.selectByPrimaryKey(blogId);
    }

    @Override
    @Transactional
    public String updateBlog(Blog blog) {
        Blog blogForUpdate = blogMapper.selectByPrimaryKey(blog.getBlogId());
        if(blogForUpdate == null)   return "数据不存在";
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setEnableComment(blog.getEnableComment());
        BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if(blogCategory == null){
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        }else {
            //设置博客分类名称
            blogForUpdate.setBlogCategoryName(blogCategory.getCategoryName());
            blogForUpdate.setBlogCategoryId(blogCategory.getCategoryId());
            //分类的排序值+1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if(tags.length > 6){
            return "标签数量限制为6";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());
        //新增的tag对象
        List<BlogTag> tagListForInsert = new ArrayList<>();
        //所有的tag对象，用于建立关系数据
        List<BlogTag> allTagsList = new ArrayList<>();
        for(int i = 0; i < tags.length; i++){
            BlogTag tag = tagMapper.selectByTagName(tags[i]);
            if(tag == null){
                BlogTag tmpTag = new BlogTag();
                tmpTag.setTagName(tags[i]);
                tagListForInsert.add(tmpTag);
            }else {
                allTagsList.add(tag);
            }
        }
        //新增标签数据不为空->新增标签数据
        if(!CollectionUtils.isEmpty(tagListForInsert)){
            tagMapper.batchInsertBlogTag(tagListForInsert);
        }
        List<BlogTagRelation> blogTagRelations = new ArrayList<>();
        //新增关系数据
        allTagsList.addAll(tagListForInsert);
        for(BlogTag tag : allTagsList){
            BlogTagRelation blogTagRelation = new BlogTagRelation();
            blogTagRelation.setBlogId(blog.getBlogId());
            blogTagRelation.setTagId(tag.getTagId());
            blogTagRelations.add(blogTagRelation);
        }
        //修改blog信息->修改分类排序值->删除原关系数据->保存新的关系数据
        categoryMapper.updateByPrimaryKeySelective(blogCategory);
        tagRelationMapper.deleteByBlogId(blog.getBlogId());
        if (blogMapper.updateByPrimaryKeySelective(blog) > 0){
            return "success";
        }
        return "失败";
    }

    @Override
    public PageResult getBlogsPage(PageQueryUtil pageUtil) {
        List<Blog> blogList = blogMapper.findBlogList(pageUtil);
        int total = blogMapper.getTotalBlogs(pageUtil);
        PageResult pageResult = new PageResult(blogList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if(ids.length < 1){
            return false;
        }
        return blogMapper.deleteBatch(ids) > 0;
    }

    @Override
    //
    /*
      SimpleBlogListVO params: blogId, blogTitle
      根据 type 字段的不同去查询对应的博客列表
      type 等于 0 时为查询点击最多的博客列表，type 等于 1 时为查询最新发布的博客列表
    * */
    public List<SimpleBlogListVO> getBlogListForIndexPage(int type) {
        List<SimpleBlogListVO> simpleBlogListVOS = new ArrayList<>();
        List<Blog> blogs = blogMapper.findBlogListByType(type, 9);
        if(!CollectionUtils.isEmpty(blogs)){
            for(Blog blog : blogs){
                SimpleBlogListVO simpleBlogListVO = new SimpleBlogListVO();
                //Blog 类型的数据转换为 SimpleBlogListVO
                BeanUtils.copyProperties(blog, simpleBlogListVO);
                simpleBlogListVOS.add(simpleBlogListVO);
            }
        }
        return simpleBlogListVOS;
    }

    /**
    * @Description:  首页博客列表和分页功能,填充的数据就是博客列表和分页按钮以及跳转逻辑
    * @Param: [page]
    * @return: com.von.util.PageResult
    * @Author: YU
    * @Date: 2021/2/11
    */
    @Override
    public PageResult getBlogsForIndexPage(int page) {
        Map params = new HashMap();
        params.put("page", page);
        //每页8条
        params.put("limit", 8);
        params.put("blogStatus", 1);//过滤发布状态下的数据
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        List<Blog> blogList = blogMapper.findBlogList(pageQueryUtil);
        List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(blogList);
        int total = blogMapper.getTotalBlogs(pageQueryUtil);
        PageResult pageResult = new PageResult(blogListVOS, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    /** 
    * @Description: 根据搜索关键字获取首页文章列表 
    * @Param: [keyword, page] 
    * @return: com.von.util.PageResult 
    * @Author: YU
    * @Date: 2021/2/11 
    */
    @Override
    public PageResult getBlogsPageBySearch(String keyword, int page) {
        if(page > 0 && PatternUtil.validKeyword(keyword)){
            Map param = new HashMap();
            param.put("page", page);
            param.put("limit", 9);
            param.put("keyword", keyword);
            param.put("blogStatus", 1);//过滤发布状态下的数据
            PageQueryUtil pageQueryUtil = new PageQueryUtil(param);
            List<Blog> blogList = blogMapper.findBlogList(pageQueryUtil);
            //数据填充，将blog entity中的属性复制到blogList
            List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(blogList);
            int total = blogMapper.getTotalBlogs(pageQueryUtil);
            PageResult pageResult = new PageResult(blogListVOS, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
            return pageResult;
        }
        return null;
    }

    /**
     * 数据填充,还缺个分类封面
     */
    private List<BlogListVO> getBlogListVOsByBlogs(List<Blog> blogList) {
        List<BlogListVO> blogListVOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(blogList)){
            List<Integer> categoryIds = blogList.stream().map(Blog::getBlogCategoryId).collect(Collectors.toList());
            Map<Integer, String> blogCategoryMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(categoryIds)){
                List<BlogCategory> blogCategories = categoryMapper.selectByCategoryIds(categoryIds);
                if(!CollectionUtils.isEmpty(blogCategories)){
                    blogCategoryMap = blogCategories.stream().collect(Collectors.toMap(BlogCategory::getCategoryId, BlogCategory::getCategoryIcon, (key1, key2) -> key2));
                }
            }
            for(Blog blog : blogList){
                BlogListVO blogListVO = new BlogListVO();
                BeanUtils.copyProperties(blog, blogListVO);
                if (blogCategoryMap.containsKey(blog.getBlogCategoryId())) {
                    blogListVO.setBlogCategoryIcon(blogCategoryMap.get(blog.getBlogCategoryId()));
                } else {
                    blogListVO.setBlogCategoryId(0);
                    blogListVO.setBlogCategoryName("默认分类");
                    blogListVO.setBlogCategoryIcon("/admin/dist/img/category/1.png");
                }
                blogListVOS.add(blogListVO);
            }
        }
        return blogListVOS;
    }



    /**
     * 文章详情获取
     *
     * @param blogId
     * @return
     */
    public BlogDetailVO getBlogDetail(Long blogId) {
        Blog blog = blogMapper.selectByPrimaryKey(blogId);
        //不为空且状态为已发布
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null) {
            return blogDetailVO;
        }
        return null;
    }

    /**
     * 方法抽取
     *
     * @param blog
     * @return
     */
    private BlogDetailVO getBlogDetailVO(Blog blog) {
        //判空以及发布状态是否为已发布
        if (blog != null && blog.getBlogStatus() == 1) {
            //增加浏览量
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.updateByPrimaryKey(blog);
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            //md格式转换
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));
            BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
            if (blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            //分类信息
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            if (!StringUtils.isEmpty(blog.getBlogTags())) {
                //标签设置
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVO.setBlogTags(tags);
            }
            return blogDetailVO;
        }
        return null;
    }
}



















