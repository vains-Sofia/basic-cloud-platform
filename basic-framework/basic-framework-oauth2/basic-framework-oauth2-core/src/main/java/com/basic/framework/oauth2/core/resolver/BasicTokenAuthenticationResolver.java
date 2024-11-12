package com.basic.framework.oauth2.core.resolver;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 获取根据token类型使用不同类型的解析器
 *
 * @author vains
 */
public interface BasicTokenAuthenticationResolver {

    /**
     * 根据传入class获取ioc中的bean
     *
     * @param applicationContext ioc容器对象，获取
     * @param type               要获取的bean的class
     * @param <T>                class的具体类型
     * @return bean的实例
     */
    default <T> T getOptionalBean(ApplicationContext applicationContext, Class<T> type) {
        Map<String, T> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, type);
        if (beansMap.size() > 1) {
            throw new NoUniqueBeanDefinitionException(type, beansMap.size(),
                    "Expected single matching bean of type '" + type.getName() + "' but found " +
                            beansMap.size() + ": " + StringUtils.collectionToCommaDelimitedString(beansMap.keySet()));
        }
        return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
    }

}
