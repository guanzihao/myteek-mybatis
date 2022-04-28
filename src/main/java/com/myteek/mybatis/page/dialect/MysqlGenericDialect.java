package com.myteek.mybatis.page.dialect;

import com.myteek.mybatis.page.OrderType;
import com.myteek.mybatis.page.Util;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;
import java.util.stream.Collectors;

public class MysqlGenericDialect extends GenericDialect {

    @Override
    public String getCountSql(String sql) {
        return Util.parseSql(sql);
    }

    @Override
    public String getPageSql(String sql, RowBounds rowBounds, CacheKey pageKey, Map<String, OrderType> orders) {
        pageKey.update(rowBounds.getLimit());
        if (sql.toLowerCase().indexOf("order by") == -1 && orders.size() > 0) {
            String orderSql = orders.entrySet().stream().map(item ->
                item.getKey() + " " + item.getValue().toString().toLowerCase()
            ).collect(Collectors.joining(", "));
            if (orderSql.length() > 0) {
                sql += " order by " + orderSql;
            }
        }
        return sql + " limit " + rowBounds.getOffset() + ", " + rowBounds.getLimit();
    }

}
