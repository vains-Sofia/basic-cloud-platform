package com.basic.cloud.mybatis.plus.util;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.reflection.property.PropertyNamer;

/**
 * Lambda方法帮助了类
 *
 * @author vains
 */
@UtilityClass
public class LambdaMethodUtils {

    /**
     * 根据 lambda 方法获取数据库对应的字段
     *
     * @param column get方法
     * @param <T>    实体类型
     * @return 字段名
     */
    public static <T> String extractMethodToProperty(SFunction<T, ?> column) {
        LambdaMeta metaCreateBy = LambdaUtils.extract(column);
        return PropertyNamer.methodToProperty(metaCreateBy.getImplMethodName());
    }

}
