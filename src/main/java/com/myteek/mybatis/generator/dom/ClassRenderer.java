package com.myteek.mybatis.generator.dom;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaDomUtils;
import org.mybatis.generator.api.dom.java.render.RenderingUtilities;
import org.mybatis.generator.internal.util.CustomCollectors;

import java.util.ArrayList;
import java.util.List;

public class ClassRenderer {

    /**
     * class render
     * @param innerClass inner class
     * @param compilationUnit compilation unit
     * @return List&lt;String&gt;
     */
    public List<String> render(InnerClass innerClass, CompilationUnit compilationUnit) {
        List<String> lines = new ArrayList<>();
        lines.addAll(innerClass.getJavaDocLines());
        lines.addAll(innerClass.getAnnotations());
        lines.add(renderFirstLine(innerClass, compilationUnit));
        // 添加主类后面的空行
        lines.add("");
        lines.addAll(RenderingUtilities.renderFields(innerClass.getFields(), compilationUnit));
        lines.addAll(RenderingUtilities.renderInitializationBlocks(innerClass.getInitializationBlocks()));
        lines.addAll(RenderingUtilities.renderClassOrEnumMethods(innerClass.getMethods(), compilationUnit));
        lines.addAll(RenderingUtilities.renderInnerClasses(innerClass.getInnerClasses(), compilationUnit));
        lines.addAll(RenderingUtilities.renderInnerInterfaces(innerClass.getInnerInterfaces(), compilationUnit));
        lines.addAll(RenderingUtilities.renderInnerEnums(innerClass.getInnerEnums(), compilationUnit));
        lines = RenderingUtilities.removeLastEmptyLine(lines);
        // 空行
        lines.add("");
        lines.add("}"); //$NON-NLS-1$
        // for checkstyle last blank row
        lines.add("");
        return lines;
    }

    private String renderFirstLine(InnerClass innerClass, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        sb.append(innerClass.getVisibility().getValue());
        if (innerClass.isAbstract()) {
            sb.append("abstract "); //$NON-NLS-1$
        }
        if (innerClass.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }
        if (innerClass.isFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }
        sb.append("class "); //$NON-NLS-1$
        sb.append(innerClass.getType().getShortName());
        sb.append(RenderingUtilities.renderTypeParameters(innerClass.getTypeParameters(), compilationUnit));
        sb.append(renderSuperClass(innerClass, compilationUnit));
        sb.append(renderSuperInterfaces(innerClass, compilationUnit));
        sb.append(" {"); //$NON-NLS-1$
        return sb.toString();
    }

    private String renderSuperClass(InnerClass innerClass, CompilationUnit compilationUnit) {
        return innerClass.getSuperClass()
                .map(sc -> " extends " + JavaDomUtils.calculateTypeName(compilationUnit, sc)) //$NON-NLS-1$
                .orElse(""); //$NON-NLS-1$
    }

    // should return an empty string if no super interfaces
    private String renderSuperInterfaces(InnerClass innerClass, CompilationUnit compilationUnit) {
        return innerClass.getSuperInterfaceTypes().stream()
                .map(tp -> JavaDomUtils.calculateTypeName(compilationUnit, tp))
                .collect(CustomCollectors.joining(", ", " implements ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
