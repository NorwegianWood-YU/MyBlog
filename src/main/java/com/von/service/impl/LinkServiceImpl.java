package com.von.service.impl;

import com.von.dao.BlogLinkMapper;
import com.von.entity.BlogLink;
import com.von.service.LinkService;
import com.von.util.PageQueryUtil;
import com.von.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LinkServiceImpl implements LinkService {
    @Autowired
    private BlogLinkMapper blogLinkMapper;

    @Override
    public PageResult getBlogLinkPage(PageQueryUtil pageUtil) {
        List<BlogLink> links = blogLinkMapper.findLinkList(pageUtil);
        int total = blogLinkMapper.getTotalLinks(pageUtil);
        PageResult pageResult = new PageResult(links, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean saveLink(BlogLink link) {
        return blogLinkMapper.insertSelective(link) > 0;
    }

    @Override
    public BlogLink selectById(Integer id) {
        return blogLinkMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateLink(BlogLink tempLink) {
        return blogLinkMapper.updateByPrimaryKeySelective(tempLink) > 0;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogLinkMapper.deleteBatch(ids) > 0;
    }
}
