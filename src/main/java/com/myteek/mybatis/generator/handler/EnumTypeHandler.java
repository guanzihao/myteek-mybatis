package com.myteek.mybatis.generator.handler;

import com.myteek.mybatis.generator.base.ConfigEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@MappedJdbcTypes(value = JdbcType.TINYINT, includeNullJdbcType = true)
public class EnumTypeHandler extends BaseTypeHandler<ConfigEnum> {

    private Class<ConfigEnum> type;

    /**
     * enum type convert
     * @param type type
     */
    public EnumTypeHandler(Class<ConfigEnum> type) {
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("Type argument can not be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ConfigEnum parameter,
                                    JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public ConfigEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return valueOf(rs.getInt(columnName));
    }

    @Override
    public ConfigEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return valueOf(rs.getInt(columnIndex));
    }

    @Override
    public ConfigEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return valueOf(cs.getInt(columnIndex));
    }

    private ConfigEnum valueOf(int value) {
        for (ConfigEnum item : type.getEnumConstants()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

}
