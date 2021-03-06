package com.von.service;

import com.von.entity.BlogLink;
import com.von.util.PageQueryUtil;
import com.von.util.PageResult;

public interface LinkService {
    //查询友链的分页数据

    PageResult getBlogLinkPage(PageQueryUtil pageUtil);

    Boolean saveLink(BlogLink link);

    BlogLink selectById(Integer id);

    Boolean updateLink(BlogLink tempLink);

    Boolean deleteBatch(Integer[] ids);
}
