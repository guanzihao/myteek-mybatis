package com.myteek.mybatis.page;

import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Page implements Serializable {

    private int pageSize;

    private int pageNum;

    private Map<String, OrderType> orders = new LinkedHashMap<>();

    public Page() {
    }

    public Page(int pageNum) {
        this.pageNum = pageNum;
    }

    public Page(int pageSize, int pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public int getPageNum() {
        return pageNum > 0 ? pageNum : 1;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize > 0 ? pageSize : Constant.PAGE_SIZE;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * to row bounds
     * @return RowBounds
     */
    public RowBounds toRowBounds() {
        int offset = (getPageNum() - 1) * getPageSize();
        int limit = getPageSize();
        return new RowBounds(offset, limit);
    }

    public Map<String, OrderType> getOrders() {
        return Collections.unmodifiableMap(orders);
    }

    public void setOrders(Map<String, OrderType> orders) {
        this.orders = orders;
    }

    /**
     * set order
     * @param orders orders
     * @param orderType order type
     */
    public void setOrder(List<String> orders, OrderType orderType) {
        orders.forEach(item -> setOrder(item, orderType));
    }

    public void setOrder(String order, OrderType orderType) {
        this.orders.put(order, orderType);
    }

}
