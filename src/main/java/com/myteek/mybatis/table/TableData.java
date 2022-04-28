package com.myteek.mybatis.table;

import com.myteek.mybatis.page.PageList;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class TableData<T> {

    private List<T> list;

    private LinkedHashMap<String, Object> thead;

    private int current;

    private int pageSize;

    private int total;

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder<T> {

        private List<T> list;

        private LinkedHashMap<String, Object> thead;

        private int current;

        private int pageSize;

        private int total;

        public Builder list(List<T> list) {
            this.list = list;
            return this;
        }

        public Builder thead(LinkedHashMap<String, Object> thead) {
            this.thead = thead;
            return this;
        }

        public Builder current(int current) {
            this.current = current;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder total(int total) {
            this.total = total;
            return this;
        }

        /**
         * page list
         * @param pageList
         * @return
         */
        public Builder pageList(PageList pageList) {
            this.list = pageList;
            this.current = pageList.getPageNum();
            this.pageSize = pageList.getPageSize();
            this.total = pageList.getTotalRows();
            return this;
        }

        /**
         * builder
         * @return
         */
        public TableData build() {
            TableData ret = new TableData();
            ret.setList(list);
            ret.setThead(thead);
            ret.setCurrent(current);
            ret.setPageSize(pageSize);
            ret.setTotal(total);
            return ret;
        }
    }

}
