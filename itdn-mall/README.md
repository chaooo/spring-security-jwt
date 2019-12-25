# itdn_mall
## Spring Boot 电商后台


### 1. 开发环境
1. `IDEA Community 2019.2.3`
    + idea插件(`Spring Assistant`, `Free Mybatis plugin`)
    + 社区版需要`Spring Assistant`插件提供对`spring boot/cloud`工程的支持
    + `Free Mybatis plugin`插件提供`Mapper.xml`跳转回到`Mapper接口`的功能
2. `JDK1.8`
3. `MySQL5.7`


### 2. 数据库设计
1. 【商品类别表】
    + `category`(`id`,`parent_id`,`name`,`status`,`sort_order`,`create_time`,`update_time`)
2. 【商品表】
    + `product`(`id`,`category_id`,`name`,`subtitle`,`main_image`,`sub_images`,`detail`,`price`,`stock`,`status`,`create_time`,`update_time`)
3. 【购物车表】
    + `cart`(`id`,`user_id`,`product_id`,`quantity`,`checked`,`create_time`,`update_time`)
4. 【支付表】
    + `pay_info`(`id`,`user_id`,`order_no`,`pay_platform`,`platform_number`,`platform_status`,`create_time`,`update_time`)
5. 【订单表】
    + `order_list`(`id`,`order_no`,`user_id`,`shipping_id`,`payment`,`payment_type`,`postage`,`status`,`payment_time`,`send_time`,`end_time`,`close_time`,`create_time`,`update_time`)
6. 【订单子表】
    + `order_item`(`id`,`user_id`,`order_no`,`product_id`,`product_name`,`product_image`,`current_unit_price`,`quantity`,`total_price`,`create_time`,`update_time`)
7. 【用户表】
    + `user`(`id`,`username`,`password`,`email`,`phone`,`question`,`answer`,`role`,`create_time`,`update_time`)
8. 【收货信息表】
    + `shipping`(`id`,`user_id`,`receiver_name`,`receiver_phone`,`receiver_mobile`,`receiver_province`,`receiver_city`,`receiver_district`,`receiver_address`,`receiver_zip`,`create_time`,`update_time`)



### 3. 使用MyBatis Generator自动生成entity、mapper和dao层
1. 在pom.xml中加入插件依赖：
``` xml
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.7</version>
</dependency>
```

2. 在resources/下编写generatorConfig.xml配置文件
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<!-- 配置生成器 -->
<generatorConfiguration>
    <!-- 指定驱动包 -->
    <classPathEntry location="D:/apache-maven-3.6.1/.m2/repository/mysql/mysql-connector-java/5.1.48/mysql-connector-java-5.1.48.jar" />
    <!-- 一个数据库一个context,defaultModelType="flat" 大数据字段，不分表 -->
    <context id="MysqlTables" targetRuntime="MyBatis3Simple" defaultModelType="flat">
        <!-- 自动识别数据库关键字，默认false，
        如果设置为true，根据SqlReservedWords中定义的关键字列表；
        一般保留默认值，遇到数据库关键字（Java关键字），使用columnOverride覆盖 -->
        <property name="autoDelimitKeywords" value="true"/>
        <!-- 生成的Java文件的编码 -->
        <property name="javaFileEncoding" value="utf-8"/>
        <!-- 注释 -->
        <commentGenerator>
            <property name="suppressAllComments" value="true"/><!-- 是否取消注释 -->
            <property name="suppressDate" value="false"/> <!-- 是否生成注释代时间戳-->
        </commentGenerator>
        <!-- 指定数据库连接参数 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/itdn_mall"
                        userId="root"
                        password="123qwe">
            <property name="nullCatalogMeansCurrent" value="true"/>
        </jdbcConnection>
        <!-- 类型转换 默认false，
            把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer
            true，把JDBC DECIMAL 和 NUMERIC 类型解析为java.math.BigDecimal-->
        <javaTypeResolver >
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>
        <!-- 指定生成 实体类  路径-->
        <javaModelGenerator targetPackage="top.itdn.mall.entity" targetProject="./src/main/java">
            <!-- 是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
            <!-- 从数据库返回的值被清理前后的空格  -->
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- 指定 SQL定义xml文件  路径-->
        <sqlMapGenerator targetPackage="mappers"  targetProject="./src/main/resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- 指定 Mapper映射器（dao）文件类型  路径
        type=[XMLMAPPER, ANNOTATEDMAPPER]-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="top.itdn.mall.dao"  targetProject="./src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 指定  数据库的 基本表-->
        <table tableName="%" enableSelectByExample="false" enableDeleteByExample="false" enableCountByExample="false" enableUpdateByExample="false" selectByExampleQueryId="flase">
            <property name="useActualColumnNames" value="true"/>
            <generatedKey column="ID" sqlStatement="mysql" identity="true" />
        </table>
    </context>
</generatorConfiguration>
```

3. 编写MybatisGenerater.java main方法
``` java
public class MybatisGenerater {
    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        File configFile =  new File("src/main/resources/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
```

4.运行main函数，刷新，生成成功，目录结构：
```
itdnMall
└── src.main
       ├── java
       │   └── top.itdn.mall
       │       ├── dao/
       │       ├── entity/
       │       ├── MybatisGenerater.java  
       │       └── RunApplication.java
       └── resources
            ├── static/
            ├── templates/
            ├── mappers/
            ├── generatorConfig.xml
            └── application.yml
```


### 后台管理模块


