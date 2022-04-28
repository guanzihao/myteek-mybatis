package com.myteek.mybatis.generator.dom;

import org.mybatis.generator.api.dom.java.Interface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderImports;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderPackage;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderStaticImports;

public class TopLevelInterfaceRenderer {

    private InterfaceRenderer interfaceRenderer = new InterfaceRenderer();

    /**
     * top level interface render
     * @param topLevelInterface top level interface
     * @return String
     */
    public String render(Interface topLevelInterface) {
        List<String> lines = new ArrayList<>();
        lines.addAll(topLevelInterface.getFileCommentLines());
        lines.addAll(renderPackage(topLevelInterface));
        lines.addAll(renderStaticImports(topLevelInterface));
        lines.addAll(renderImports(topLevelInterface));
        lines.addAll(interfaceRenderer.render(topLevelInterface, topLevelInterface));
        return lines.stream()
                .collect(Collectors.joining(System.getProperty("line.separator"))); //$NON-NLS-1$
    }

}
