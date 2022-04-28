package com.myteek.mybatis.generator.handler;

import com.myteek.mybatis.generator.util.Util;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JsonArrayHandler<T> extends BaseTypeHandler<List<T>> {

    private Class<T> className;

    /**
     * constructor
     * @param className class name
     */
    public JsonArrayHandler(Class<T> className) {
        if (className == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.className = className;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List parameter,
                                    JdbcType jdbcType) throws SQLException {
        ps.setString(i, Util.toJson(parameter));
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Util.toArray(rs.getString(columnName), className);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Util.toArray(rs.getString(columnIndex), className);
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Util.toArray(cs.getString(columnIndex), className);
    }

}
