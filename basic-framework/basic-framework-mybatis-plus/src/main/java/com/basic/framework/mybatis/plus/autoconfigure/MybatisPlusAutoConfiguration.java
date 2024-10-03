package com.basic.framework.mybatis.plus.autoconfigure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.basic.framework.mybatis.plus.handler.BasicMetaObjectHandler;
import com.basic.framework.mybatis.plus.handler.MybatisBasicEnumTypeHandler;
import com.basic.framework.mybatis.plus.handler.type.BasicCollectionTypeHandler;
import com.basic.framework.mybatis.plus.handler.type.BasicMapTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Mybatis Plus 自动配置类
 *
 * @author vains
 */
@Import({BasicMetaObjectHandler.class})
public class MybatisPlusAutoConfiguration {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 如果配置多个插件,切记分页最后添加，如果有多数据源可以不配具体类型 否则都建议配上具体的DbType
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 自定义枚举转换器注册
     *
     * @return MybatisPlusPropertiesCustomizer
     */
    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        return properties -> {
            GlobalConfig globalConfig = properties.getGlobalConfig();
            globalConfig.setBanner(false);
            MybatisPlusProperties.CoreConfiguration configuration = new MybatisPlusProperties.CoreConfiguration();
            configuration.setDefaultEnumTypeHandler(MybatisBasicEnumTypeHandler.class);
            properties.setConfiguration(configuration);
        };
    }

    /**
     * 注册Mybatis Plus的TypeHandler，无需在yaml中添加包扫描即可生效
     *
     * @return ConfigurationCustomizer
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.getTypeHandlerRegistry().register(BasicMapTypeHandler.class);
            configuration.getTypeHandlerRegistry().register(BasicCollectionTypeHandler.class);
        };
    }

}
