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

    public InExpression(String property, Collection<Serializable> values) {
        super(property, SqlKeywordEnum.IN, values);
    }

    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (ObjectUtils.isEmpty(super.getValues())) {
            return null;
        }
        return super.getKeyword().toPredicate(super.getProperty(), super.getValues(), root);
    }

}
