package com.myteek.mybatis.page;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@ToString
public class PageResult<T> implements Serializable {

    private int pageSize;
    private int pageNum;
    private int totalPage;
    private int totalRows;

    private List<T> data;

    /**
     * constructor
     * @param list page list
     */
    public PageResult(PageList<T> list) {
        this.pageSize = list.getPageSize();
        this.pageNum = list.getPageNum();
        this.totalPage = list.getTotalPage();
        this.totalRows = list.getTotalRows();
        this.data = list;
    }

}
