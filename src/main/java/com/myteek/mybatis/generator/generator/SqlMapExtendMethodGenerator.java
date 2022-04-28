package com.myteek.mybatis.generator.generator;

import com.myteek.mybatis.generator.element.GenericTextElement;
import com.myteek.mybatis.generator.element.GenericXmlElement;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.List;

public class SqlMapExtendMethodGenerator {

    public static final String WHERE_CLAUSE_ID = "model_where_clause";

    public static final String WHERE_CLAUSE_LIKE_ID = "model_where_clause_like";

    private final XmlElement parentElement;
    private final IntrospectedTable introspectedTable;

    public SqlMapExtendMethodGenerator(XmlElement parentElement, IntrospectedTable introspectedTable) {
        this.parentElement = parentElement;
        this.introspectedTable = introspectedTable;
    }

    /**
     * select element
     * @param selectId select id
     * @return XmlElement
     */
    public XmlElement buildSelectElement(String selectId) {
        XmlElement element = new XmlElement("select");
        element.addAttribute(new Attribute("id", selectId));

        boolean hasBlobColumns = introspectedTable.getBLOBColumns().size() > 0;
        element.addAttribute(new Attribute("resultMap",
                hasBlobColumns ? introspectedTable.getResultMapWithBLOBsId() :
                        introspectedTable.getBaseResultMapId()));
        element.addElement(new GenericTextElement("select"));

        String column = "<include refid=\"" + introspectedTable.getBaseColumnListId() + "\" />";
        if (hasBlobColumns) {
            column += ", <include refid=\"" + introspectedTable.getBlobColumnListId() + "\" />";
        }

        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
        element.addElement(new GenericTextElement(column));
        element.addElement(new GenericTextElement("from " + tableName));
        return element;
    }

    private void buildWhereCase(XmlElement element) {
        element.addElement(new GenericTextElement("<include refid=\"" + WHERE_CLAUSE_ID + "\" />"));
        parentElement.addElement(element);
        parentElement.addElement(new TextElement(""));
    }

    private void buildWhereCaseLike(XmlElement element) {
        element.addElement(new GenericTextElement("<include refid=\"" + WHERE_CLAUSE_LIKE_ID + "\" />"));
        parentElement.addElement(element);
        parentElement.addElement(new TextElement(""));
    }

