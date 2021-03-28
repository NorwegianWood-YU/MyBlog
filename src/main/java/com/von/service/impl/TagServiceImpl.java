package com.von.service.impl;

import com.von.dao.BlogTagMapper;
import com.von.dao.BlogTagRelationMapper;
import com.von.entity.BlogTag;
import com.von.entity.BlogTagCount;
import com.von.service.TagService;
import com.von.util.PageQueryUtil;
import com.von.util.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private BlogTagRelationMapper relationMapper;
    final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public PageResult getBlogTagPage(PageQueryUtil pageUtil) {
        List<BlogTag> tags = blogTagMapper.findTagList(pageUtil);
        int total = blogTagMapper.getTotalTags(pageUtil);
        PageResult pageResult = new PageResult(tags, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    //如果tag存在则返回false
    @Override
    public Boolean saveTag(String tagName) {
        BlogTag tmp = blogTagMapper.selectByTagName(tagName);
        if(tmp == null){
            BlogTag blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            return blogTagMapper.insertSelective(blogTag) > 0;
        }
        return false;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        //已和书建立过关系的tag不删除
        List<Long> relations = relationMapper.selectDistinctTagIds(ids);
        if(!CollectionUtils.isEmpty(relations)){
            return false;

        }
        //删除tag
        return blogTagMapper.deleteBatch(ids) > 0;
    }

    //查询当前使用最多的 20 个标签数据
    @Override
    public List<BlogTagCount> getBlogTagCountForIndex() {
        return blogTagMapper.getTagCount();
    }
}
