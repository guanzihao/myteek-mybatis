package com.myteek.mybatis.page.dialect;

public enum DialectType {

    MYSQL("mysql", MysqlGenericDialect.class),

    POSTGRESQL("postgresql", PostgreSqlGenericDialect.class);

    private String name;

    private Class clazz;

    DialectType(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    /**
     * get class by name
     * @param name class name
     * @return Class
     */
    public static Class getClassByName(String name) {
        if (name == null || "".equals(name)) {
            return MYSQL.clazz;
        }
        for (DialectType item : DialectType.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item.getClazz();
            }
        }
        throw new RuntimeException("unsupported dialect type: " + name);
    }

    public String getName() {
        return name;
    }

    public Class getClazz() {
        return clazz;
    }

}
