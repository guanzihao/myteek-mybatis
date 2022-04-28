package com.myteek.mybatis.page;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class PageList<T> extends ArrayList<T> {

    private int pageSize;
    private int pageNum;
    private int totalRows;

    /**
     * constructor
     * @param list list model
     * @param pageNum page number
     * @param pageSize page size
     * @param totalRows total rows
     */
    public PageList(List<T> list, int pageNum, int pageSize, int totalRows) {
        this.addAll(list);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalRows = totalRows;
    }

    public int getTotalPage() {
        return totalRows % pageSize == 0 ? totalRows / pageSize : totalRows / pageSize + 1;
    }

}
