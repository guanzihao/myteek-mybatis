package com.myteek.mybatis.generator.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class DateTypeHandler extends BaseTypeHandler<String> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter,
                                    JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            return;
        }
        ps.setDate(i, Date.valueOf(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Date date = rs.getDate(columnName);
        return date == null ? null : dateFormat.format(date);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Date date = rs.getDate(columnIndex);
        return date == null ? null : dateFormat.format(date);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Date date = cs.getDate(columnIndex);
        return date == null ? null : dateFormat.format(date);
    }

}
