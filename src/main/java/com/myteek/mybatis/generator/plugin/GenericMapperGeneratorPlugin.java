package com.myteek.mybatis.generator.plugin;

import com.myteek.mybatis.constant.Constants;
import com.myteek.mybatis.generator.util.Util;
import com.myteek.mybatis.generator.element.GenericTextElement;
import com.myteek.mybatis.generator.element.GenericXmlElement;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.VisitableElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenericMapperGeneratorPlugin extends PluginAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GenericMapperGeneratorPlugin.class);

    private String mapperTargetDir;
    private String mapperTargetPackage;
    private Boolean isSubPackagesEnabled;

    private ShellCallback shellCallback;

    private Field isMergeAble;

    /**
     * constructor
     * @throws NoSuchFieldException NoSuchFieldException
     */
    public GenericMapperGeneratorPlugin() throws NoSuchFieldException {
        shellCallback = new DefaultShellCallback(false);
        isMergeAble = GeneratedXmlFile.class.getDeclaredField("isMergeable");
        isMergeAble.setAccessible(true);
    }

    @Override
    public boolean validate(List<String> warnings) {
        String mapperTargetDir = properties.getProperty("mapperTargetDir");
        this.mapperTargetDir = mapperTargetDir;
        String mapperTargetPackage = properties.getProperty("mapperTargetPackage");
        this.mapperTargetPackage = mapperTargetPackage;
        String enableSubPackages = this.properties.getProperty("enableSubPackages");
        enableSubPackages = !StringUtility.stringHasValue(enableSubPackages) ? "false" : enableSubPackages;
        this.isSubPackagesEnabled = Boolean.valueOf(enableSubPackages);
        return StringUtility.stringHasValue(mapperTargetDir) && StringUtility.stringHasValue(mapperTargetPackage);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        ArrayList<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        String packageName =
                introspectedTable.getFullyQualifiedTable().getSubPackageForClientOrSqlMap(isSubPackagesEnabled);
        Iterator<GeneratedJavaFile> javaFileIterator = introspectedTable.getGeneratedJavaFiles().iterator();
        while (javaFileIterator.hasNext()) {
            GeneratedJavaFile javaFile = javaFileIterator.next();
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType javaType = unit.getType();
            String shortName = javaType.getShortName();
            if (shortName.equals(introspectedTable.getFullyQualifiedTable().getDomainObjectName()) &&
                    !shortName.endsWith("Example") && !shortName.endsWith("Mapper") &&
                    !shortName.endsWith("SqlProvider")) {
                String mapperName = shortName + "Mapper";
                Interface mapperInterface = new Interface(mapperTargetPackage + packageName + "." + mapperName);
                mapperInterface.setVisibility(JavaVisibility.PUBLIC);
                mapperInterface.addImportedType(javaType);
                FullyQualifiedJavaType mapper = new FullyQualifiedJavaType(Constants.MAPPER_CLASS_PATH);
                mapperInterface.addImportedType(mapper);

                mapperInterface.addJavaDocLine("/**");
                mapperInterface.addJavaDocLine(" * Mapper: " + mapperName);
                mapperInterface.addJavaDocLine(" * Model: " + shortName);
                mapperInterface.addJavaDocLine(" * Table: " +
                        introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
                mapperInterface.addJavaDocLine(" * This Mapper generated by Mybatis Generator Extend at " + Util.now());
                mapperInterface.addJavaDocLine(" */");

                if (primaryKeyColumns.size() > 0) {
                    FullyQualifiedJavaType superInterfaceType =
                            new FullyQualifiedJavaType(Constants.GENERIC_MAPPER_CLASS_PATH);
                    superInterfaceType.addTypeArgument(javaType);
                    if (primaryKeyColumns.size() == 1 &&
                            Constants.DEFAULT_PRIMARY_COLUMN_NAME
                                    .equals(primaryKeyColumns.get(0).getActualColumnName())) {
                        superInterfaceType.addTypeArgument(primaryKeyColumns.get(0).getFullyQualifiedJavaType());
                    } else {
                        superInterfaceType.addTypeArgument(
                                new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType()));
                    }
                    mapperInterface.addImportedType(superInterfaceType);
                    mapperInterface.addSuperInterface(superInterfaceType);
                } else {
                    FullyQualifiedJavaType superInterfaceType =
                            new FullyQualifiedJavaType(Constants.GENERIC_WITHOUT_PRIMARY_KEY_MAPPER_CLASS_PATH);
                    mapperInterface.addImportedType(superInterfaceType);
                    superInterfaceType.addTypeArgument(javaType);
                    mapperInterface.addSuperInterface(superInterfaceType);
                }
                mapperInterface.addAnnotation("@Mapper");

                try {
                    GeneratedJavaFile file = new GeneratedJavaFile(mapperInterface, mapperTargetDir, javaFormatter);
                    File mapperDir = shellCallback.getDirectory(mapperTargetDir, mapperTargetPackage + packageName);
                    File mapperFile = new File(mapperDir, file.getFileName());
                    if (!mapperFile.exists()) {
                        mapperJavaFiles.add(file);
                    }
                } catch (ShellException e) {
                    logger.error("Get directory error!", e);
                }
            }
        }
        return mapperJavaFiles;
    }

    /**
     * context generate additional xml files
     * @param introspectedTable introspected table
     * @return List GeneratedXmlFile
     */
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        List<GeneratedXmlFile> extXmlFiles = new ArrayList<>(1);
        List<GeneratedXmlFile> xmlFiles = introspectedTable.getGeneratedXmlFiles();
        for (GeneratedXmlFile xmlFile : xmlFiles) {
            try {
                isMergeAble.set(xmlFile, Boolean.FALSE);
            } catch (IllegalAccessException e) {
                logger.error("Reflect set field error!", e);
            }
            Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                    XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
            XmlElement root = new XmlElement("mapper");
            document.setRootElement(root);
            String namespace = mapperTargetPackage + "." +
                    introspectedTable.getTableConfiguration().getDomainObjectName() + "Mapper";
            root.addAttribute(new Attribute("namespace", namespace));
            root.addElement(new TextElement("  <!--"));
            root.addElement(new TextElement("       " +
                    "This Mapper File generated by MyBatis Generator Extend at " + Util.now()));
            root.addElement(new TextElement("  -->"));
            root.addElement(new TextElement("  "));
            String fileName = xmlFile.getFileName();
            String targetProject = xmlFile.getTargetProject();
            String targetPackage = xmlFile.getTargetPackage() + ".extend";
            try {
                File directory = shellCallback.getDirectory(targetProject, targetPackage);
                File targetFile = new File(directory, fileName);
                if (!targetFile.exists()) {
                    GeneratedXmlFile gxf = new GeneratedXmlFile(document, fileName, targetPackage,
                            targetProject, true, context.getXmlFormatter());
                    extXmlFiles.add(gxf);
                }
            } catch (ShellException e) {
                logger.error("Get directory error!", e);
            }
            extXmlFiles.add(xmlFile);
        }
        return extXmlFiles;
    }

    /**
     * 修改 mapper 的 namespace
     * @param document document
     * @param introspectedTable introspected table
     */
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement root = document.getRootElement();
        GenericXmlElement newRoot = new GenericXmlElement(root.getName());
        for (Attribute attribute : root.getAttributes()) {
            newRoot.addAttribute(attribute);
        }
        for (VisitableElement element : root.getElements()) {
            if (element instanceof XmlElement) {
                newRoot.addElement(new GenericXmlElement((XmlElement) element));
            } else if (element instanceof TextElement) {
                GenericTextElement text = new GenericTextElement(((TextElement) element).getContent());
                newRoot.addElement(text);
            } else {
                newRoot.addElement(element);
            }
        }
        String nameSpace = mapperTargetPackage + "." +
                introspectedTable.getTableConfiguration().getDomainObjectName() + "Mapper";
        document.setRootElement(newRoot);
        introspectedTable.setMyBatis3FallbackSqlMapNamespace(nameSpace);
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement xmlElement,
                                                                     IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element,
                                                         IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapCountByExampleElementGenerated(XmlElement element,
                                                        IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element,
                                                                  IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element,
                                                                     IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
                                                      IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
                                                                  IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element,
                                                                  IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
                                                                     IntrospectedTable introspectedTable) {
        return false;
    }

}
