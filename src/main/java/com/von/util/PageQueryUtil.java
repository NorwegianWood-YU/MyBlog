package com.von.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分页查询参数
 * 列表接口负责接收前端传来的分页参数，如 page 、limit 等参数，
 * 之后将数据总数(select count(*) from tb)和对应页面的数据列表(select * from tb limit 10,20)查询出来并封装为分页数据返回给前端。
 * 前端将所需页码和条数参数传输给后端，而后端在接受到分页请求后会对分页参数进行计算，并利用 MySQL 的 limit 关键字去查询对应的记录
 */
public class PageQueryUtil extends LinkedHashMap<String, Object> {
    //当前页码
    private int page;
    //每页条数
    private int limit;

    public PageQueryUtil(Map<String, Object> params) {
        this.putAll(params);

        //分页参数
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("start", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "PageUtil{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }
}
