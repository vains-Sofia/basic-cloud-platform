package com.basic.framework.data.jpa.lambda;

import jakarta.persistence.Column;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Lambda工具类
 *
 * @author vains
 */
public class LambdaUtils {

    /**
     * 根据 lambda 方法获取数据库对应的字段
     *
     * @param fn  get方法lambda表达式
     * @param <T> 实体类型
     * @return 字段名
     */
    public static <T> String extractMethodToProperty(SFunction<T, ?> fn) {
        // 获取get方法lambda表达式的字段
        Field field = getField(fn);
        if (field != null) {
            return field.getName();
        }
        return null;
    }

    /**
     * 将bean的属性的get方法，作为lambda表达式传入时，获取get方法对应的属性Field
     *
     * @param fn  lambda表达式，bean的属性的get方法
     * @param <T> 泛型
     * @return 属性对象
     */
    public static <T> Field getField(SFunction<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.canAccess(fn);
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从lambda信息取出method、field、class等
        String fieldName = getFieldName(serializedLambda);
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return field;
    }

    /**
     * 获取字段名称
     *
     * @param serializedLambda lambda表达式的序列化形式
     * @return 字段名称
     */
    private static String getFieldName(SerializedLambda serializedLambda) {
        String implMethodName = serializedLambda.getImplMethodName();
        // 确保方法是符合规范的get方法，boolean类型是is开头
        if (!implMethodName.startsWith("is") && !implMethodName.startsWith("get")) {
            throw new RuntimeException("get方法名称: " + implMethodName + ", 不符合java bean规范");
        }

        // get方法开头为 is 或者 get，将方法名 去除is或者get，然后首字母小写，就是属性名
        int prefixLen = implMethodName.startsWith("is") ? 2 : 3;

        String fieldName = implMethodName.substring(prefixLen);
        String firstChar = fieldName.substring(0, 1);
        fieldName = fieldName.replaceFirst(firstChar, firstChar.toLowerCase());
        return fieldName;
    }

    /**
     * 获取Spring data jpa的 {@link Column} 注解
     *
     * @param field 反射获取到的字段
     * @return jpa的 Column 注解
     */
    private static Column getColumnAnnotation(Field field) {
        if (field != null && field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class);
        }
        return null;
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param camelCaseStr 驼峰命名的字符串
     * @return 转换后的下划线命名的字符串
     */
    public static String camelToUnderscore(String camelCaseStr) {
        if (camelCaseStr == null || camelCaseStr.isEmpty()) {
            return camelCaseStr;
        }

        StringBuilder result = new StringBuilder(camelCaseStr.length() * 2);
        int i = 0;
        char[] chars = camelCaseStr.toCharArray();
        for (char c : chars) {
            if (i > 0 && Character.isUpperCase(c)) {
                result.append('_');
            }
            result.append(Character.toLowerCase(c));
            i++;
        }

        return result.toString();
    }

}
