package com.myteek.mybatis.generator.handler;

import com.myteek.mybatis.generator.util.Util;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonObjectHandler<T> extends BaseTypeHandler<T> {

    private Class<T> className;

    /**
     * constructor
     * @param className class name
     */
    public JsonObjectHandler(Class<T> className) {
        if (className == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.className = className;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter,
                                    JdbcType jdbcType) throws SQLException {
        ps.setString(i, Util.toJson(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Util.toObject(rs.getString(columnName), className);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Util.toObject(rs.getString(columnIndex), className);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Util.toObject(cs.getString(columnIndex), className);
    }

}
