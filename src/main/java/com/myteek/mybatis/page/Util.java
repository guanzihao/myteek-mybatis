package com.myteek.mybatis.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Util {

    private static final Pattern pattern = Pattern.compile("\\s+FROM\\s+", Pattern.CASE_INSENSITIVE);

    /**
     * new count mapper statement
     * @param ms mappedStatement
     * @return String
     */
    public static MappedStatement newCountMappedStatement(MappedStatement ms) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
                ms.getId() + "_count", ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(String.join(",", ms.getKeyProperties()));
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        List<ResultMap> resultMapList = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(
                ms.getConfiguration(), ms.getId(), Integer.class, new ArrayList<>()).build();
        resultMapList.add(resultMap);
        builder.resultMaps(resultMapList);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * parse sql
     * @param sql sql
     * @return String
     */
    public static String parseSql(String sql) {
        sql = sql.replace("[\r\n]", " ").replace("\\s{2,}", " ");
        String tempSql = sql.toLowerCase();
        if (tempSql.indexOf("select distinct") == -1) {
            int index = getFirstFromIndex(sql);
            int orderIndex = tempSql.lastIndexOf("order by");
            orderIndex = orderIndex != -1 ? orderIndex : sql.length();
            if (log.isDebugEnabled()) {
                log.debug("sql: {}", sql);
            }
            String selectBody = sql.substring(index, orderIndex);
            return "select count(1) " + selectBody;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("sql: {}", sql);
            }
            return "select count(1) from (" + sql + ") tmp_count";
        }
    }

    private static int getFirstFromIndex(String sql) {
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            return matcher.start(0);
        }
        return 0;
    }

}
