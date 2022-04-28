package com.myteek.mybatis.generator.plugin;

import com.myteek.mybatis.constant.Constants;
import com.myteek.mybatis.generator.util.Util;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenericServiceGeneratorPlugin extends PluginAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GenericServiceGeneratorPlugin.class);

    private String serviceTargetDir;
    private String serviceTargetPackage;
    private Boolean isSubPackagesEnabled;

    private ShellCallback shellCallback;

    public GenericServiceGeneratorPlugin() {
        this.shellCallback = new DefaultShellCallback(false);
    }

    @Override
    public boolean validate(List<String> list) {
        String serviceTargetDir = this.properties.getProperty("serviceTargetDir");
        this.serviceTargetDir = serviceTargetDir;
        String serviceTargetPackage = this.properties.getProperty("serviceTargetPackage");
        this.serviceTargetPackage = serviceTargetPackage;
        String enableSubPackages = this.properties.getProperty("enableSubPackages");
        enableSubPackages = !StringUtility.stringHasValue(enableSubPackages) ? "false" : enableSubPackages;
        this.isSubPackagesEnabled = Boolean.valueOf(enableSubPackages);
        return StringUtility.stringHasValue(serviceTargetDir) && StringUtility.stringHasValue(serviceTargetPackage);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        ArrayList<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();
        JavaFormatter javaFormatter = this.context.getJavaFormatter();

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();

        String packageName = introspectedTable.getFullyQualifiedTable().getSubPackageForModel(isSubPackagesEnabled);

        Iterator<GeneratedJavaFile> javaFilesIterator = introspectedTable.getGeneratedJavaFiles().iterator();
        while (javaFilesIterator.hasNext()) {
            GeneratedJavaFile javaFile = javaFilesIterator.next();
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType modelJavaType = unit.getType();
            String shortName = modelJavaType.getShortName();
            if (shortName.equals(introspectedTable.getFullyQualifiedTable().getDomainObjectName()) &&
                    !shortName.endsWith("Example") && !shortName.endsWith("Mapper") &&
                    !shortName.endsWith("SqlProvider")
            ) {
                String serviceName = shortName + "Service";
                Interface serviceInterface = new Interface(this.serviceTargetPackage + packageName + "." + serviceName);
                serviceInterface.setVisibility(JavaVisibility.PUBLIC);

                serviceInterface.addJavaDocLine("/**");
                serviceInterface.addJavaDocLine(" * Service: " + serviceName);
                serviceInterface.addJavaDocLine(" * Mapper : " + shortName + "Mapper");
                serviceInterface.addJavaDocLine(" * Model  : " + shortName);
                serviceInterface.addJavaDocLine(" * This Service generated by MyBatis Generator Extend at " +
                        Util.now());
                serviceInterface.addJavaDocLine(" */");

                serviceInterface.addImportedType(modelJavaType);

                if (primaryKeyColumns.size() > 0) {
                    FullyQualifiedJavaType pkType = primaryKeyColumns.size() > 1 ?
                            ((InnerClass) unit).getSuperClass().get() :
                            primaryKeyColumns.get(0).getFullyQualifiedJavaType();
                    FullyQualifiedJavaType superInterfaceType = new FullyQualifiedJavaType(
                            Constants.GENERIC_SERVICE_CLASS_PATH
                    );
                    superInterfaceType.addTypeArgument(modelJavaType);
                    if (primaryKeyColumns.size() == 1) {
                        superInterfaceType.addTypeArgument(pkType);

                        // add checkExistsAndGet, only support pk index 1
                        Method checkExistsAndGet = new Method("checkExistsAndGet");
                        checkExistsAndGet.setReturnType(modelJavaType);
                        checkExistsAndGet.addParameter(new Parameter(pkType, "id"));
                        checkExistsAndGet.setAbstract(true);
                        serviceInterface.addMethod(checkExistsAndGet);

                    } else {
                        superInterfaceType.addTypeArgument(
                                new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType()));
                    }
                    serviceInterface.addImportedType(superInterfaceType);
                    serviceInterface.addSuperInterface(superInterfaceType);

                } else {
                    FullyQualifiedJavaType superInterfaceType = new FullyQualifiedJavaType(
                            Constants.GENERIC_WITHOUT_PRIMARY_KEY_SERVICE_CLASS_PATH
                    );
                    serviceInterface.addImportedType(superInterfaceType);
                    superInterfaceType.addTypeArgument(modelJavaType);
                    serviceInterface.addSuperInterface(superInterfaceType);
                }

                try {
                    GeneratedJavaFile file = new GeneratedJavaFile(serviceInterface,
                            this.serviceTargetDir, javaFormatter
                    );
                    File mapperDir = this.shellCallback.getDirectory(this.serviceTargetDir,
                            this.serviceTargetPackage + packageName
                    );
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

}