    /**
     * add model where case
     */
    public void addModelWhereCase() {
        GenericXmlElement whereCase = new GenericXmlElement("sql");
        whereCase.addAttribute(new Attribute("id", WHERE_CLAUSE_ID));
        GenericXmlElement whereElement = new GenericXmlElement("where");
        whereCase.addElement(whereElement);

        GenericXmlElement modelIf = new GenericXmlElement("if");
        modelIf.addAttribute(new Attribute("test", "model != null"));
        whereElement.addElement(modelIf);

        String alias = introspectedTable.getTableConfiguration().getAlias();
        List<IntrospectedColumn> columnList = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columnList) {
            GenericXmlElement ifElement = new GenericXmlElement("if");
            String test = "model." + column.getJavaProperty() + " != null";
            ifElement.addAttribute(new Attribute("test", test));
            String cases = "and " + (alias != null && alias.length() > 0 ? alias + "." : "") +
                    column.getActualColumnName() + " = #{model." + column.getJavaProperty() + "}";
            GenericTextElement caseElement = new GenericTextElement(cases);
            ifElement.addElement(caseElement);
            modelIf.addElement(ifElement);
        }
        parentElement.addElement(whereCase);
        parentElement.addElement(new TextElement(""));
    }

    /**
     * add model where case like
     */
    public void addModelWhereCaseLike() {
        GenericXmlElement whereCase = new GenericXmlElement("sql");
        whereCase.addAttribute(new Attribute("id", WHERE_CLAUSE_LIKE_ID));
        GenericXmlElement whereElement = new GenericXmlElement("where");
        whereCase.addElement(whereElement);

        GenericXmlElement modelIf = new GenericXmlElement("if");
        modelIf.addAttribute(new Attribute("test", "model != null"));
        whereElement.addElement(modelIf);

        String alias = introspectedTable.getTableConfiguration().getAlias();
        List<IntrospectedColumn> columnList = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columnList) {
            GenericXmlElement ifElement = new GenericXmlElement("if");
            String test = "model." + column.getJavaProperty() + " != null";
            ifElement.addAttribute(new Attribute("test", test));
            String cases;
            if (column.isStringColumn()) {
                cases = "and " + (alias != null && alias.length() > 0 ? alias + "." : "") +
                        column.getActualColumnName() + " like #{model." + column.getJavaProperty() + "}";
            } else {
                cases = "and " + (alias != null && alias.length() > 0 ? alias + "." : "") +
                        column.getActualColumnName() + " = #{model." + column.getJavaProperty() + "}";
            }
            GenericTextElement caseElement = new GenericTextElement(cases);
            ifElement.addElement(caseElement);
            modelIf.addElement(ifElement);
        }
        parentElement.addElement(whereCase);
        parentElement.addElement(new TextElement(""));
    }

    /**
     * selectAll element
     */
    public void addSelectAll() {
        XmlElement element = buildSelectElement("selectAll");
        parentElement.addElement(element);
        parentElement.addElement(new TextElement(""));
    }

    /**
     * select
     */
    public void addSelect() {
        XmlElement element = buildSelectElement("select");
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        element.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        buildWhereCase(element);
        GenericXmlElement modelIf = new GenericXmlElement("if");
        modelIf.addAttribute(new Attribute("test", "model == null"));
        modelIf.addElement(new GenericTextElement("where 0 = 1"));
        element.addElement(modelIf);
    }

    /**
     * select like
     */
    public void addSelectLike() {
        XmlElement element = buildSelectElement("selectLike");
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        element.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        buildWhereCaseLike(element);
        GenericXmlElement modelIf = new GenericXmlElement("if");
        modelIf.addAttribute(new Attribute("test", "model == null"));
        modelIf.addElement(new GenericTextElement("where 0 = 1"));
        element.addElement(modelIf);
    }

    /**
     * select one
     */
    public void addSelectOne() {
        XmlElement element = buildSelectElement("selectOne");
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        element.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        buildWhereCase(element);
        element.addElement(new GenericTextElement("limit 1"));
    }

    /**
     * select page list
     */
    public void addSelectPageList() {
        XmlElement element = buildSelectElement("selectPageList");
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        element.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        buildWhereCase(element);
    }

    /**
     * select page list like
     */
    public void addSelectPageListLike() {
        XmlElement element = buildSelectElement("selectPageListLike");
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        element.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        buildWhereCaseLike(element);
    }

    /**
     * select count
     */
    public void addSelectCount() {
        XmlElement element = new XmlElement("select");
        element.addAttribute(new Attribute("id", "selectCount"));
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        element.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        element.addAttribute(new Attribute("resultType", "java.lang.Integer"));
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        element.addElement(new GenericTextElement("select count(1) from " + tableName));
        buildWhereCase(element);
    }

    /**
     * select count like
     */
    public void addSelectCountLike() {
        XmlElement element = new XmlElement("select");
        element.addAttribute(new Attribute("id", "selectCountLike"));
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        element.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        element.addAttribute(new Attribute("resultType", "java.lang.Integer"));
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        element.addElement(new GenericTextElement("select count(1) from " + tableName));
        buildWhereCaseLike(element);
    }

    /**
     * add batch insert
     */
    public void addBatchInsert() {
        XmlElement element = new XmlElement("insert");
        element.addAttribute(new Attribute("id", "batchInsert"));
        element.addAttribute(new Attribute("parameterType", "java.util.List"));
        List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
        if (pkColumns.size() == 1) {
            element.addAttribute(new Attribute("useGeneratedKeys", "true"));
            element.addAttribute(new Attribute("keyProperty", pkColumns.get(0).getJavaProperty()));
        }
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        element.addElement(new GenericTextElement("INSERT INTO " + tableName + " ("));
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        int len = 3;
        for (int i = 0; i < columns.size(); i += len) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < i + len; j++) {
                if (j < columns.size()) {
                    IntrospectedColumn column = columns.get(j);
                    sb.append(column.getActualColumnName()).append(j < columns.size() - 1 ? ", " : "");
                }
            }
            element.addElement(new GenericTextElement("     " + sb));
        }
        element.addElement(new GenericTextElement(") VALUES"));
        element.addElement(new GenericTextElement(
                "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">"));
        element.addElement(new GenericTextElement("("));
        for (int i = 0; i < columns.size(); i += len) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < i + len; j++) {
                if (j < columns.size()) {
                    IntrospectedColumn column = columns.get(j);
                    sb.append("#{item.").append(column.getJavaProperty())
                            .append("}").append(j < columns.size() - 1 ? ", " : "");
                }
            }
            element.addElement(new GenericTextElement("      " + sb));
        }
        element.addElement(new GenericTextElement(")"));
        element.addElement(new GenericTextElement("</foreach>"));
        parentElement.addElement(element);
        parentElement.addElement(new TextElement(""));
    }

    /**
     * add select by id list
     */
    public void addSelectByIds() {
        XmlElement element = buildSelectElement("selectByIds");
        element.addAttribute(new Attribute("parameterType", "java.util.List"));
        int pkSize = introspectedTable.getPrimaryKeyColumns().size();
        element.addElement(new GenericTextElement("where"));
        if (pkSize >= 2) {
            element.addElement(new GenericTextElement(" 0 = 1"));
        } else if (pkSize == 1) {
            element.addElement(new GenericTextElement("<choose>"));
            element.addElement(new GenericTextElement("<when test=\"list != null and list.size() > 0\">"));
            element.addElement(new GenericTextElement(
                    introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty() + " IN"));
            element.addElement(new GenericTextElement(
                    "<foreach collection=\"list\" index=\"index\" " +
                            "item=\"item\" open=\"(\" close=\")\" separator=\",\">"));
            element.addElement(new GenericTextElement("     #{item}"));
            element.addElement(new GenericTextElement("</foreach>"));
            element.addElement(new GenericTextElement("</when>"));
            element.addElement(new GenericTextElement("<otherwise>"));
            element.addElement(new GenericTextElement(" 0 = 1"));
            element.addElement(new GenericTextElement("</otherwise>"));
            element.addElement(new GenericTextElement("</choose>"));
        }
        if (pkSize > 0) {
            parentElement.addElement(element);
            parentElement.addElement(new TextElement(""));
        }
    }

    /**
     * add delete by id
     */
    public void addDeleteByIds() {
        XmlElement element = new XmlElement("delete");
        element.addAttribute(new Attribute("id", "deleteByIds"));
        element.addAttribute(new Attribute("parameterType", "java.util.List"));
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        int pkSize = introspectedTable.getPrimaryKeyColumns().size();
        element.addElement(new GenericTextElement("delete from " + tableName));
        element.addElement(new GenericTextElement("where"));
        if (pkSize > 1) {
            element.addElement(new GenericTextElement("0 = 1"));
        } else if (pkSize == 1) {
            element.addElement(new GenericTextElement("<choose>"));
            element.addElement(new GenericTextElement("<when test=\"list != null and list.size() > 0\">"));
            element.addElement(new GenericTextElement(
                    introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty() + " IN"));
            element.addElement(new GenericTextElement(
                    "<foreach collection=\"list\" index=\"index\" " +
                            "item=\"item\" open=\"(\" close=\")\" separator=\",\">"));
            element.addElement(new GenericTextElement("     #{item}"));
            element.addElement(new GenericTextElement("</foreach>"));
            element.addElement(new GenericTextElement("</when>"));
            element.addElement(new GenericTextElement("<otherwise>"));
            element.addElement(new GenericTextElement(" 0 = 1"));
            element.addElement(new GenericTextElement("</otherwise>"));
            element.addElement(new GenericTextElement("</choose>"));
        }
        if (pkSize > 0) {
            parentElement.addElement(element);
            parentElement.addElement(new TextElement(""));
        }
    }

    /**
     * insert or update selective
     * insert into table (a, b, c) value (a, b, c) on duplicate key
     * update a = value(a), b = value(b), c = value(c)
     * return 1 insert new row
     * return 2 row is updated
     */
    public void addInsertOrUpdateSelective() {
        XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", "insertOrUpdateSelective"));
        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));

        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(introspectedTable.getFullyQualifiedTableNameAtRuntime());

        answer.addElement(new GenericTextElement(sb.toString()));

        // trim node
        XmlElement insertTrimElement = new XmlElement("trim");  // $NON-NLS-1$
        insertTrimElement.addAttribute(new Attribute("prefix", "("));
        insertTrimElement.addAttribute(new Attribute("suffix", ")"));
        insertTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        answer.addElement(insertTrimElement);

        XmlElement valuesTrimElement = new XmlElement("trim");
        valuesTrimElement.addAttribute(new Attribute("prefix", "values ("));
        valuesTrimElement.addAttribute(new Attribute("suffix", ")"));
        valuesTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        answer.addElement(valuesTrimElement);

        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            if (introspectedColumn.isSequenceColumn() || introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                // if it is a sequences columns, it is not optional, this is required for Mybatis3
                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
                sb.append(",");
                insertTrimElement.addElement(new TextElement(sb.toString()));

                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                sb.append(",");
                valuesTrimElement.addElement(new TextElement(sb.toString()));

                continue;
            }

            // <if test="A != null"> A, </if> ==> <if test="A != null"> </if>
            XmlElement insertNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            insertNotNullElement.addAttribute(new Attribute("test", sb.toString()));

            // <if test="A != null"> port, </if> ==> A
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(",");
            insertNotNullElement.addElement(new GenericTextElement(sb.toString()));
            insertTrimElement.addElement(insertNotNullElement);

            // <if test="A != null" >#{A, jdbcType=VARCHAR},</if> ==> <if test="A != null"> </if>
            XmlElement valuesNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            valuesNotNullElement.addAttribute(new Attribute("test", sb.toString()));

            // <if test="A != null">#{A, jdbcType=VARCHAR},</if> ==> #{A, jdbcType=VARCHAR},
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append(",");
            valuesNotNullElement.addElement(new GenericTextElement(sb.toString()));
            valuesTrimElement.addElement(valuesNotNullElement);
        }

        sb.setLength(0);
        sb.append("on duplicate key update");
        answer.addElement(new GenericTextElement(sb.toString()));

        XmlElement updateTrimElement = new XmlElement("trim");
        updateTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        answer.addElement(updateTrimElement);

        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
            // <if test="A != null"> A = values(A), </if> ==> <if test="A != null"> A = values(A), </if>
            XmlElement valuesNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            valuesNotNullElement.addAttribute(new Attribute("test", sb.toString()));

            sb.setLength(0);
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            sb.append(String.format("%s = values(%s), ", columnName, columnName));
            valuesNotNullElement.addElement(new GenericTextElement(sb.toString()));

            updateTrimElement.addElement(valuesNotNullElement);
        }
        parentElement.addElement(answer);
        parentElement.addElement(new GenericTextElement(""));
    }

}
