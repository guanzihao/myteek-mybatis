package com.myteek.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.util.Iterator;
import java.util.List;

public class EqualsHashCodePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * generate to string
     * @param topLevelClass top level class
     * @param introspectedColumns introspected columns
     * @param introspectedTable introspected table
     */
    public void generateToString(TopLevelClass topLevelClass, List<IntrospectedColumn> introspectedColumns,
                                 IntrospectedTable introspectedTable) {
        Method method = new Method("toString");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * to string");
        method.addJavaDocLine(" * @return");
        method.addJavaDocLine(" */");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        method.addBodyLine("StringBuilder sb = new StringBuilder();");
        method.addBodyLine("sb.append(getClass().getSimpleName());");
        method.addBodyLine("sb.append(\" {\");");
        for (int i = 0; i < introspectedColumns.size(); i++) {
            IntrospectedColumn introspectedColumn = introspectedColumns.get(i);
            FullyQualifiedJavaType javaType = introspectedColumn.getFullyQualifiedJavaType();
            String getterMethod = JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(), javaType);
            StringBuilder sb = new StringBuilder();
            sb.append("sb.append(\"").append(i > 0 ? ", " : "").append(introspectedColumn.getJavaProperty())
                    .append("=\")").append(".append(").append(getterMethod).append("()").append(");");
            method.addBodyLine(sb.toString());
        }
        method.addBodyLine("sb.append(\"}\");");
        method.addBodyLine("return sb.toString();");
        topLevelClass.addMethod(method);
    }

    /**
     * generate hash code
     * @param topLevelClass top level class
     * @param introspectedColumns introspected columns
     * @param introspectedTable introspected table
     */
    public void generateHashCode(TopLevelClass topLevelClass, List<IntrospectedColumn> introspectedColumns,
                                 IntrospectedTable introspectedTable) {
        Method method = new Method("hashCode");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * hash code");
        method.addJavaDocLine(" * @return");
        method.addJavaDocLine(" */");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        method.addBodyLine("final int prime = 256;");
        method.addBodyLine("int result = 1;");
        StringBuilder sb = new StringBuilder();
        boolean hasTemp = false;
        Iterator<IntrospectedColumn> iterator = introspectedColumns.iterator();
        while (iterator.hasNext()) {
            IntrospectedColumn introspectedColumn = iterator.next();
            FullyQualifiedJavaType javaType = introspectedColumn.getFullyQualifiedJavaType();
            String getterMethod = JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(), javaType);
            sb.setLength(0);
            if (javaType.isPrimitive()) {
                if ("boolean".equals(javaType.getFullyQualifiedName())) {
                    sb.append("result = prime * result + (").append(getterMethod).append("() ? 1231 : 1237);");
                    method.addBodyLine(sb.toString());
                } else if ("byte".equals(javaType.getFullyQualifiedName())) {
                    sb.append("result = prime * result + ").append(getterMethod).append("();");
                    method.addBodyLine(sb.toString());
                } else if ("char".equals(javaType.getFullyQualifiedName())) {
                    sb.append("result = prime * result + ").append(getterMethod).append("();");
                    method.addBodyLine(sb.toString());
                } else if ("double".equals(javaType.getFullyQualifiedName())) {
                    if (!hasTemp) {
                        method.addBodyLine("long temp;");
                        hasTemp = true;
                    }
                    sb.append("temp = Double.doubleToLongBits(").append(getterMethod).append("());");
                    method.addBodyLine(sb.toString());
                } else if ("float".equals(javaType.getFullyQualifiedName())) {
                    sb.append("result = prime * result + Float.floatToIntBits(").append(getterMethod).append("());");
                    method.addBodyLine(sb.toString());
                } else if ("int".equals(javaType.getFullyQualifiedName())) {
                    sb.append("result = prime * result + ").append(getterMethod).append("();");
                    method.addBodyLine(sb.toString());
                } else if ("long".equals(javaType.getFullyQualifiedName())) {
                    sb.append("result = prime * result + (int) (").append(getterMethod)
                            .append("() ^ (").append(getterMethod)
                            .append("() >>> 32));");
                    method.addBodyLine(sb.toString());
                } else if ("short".equals(javaType.getFullyQualifiedName())) {
                    sb.append("result = prime * result + ").append(getterMethod).append("();");
                    method.addBodyLine(sb.toString());
                }
            } else {
                sb.append("result = prime * result + ((").append(getterMethod).append("() == null) ? 0 : ")
                        .append(getterMethod).append("().hashCode());");
                method.addBodyLine(sb.toString());
            }
        }
        method.addBodyLine("return result;");
        topLevelClass.addMethod(method);
    }

    /**
     * generate equal statement
     * @param topLevelClass top level class
     * @param introspectedColumns introspected columns
     * @param introspectedTable introspected tables
     */
    public void generateEquals(TopLevelClass topLevelClass, List<IntrospectedColumn> introspectedColumns,
                               IntrospectedTable introspectedTable) {
        Method method = new Method("equals");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * equals");
        method.addJavaDocLine(" * @param that that");
        method.addJavaDocLine(" * @return");
        method.addJavaDocLine(" */");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "that"));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        method.addBodyLine("if (this == that) {");
        method.addBodyLine("return true;");
        method.addBodyLine("}");
        method.addBodyLine("if (that == null) {");
        method.addBodyLine("return false;");
        method.addBodyLine("}");
        method.addBodyLine("if (getClass() != that.getClass()) {");
        method.addBodyLine("return false;");
        method.addBodyLine("}");
        StringBuilder sb = new StringBuilder();
        sb.append(topLevelClass.getType().getShortName())
                .append(" other = (")
                .append(topLevelClass.getType().getShortName())
                .append(") that;");
        method.addBodyLine(sb.toString());
        boolean first = true;
        for (Iterator<IntrospectedColumn> iter = introspectedColumns.iterator();
             iter.hasNext(); method.addBodyLine(sb.toString())) {
            IntrospectedColumn introspectedColumn = iter.next();
            sb.setLength(0);
            if (first) {
                sb.append("return (");
                first = false;
            } else {
                OutputUtilities.javaIndent(sb, 1);
                sb.append("&& (");
            }
            String getterMethod = JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(),
                    introspectedColumn.getFullyQualifiedJavaType());
            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                sb.append(getterMethod).append("() == other.").append(getterMethod).append("())");
            } else {
                sb.append(getterMethod).append("() == null ? other.").append(getterMethod)
                        .append("() == null : ").append(getterMethod).append("().equals(other.")
                        .append(getterMethod).append("()))");
            }
            if (!iter.hasNext()) {
                sb.append(";");
            }
        }
        topLevelClass.addMethod(method);
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
                                                      IntrospectedTable introspectedTable) {
        generateEquals(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
        generateHashCode(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
        generateToString(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateEquals(topLevelClass, introspectedTable.getPrimaryKeyColumns(), introspectedTable);
        generateHashCode(topLevelClass, introspectedTable.getPrimaryKeyColumns(), introspectedTable);
        generateToString(topLevelClass, introspectedTable.getPrimaryKeyColumns(), introspectedTable);
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List columns;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            columns = introspectedTable.getNonBLOBColumns();
        } else {
            columns = introspectedTable.getPrimaryKeyColumns();
        }
        if (!columns.isEmpty()) {
            generateEquals(topLevelClass, columns, introspectedTable);
            generateHashCode(topLevelClass, columns, introspectedTable);
            generateToString(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
        }
        return true;
    }

}
