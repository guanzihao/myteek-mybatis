package com.myteek.mybatis.page.dialect;

import com.myteek.mybatis.page.Constant;
import com.myteek.mybatis.page.OrderType;
import com.myteek.mybatis.page.Page;
import com.myteek.mybatis.page.PageList;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class GenericDialect implements Dialect {

    private static final Logger logger = LoggerFactory.getLogger(GenericDialect.class);

    /**
     * get dialect instance
     * @param properties properties
     * @return Dialect
     * @throws NoSuchMethodException NoSuchMethodException
     * @throws SecurityException SecurityException
     * @throws IllegalAccessException IllegalAccessException
     * @throws InstantiationException InstantiationException
     * @throws IllegalArgumentException IllegalArgumentException
     * @throws InvocationTargetException InvocationTargetException
     */
    public static Dialect getDialectInstance(Properties properties) throws RuntimeException {
        try {
            String dialectName = properties.getProperty(Constant.DIALECT);
            Class dialectClass = DialectType.getClassByName(dialectName);
            return (Dialect) dialectClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * can be paged
     * @param ms mapped statement
     * @param sql sql
     * @param parameter parameter
     * @return boolean
     */
    public boolean canBePaged(MappedStatement ms, String sql, Object parameter) {
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.SELECT) {
            if (sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
                throw new RuntimeException("for update statement not support page");
            }
            return getPageParameter(parameter) != null;
        } else {
            throw new RuntimeException("only support select statement");
        }
    }

    /**
     * get page parameter
     * @param parameter parameter
     * @return Page
     */
    public Page getPageParameter(Object parameter) {
        if (parameter instanceof Map) {
            Map map = (Map) parameter;
            if (map.containsKey(Constant.PAGE_KEY)) {
                Object pageObj = map.get(Constant.PAGE_KEY);
                if (pageObj instanceof Page) {
                    return (Page) pageObj;
                }
            }
        }
        return null;
    }

    /**
     * get count sql
     * @param ms mapped statement
     * @param boundSql bound sql
     * @param parameter parameter
     * @param additionalParameters additional parameters
     * @return BoundSql
     */
    public BoundSql getCountSql(MappedStatement ms, BoundSql boundSql, Object parameter,
                                Map<String, Object> additionalParameters) {
        String countSql = getCountSql(boundSql.getSql());
        BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql,
                boundSql.getParameterMappings(), parameter);
        for (Map.Entry<String, Object> entry : additionalParameters.entrySet()) {
            countBoundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return countBoundSql;
    }

    public abstract String getCountSql(String sql);

    /**
     * get page sql
     * @param ms mapped statement
     * @param boundSql bound sql
     * @param parameter parameter
     * @param pageKey page key
     * @param rowBounds row bounds
     * @param additionalParameters additional parameters
     * @param orders orders
     * @return BoundSql
     */
    public BoundSql getPageSql(MappedStatement ms, BoundSql boundSql, Object parameter,
                               CacheKey pageKey, RowBounds rowBounds, Map<String, Object> additionalParameters,
                               Map<String, OrderType> orders) {
        String pageSql = getPageSql(boundSql.getSql(), rowBounds, pageKey, orders);
        BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), pageSql,
                boundSql.getParameterMappings(), parameter);
        for (Map.Entry<String, Object> entry : additionalParameters.entrySet()) {
            pageBoundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return pageBoundSql;
    }

    public abstract String getPageSql(String sql, RowBounds rowBounds, CacheKey pageKey,
                                      Map<String, OrderType> orders);

    /**
     * build page list
     * @param resultList  result list
     * @param page page
     * @param totalRows total rows
     * @return PageList
     */
    public PageList buildPageList(List resultList, Page page, int totalRows) {
        if (logger.isDebugEnabled()) {
            logger.debug("resultList: {}, page.getPageNum(): {}, page.getPageSize(): {}, page.getTotalRows: {}",
                    resultList, page.getPageNum(), page.getPageSize(), totalRows);
        }
        return new PageList(resultList, page.getPageNum(), page.getPageSize(), totalRows);
    }

}
