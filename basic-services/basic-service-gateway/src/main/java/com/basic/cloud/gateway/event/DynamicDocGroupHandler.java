package com.basic.cloud.gateway.event;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;
import static org.springdoc.core.utils.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;

/**
 * 通过路由更新的监听动态刷新Spring Doc的Group
 *
 * @author YuJx
 */
@Slf4j
@Component
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
public class DynamicDocGroupHandler implements ApplicationListener<RefreshRoutesEvent> {

    /**
     * swagger只收集统一微服务下的模块
     */
    private final String LB_SCHEME = "lb";

    /**
     * 获取微服务代理时拦截的路径predicate
     */
    private final String PATH_PREDICATE = "Path";

    private final RouteDefinitionLocator routeDefinitionLocator;

    private final SwaggerUiConfigParameters swaggerUiConfigParameters;

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    private final Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> DEFAULT_SWAGGER_URLS;

    public DynamicDocGroupHandler(RouteDefinitionLocator routeDefinitionLocator,
                                  SwaggerUiConfigParameters swaggerUiConfigParameters,
                                  SwaggerUiConfigProperties swaggerUiConfigProperties) {
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.swaggerUiConfigParameters = swaggerUiConfigParameters;
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.DEFAULT_SWAGGER_URLS = swaggerUiConfigParameters.getUrls();
    }

    @Override
    public void onApplicationEvent(@Nullable RefreshRoutesEvent event) {
        if (event == null) {
            return;
        }
        // 新添加的配置
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new LinkedHashSet<>();

        if (log.isDebugEnabled()) {
            log.debug("路由发生变化，开始刷新在线文档的Group...");
        }

        // 获取最新路由数据
        routeDefinitionLocator.getRouteDefinitions().subscribe(routeDefinition -> {
            // 只处理注册在注册中心的route
            if (!Objects.equals(routeDefinition.getUri().getScheme(), LB_SCHEME)) {
                return;
            }

            if (!ObjectUtils.isEmpty(DEFAULT_SWAGGER_URLS)) {
                // 如果swagger中已存在则移除
                DEFAULT_SWAGGER_URLS.removeIf(url -> Objects.equals(url.getName(), routeDefinition.getId()));
            }

            // 筛选出带有path predicate的路由
            Optional<PredicateDefinition> pathPredicate = routeDefinition.getPredicates()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(predicateDefinition -> Objects.equals(predicateDefinition.getName(), PATH_PREDICATE))
                    .findFirst();
            pathPredicate.ifPresent(path -> {
                // 提取出路由拦截的路径
                String pathArg = path.getArgs().values().stream().toList().getFirst();
                if (ObjectUtils.isEmpty(pathArg)) {
                    return;
                }

                // 拦截路径的习惯一般是 /path/** ，所以这里直接替换掉最后的/**
                String docUrl = pathArg.replaceAll("/\\*\\*", "") + DEFAULT_API_DOCS_URL;
                // 把路由id当做模块名
                String docName = routeDefinition.getId();

                urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(docName, docUrl, docName));
            });

        });

        if (!ObjectUtils.isEmpty(DEFAULT_SWAGGER_URLS)) {
            // 合并
            urls.addAll(DEFAULT_SWAGGER_URLS);
        }

        if (log.isDebugEnabled()) {
            String groups = urls.stream()
                    .map(AbstractSwaggerUiConfigProperties.SwaggerUrl::getName)
                    .collect(Collectors.joining(","));
            log.debug("刷新Spring Gateway Doc Group成功，获取到组：{}.", groups);
        }
        // 重置urls配置
        swaggerUiConfigParameters.setUrls(urls);
        swaggerUiConfigProperties.setUrls(urls);
    }
}
