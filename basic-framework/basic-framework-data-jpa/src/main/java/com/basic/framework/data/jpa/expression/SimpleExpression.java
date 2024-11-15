package com.basic.framework.data.jpa.expression;

import com.basic.framework.data.jpa.enums.SqlKeywordEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 简单表达式
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleExpression {

    /**
     * 列名
     */
    private String property;

    /**
     * 值
     */
    private Serializable value;

    /**
     * SQL关键字
     */
    private SqlKeywordEnum keyword;

    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return keyword.toPredicate(property, value, root, criteriaBuilder);
    }
}
