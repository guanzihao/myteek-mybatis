package com.myteek.mybatis.generator.dom;

import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class JavaFormatter extends DefaultJavaFormatter {

    @Override
    public String visit(TopLevelClass topLevelClass) {
        return new TopLevelClassRenderer().render(topLevelClass);
    }

    @Override
    public String visit(Interface topLevelInterface) {
        return new TopLevelInterfaceRenderer().render(topLevelInterface);
    }

}
