package com.basic.framework.data.jpa.expression;

import com.basic.framework.data.jpa.enums.SqlKeywordEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;

/**
 * between ... and ... 表达式
 *
 * @author vains
 */
public class BetweenExpression extends MultipleValueExpression {

    /**
     * property的值在 value1 与 value2 之间
     *
     * @param property 列名
     * @param value1   开始值
     * @param value2   结束值
     */
    public BetweenExpression(String property, Serializable value1, Serializable value2) {
        super(property, SqlKeywordEnum.BETWEEN, List.of(value1, value2));
    }

    /**
     * property的值在 value1 与 value2 之间
     *
     * @param property   列名
     * @param value1     开始值
     * @param value2     结束值
     * @param sqlKeyword SQL关键字
     */
    public BetweenExpression(String property, Serializable value1, Serializable value2, SqlKeywordEnum sqlKeyword) {
        super(property, sqlKeyword, List.of(value1, value2));
        if (sqlKeyword != SqlKeywordEnum.BETWEEN && sqlKeyword != SqlKeywordEnum.NOT_BETWEEN) {
            throw new IllegalArgumentException("sqlKeyword must be between or not");
        }
    }

    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (ObjectUtils.isEmpty(super.getValues())
                && super.getValues().size() != 2) {
            // 只能有两个值才能组成条件
            return null;
        }
        Serializable[] array = super.getValues().toArray(new Serializable[0]);
        // 构建条件
        return super.getKeyword().toPredicate(super.getProperty(), array[0], array[1], root, criteriaBuilder);
    }

}
