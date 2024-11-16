package com.basic.framework.data.jpa.expression;

import com.basic.framework.data.jpa.enums.SqlKeywordEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Collection;

/**
 * in表达式
 *
 * @author vains
 */
public class InExpression extends MultipleValueExpression {

    /**
     * in查询条件表达式
     *
     * @param property 属性名
     * @param values   in的值
     */
    public InExpression(String property, Collection<Serializable> values) {
        super(property, SqlKeywordEnum.IN, values);
    }

    /**
     * in查询条件表达式
     *
     * @param property   属性名
     * @param values     in的值
     * @param sqlKeyword sql关键字枚举(in/not in)
     */
    public InExpression(String property, Collection<Serializable> values, SqlKeywordEnum sqlKeyword) {
        super(property, sqlKeyword, values);
        if (sqlKeyword != SqlKeywordEnum.IN && sqlKeyword != SqlKeywordEnum.NOT_IN) {
            throw new IllegalArgumentException("SqlKeywordEnum must be IN or NOT_IN");
        }
    }

    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (ObjectUtils.isEmpty(super.getValues())) {
            return null;
        }
        return super.getKeyword().toPredicate(super.getProperty(), super.getValues(), root);
    }

}
