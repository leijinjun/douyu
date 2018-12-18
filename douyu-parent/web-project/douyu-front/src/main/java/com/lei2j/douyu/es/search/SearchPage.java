package com.lei2j.douyu.es.search;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author lei2j
 * Created by lei2j on 2018/6/24.
 */
public class SearchPage {

    private QueryBuilder queryBuilder;

    private QueryBuilder filterBuilder;

    /**
     * 是否使用scroll查询符合条件全部数据
     */
    private boolean scroll = false;

    private String scrollId;

    /**
     * 例如：sort=id,nn desc，默认asc
     */
    private String sort;

    public SearchPage() {
    }

    public SearchPage(QueryBuilder queryBuilder, QueryBuilder filterBuilder) {
		super();
		this.queryBuilder = queryBuilder;
		this.filterBuilder = filterBuilder;
	}

    public SearchPage(QueryBuilder queryBuilder, QueryBuilder filterBuilder, boolean scroll, String scrollId) {
        this.queryBuilder = queryBuilder;
        this.filterBuilder = filterBuilder;
        this.scroll = scroll;
        this.scrollId = scrollId;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public QueryBuilder getFilterBuilder() {
        return filterBuilder;
    }

    public void setFilterBuilder(QueryBuilder filterBuilder) {
        this.filterBuilder = filterBuilder;
    }

    public boolean isScroll() {
        return scroll;
    }

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SearchPage{");
        sb.append("queryBuilder=").append(queryBuilder);
        sb.append(", filterBuilder=").append(filterBuilder);
        sb.append(", scroll=").append(scroll);
        sb.append(", scrollId='").append(scrollId).append('\'');
        sb.append(", sort='").append(sort).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
