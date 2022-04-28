package com.myteek.mybatis.page;

import com.myteek.mybatis.page.dialect.Dialect;
import com.myteek.mybatis.page.dialect.GenericDialect;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PageInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

    private Dialect dialect;
    private Field parametersField;
    private ICache<CacheKey, MappedStatement> msCache;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        initDialect();
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        BoundSql boundSql = ms.getBoundSql(parameter);
        if (dialect.canBePaged(ms, boundSql.getSql(), parameter)) {
            Map<String, Object> additionalParameters = (Map<String, Object>) parametersField.get(boundSql);
            int totalRows = queryCount(executor, ms, parameter, resultHandler, boundSql, additionalParameters);
            if (totalRows > 0) {
                return queryPage(executor, ms, parameter, resultHandler, boundSql, additionalParameters, totalRows);
            } else {
                Page page = dialect.getPageParameter(parameter);
                page.setPageNum(1);
                return dialect.buildPageList(new ArrayList(), new Page(), totalRows);
            }
        }
        return invocation.proceed();
    }

    private PageList queryPage(Executor executor, MappedStatement ms, Object parameter, ResultHandler resultHandler,
                               BoundSql boundSql, Map<String, Object> additionalParameters,
                               int totalRows) throws SQLException {
        Page page = dialect.getPageParameter(parameter);
        RowBounds rowBounds = page.toRowBounds();
        Map<String, OrderType> orders = page.getOrders();
        CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        BoundSql pageBoundSql = dialect.getPageSql(ms, boundSql, parameter, cacheKey, rowBounds,
                additionalParameters, orders);
        List resultList = executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, pageBoundSql);
        return dialect.buildPageList(resultList, page, totalRows);
    }

    private int queryCount(Executor executor, MappedStatement ms, Object parameter, ResultHandler resultHandler,
                           BoundSql boundSql, Map<String, Object> additionalParameters) throws SQLException {
        CacheKey cacheKey = executor.createCacheKey(ms, parameter, RowBounds.DEFAULT, boundSql);
        cacheKey.update("_count");
        MappedStatement countMs = msCache.get(cacheKey);
        if (countMs == null) {
            countMs = Util.newCountMappedStatement(ms);
            msCache.put(cacheKey, countMs);
        }
        BoundSql countSql = dialect.getCountSql(ms, boundSql, parameter, additionalParameters);
        List countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, countSql);
        if (countResultList.isEmpty()) {
            countResultList.add(0);
        } else if (countResultList.size() > 1) {
            int temp = countResultList.size();
            countResultList.clear();
            countResultList.add(temp);
        }
        Integer count = (Integer) countResultList.get(0);
        return count.intValue();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        try {
            dialect = GenericDialect.getDialectInstance(properties);
            parametersField = BoundSql.class.getDeclaredField("additionalParameters");
            parametersField.setAccessible(true);
            msCache = new SimpleCache<>(properties, "ms");
        } catch (Exception e) {
            logger.error("set page interceptor properties error!", e);
        }
    }

    private void initDialect() {
        if (dialect == null) {
            setProperties(new Properties());
        }
    }

}
