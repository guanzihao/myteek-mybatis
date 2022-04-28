package com.myteek.mybatis.page.dialect;

import com.myteek.mybatis.page.OrderType;
import com.myteek.mybatis.page.Page;
import com.myteek.mybatis.page.PageList;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface Dialect {

    boolean canBePaged(MappedStatement ms, String sql, Object parameter);

    Page getPageParameter(Object parameter);

    BoundSql getCountSql(MappedStatement ms, BoundSql boundSql, Object parameter,
                         Map<String, Object> additionalParameters);

    BoundSql getPageSql(MappedStatement ms, BoundSql boundSql, Object parameter,
                        CacheKey pageKey, RowBounds rowBounds, Map<String, Object> additionalParameters,
                        Map<String, OrderType> orders);

    PageList buildPageList(List resultList, Page page, int totalRows);

}
