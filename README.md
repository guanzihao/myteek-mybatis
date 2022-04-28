# myteek mybatis generator extends
Another extends for mybatis generator

# useful
```xml
<dependency>
    <groupId>com.myteek</groupId>
    <artifactId>myteek-mybatis</artifactId>
    <version>2.0-SNAPSHOT</version>
</dependency>
```

## set maven `settings.xml`
use this [settings](./settings.xml) for mvn command
```shell
mvn clean package -DskipTests install -s ./settings.xml
```

## config file
`generatorConfig.properties`
```text
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test?characterEncoding=utf8
jdbc.username=test
jdbc.password=******

modelTargetDir=src/main/java
modelTargetPackage=com.myteek.test.model

mapperTargetDir=src/main/java
mapperTargetPackage=com.myteek.test.mapper

xmlTargetDir=src/main/resources
xmlTargetPackage=com/myteek/test/mapper

serviceTargetDir=src/main/java
serviceTargetPackage=com.myteek.test.service
```

## mybatis generator config

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE generatorConfiguration PUBLIC
		"-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
		"http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
<generatorConfiguration>

	<properties resource="generatorConfig.properties"/>

	<context id="context" targetRuntime="MyBatis3">
		<property name="javaFileEncoding" value="UTF-8"/>
		<property name="javaFormatter" value="com.myteek.mybatis.generator.dom.JavaFormatter"/>

		<!-- 当表名或者字段名为SQL关键字的时候，自动给表名或字段名添加分隔符 -->
		<property name="autoDelimitKeywords" value="true"/>
		<property name="beginningDelimiter" value="`"/>
		<property name="endingDelimiter" value="`"/>

		<plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>
		<plugin type="com.myteek.mybatis.generator.plugin.EqualsHashCodePlugin"/>

		<plugin type="com.myteek.mybatis.generator.plugin.GenericModelGeneratorPlugin">
			<property name="modelTargetPackage" value="${modelTargetPackage}"/>
		</plugin>

		<plugin type="com.myteek.mybatis.generator.plugin.GenericMapperGeneratorPlugin">
			<property name="mapperTargetDir" value="${mapperTargetDir}"/>
			<property name="mapperTargetPackage" value="${mapperTargetPackage}"/>
		</plugin>

		<plugin type="com.myteek.mybatis.generator.plugin.GenericServiceGeneratorPlugin">
			<property name="serviceTargetDir" value="${serviceTargetDir}"/>
			<property name="serviceTargetPackage" value="${serviceTargetPackage}"/>
		</plugin>

		<plugin type="com.myteek.mybatis.generator.plugin.GenericServiceImplGeneratorPlugin">
			<property name="serviceTargetDir" value="${serviceTargetDir}"/>
			<property name="serviceTargetPackage" value="${serviceTargetPackage}"/>
			<property name="mapperTargetPackage" value="${mapperTargetPackage}"/>
			<!-- add spring annotation -->
			<property name="statement" value="true"/>
		</plugin>

		<plugin type="com.myteek.mybatis.generator.plugin.SqlMapExtendMethodGeneratorPlugin"/>

		<commentGenerator type="com.myteek.mybatis.generator.generator.ExtendCommentGenerator"/>

		<jdbcConnection driverClass="${jdbc.driverClassName}" connectionURL="${jdbc.url}"
						userId="${jdbc.username}" password="${jdbc.password}"/>

		<javaTypeResolver>
			<property name="forceBigDecimals" value="false"/>
		</javaTypeResolver>

		<!-- model generator -->
		<javaModelGenerator targetPackage="${modelTargetPackage}" targetProject="${modelTargetDir}">
			<property name="enableSubPackages" value="true"/>
		</javaModelGenerator>

		<!-- xml generator -->
		<sqlMapGenerator targetPackage="${xmlTargetPackage}" targetProject="${xmlTargetDir}">
			<property name="enableSubPackages" value="true"/>
		</sqlMapGenerator>

		<table tableName="test" alias="test" domainObjectName="Test">
			<property name="useActualColumnNames" value="false"/>
			<generatedKey column="id" sqlStatement="JDBC"/>
			<columnOverride column="created_at" javaType="Long" jdbcType="TIMESTAMP"
							typeHandler="com.myteek.mybatis.generator.handler.TimestampTypeHandler"/>
			<columnOverride column="updated_at" javaType="Long" jdbcType="TIMESTAMP"
							typeHandler="com.myteek.mybatis.generator.handler.TimestampTypeHandler"/>
		</table>

	</context>
</generatorConfiguration>
```

## Maven config
```xml
<plugins>
	<plugin>
		<groupId>org.mybatis.generator</groupId>
		<artifactId>mybatis-generator-maven-plugin</artifactId>
		<version>1.4.0</version>
		<configuration>
			<configurationFile>${project.basedir}/src/main/resources/generatorConfig.xml</configurationFile>
			<verbose>true</verbose>
			<overwrite>true</overwrite>
		</configuration>
		<executions>
			<execution>
				<id>generate</id>
				<goals>
					<goal>generate</goal>
				</goals>
			</execution>
		</executions>
		<dependencies>
			<dependency>
				<groupId>mysql</groupId>
				<artifactId>mysql-connector-java</artifactId>
				<version>8.0.26</version>
			</dependency>
			<dependency>
				<groupId>org.mybatis.generator</groupId>
				<artifactId>mybatis-generator-core</artifactId>
				<version>1.4.0</version>
			</dependency>
			<dependency>
				<groupId>com.myteek</groupId>
				<artifactId>myteek-mybatis</artifactId>
				<version>1.0-SNAPSHOT</version>
			</dependency>
		</dependencies>
	</plugin>
</plugins>
```
