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

    public BetweenExpression(String property, Serializable value1, Serializable value2) {
        super(property, SqlKeywordEnum.BETWEEN_AND, List.of(value1, value2));
    }

    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (ObjectUtils.isEmpty(super.getValues())
                && super.getValues().size() != 2) {
            return null;
        }
        Serializable[] array = super.getValues().toArray(new Serializable[0]);
        return super.getKeyword().toPredicate(super.getProperty(), array[0], array[1], root, criteriaBuilder);
    }

}
