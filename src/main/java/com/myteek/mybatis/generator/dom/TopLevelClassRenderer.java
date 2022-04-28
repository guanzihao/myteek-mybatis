package com.myteek.mybatis.generator.dom;

import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderImports;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderPackage;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderStaticImports;

public class TopLevelClassRenderer {

    private ClassRenderer classRenderer = new ClassRenderer();

    /**
     * top level class render
     * @param topLevelClass top level class
     * @return String
     */
    public String render(TopLevelClass topLevelClass) {
        List<String> lines = new ArrayList<>();
        lines.addAll(topLevelClass.getFileCommentLines());
        lines.addAll(renderPackage(topLevelClass));
        lines.addAll(renderStaticImports(topLevelClass));
        String className = topLevelClass.getType().getPackageName() + "." + topLevelClass.getType().getShortName();
        List<String> imports = renderImports(topLevelClass);
        imports.forEach(item -> {
            // 如果添加的是当前类的话，则忽略
            if (item.indexOf(className) < 0) {
                lines.add(item);
            }
        });
        lines.addAll(classRenderer.render(topLevelClass, topLevelClass));
        return lines.stream()
                .collect(Collectors.joining(System.getProperty("line.separator"))); //$NON-NLS-1$
    }

}
