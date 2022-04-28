package com.myteek.mybatis.generator.plugin;

import com.myteek.mybatis.generator.generator.SqlMapExtendMethodGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class SqlMapExtendMethodGeneratorPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        SqlMapExtendMethodGenerator generator = new SqlMapExtendMethodGenerator(parentElement, introspectedTable);

        generator.addModelWhereCase();
        generator.addModelWhereCaseLike();
        generator.addSelect();
        generator.addSelectLike();
        generator.addSelectOne();
        generator.addSelectPageList();
        generator.addSelectPageListLike();
        generator.addSelectCount();
        generator.addSelectCountLike();
        generator.addSelectAll();
        generator.addSelectByIds();
        generator.addBatchInsert();
        generator.addDeleteByIds();
        generator.addInsertOrUpdateSelective();

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

}
