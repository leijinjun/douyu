package com.lei2j.douyu.core.pojo;

/**
 * @author lei2j
 */
public class BaseEntity {

    /**
     * 例如：sort=id,nn desc，默认asc
     */
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
