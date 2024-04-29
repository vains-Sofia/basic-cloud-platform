package com.basic.cloud.spring.doc.customizer;

import com.basic.cloud.core.enums.BasicEnum;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.MethodParameter;

/**
 * 枚举参数自定义配置
 *
 * @author vains
 */
public class ApiEnumParameterCustomizer implements ParameterCustomizer, BasicEnumCustomizer {

    @Override
    public Parameter customize(Parameter parameterModel, MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();

        // 枚举处理
        if (BasicEnum.class.isAssignableFrom(parameterType)) {

            parameterModel.setDescription(getDescription(parameterType));

            Schema<Object> schema = new Schema<>();
            schema.setEnum(getValues(parameterType));
            parameterModel.setSchema(schema);
        }

        return parameterModel;
    }
}

