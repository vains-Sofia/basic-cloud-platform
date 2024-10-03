package com.basic.example.mybatis.plus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import org.junit.jupiter.api.Test;

import java.sql.Types;
import java.util.Collections;

/**
 * MybatisPlus生成代码测试
 *
 * @author vains
 */
public class GeneratorTests {

    @Test
    public void generator() {
        FastAutoGenerator.create("jdbc:mysql://192.168.187.128:3306/basic-examples?serverTimezone=UTC&userUnicode=true&characterEncoding=utf-8&tinyInt1isBit=true&remarks=true&useInformationSchema=true", "root", "root")
                .globalConfig(builder -> {
                    builder.author("vains") // 设置作者
                            .outputDir("D://generator//"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.basic.cloud.oauth2.authorization") // 设置父包名
                            .moduleName("server") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D://generator//")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("authorization,authorization_consent,client") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .entityBuilder()
                            .superClass(BasicEntity.class)
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .enableFileOverride();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
