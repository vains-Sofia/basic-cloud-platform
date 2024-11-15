package com.basic.framework.data.jpa.specification;

import com.basic.framework.data.jpa.enums.SqlKeywordEnum;
import com.basic.framework.data.jpa.expression.BetweenExpression;
import com.basic.framework.data.jpa.expression.InExpression;
import com.basic.framework.data.jpa.expression.SimpleExpression;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpecificationBuilder<T> implements Specification<T> {

    private final List<SimpleExpression> expressions = new ArrayList<>();

    @Override
    public Predicate toPredicate(@Nullable Root<T> root, CriteriaQuery<?> query, @Nullable CriteriaBuilder criteriaBuilder) {
        if (criteriaBuilder == null) {
            return null;
        }
        if (expressions.isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        List<Predicate> predicates = new ArrayList<>();
        for (SimpleExpression c : expressions) {
            predicates.add(c.toPredicate(root, query, criteriaBuilder));
        }
        // 将所有条件用 and 联合起来
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * 等于
     *
     * @param property 列名
     * @param value    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> eq(String property, Serializable value) {
        return this.eq(Boolean.TRUE, property, value);
    }

    /**
     * 等于
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> eq(Boolean condition, String property, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(property, value, SqlKeywordEnum.EQ));
        }
        return this;
    }

    /**
     * 不等于
     *
     * @param property 列名
     * @param value    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ne(String property, Serializable value) {
        return this.ne(Boolean.TRUE, property, value);
    }

    /**
     * 不等于
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ne(Boolean condition, String property, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(property, value, SqlKeywordEnum.NE));
        }
        return this;
    }

    /**
     * 模糊匹配
     *
     * @param property 列名
     * @param value    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> like(String property, Serializable value) {
        return this.like(Boolean.TRUE, property, value);
    }

    /**
     * 模糊匹配
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> like(Boolean condition, String property, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(property, value, SqlKeywordEnum.LIKE));
        }
        return this;
    }

    /**
     * 大于
     *
     * @param property 列名
     * @param value    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> gt(String property, Serializable value) {
        return this.gt(Boolean.TRUE, property, value);
    }

    /**
     * 大于
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> gt(Boolean condition, String property, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(property, value, SqlKeywordEnum.GT));
        }
        return this;
    }

    /**
     * 大于等于
     *
     * @param property 列名
     * @param value    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ge(String property, Serializable value) {
        return this.ge(Boolean.TRUE, property, value);
    }

    /**
     * 大于等于
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ge(Boolean condition, String property, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(property, value, SqlKeywordEnum.GE));
        }
        return this;
    }

    /**
     * 小于
     *
     * @param property 列名
     * @param value    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> lt(String property, Serializable value) {
        return this.lt(Boolean.TRUE, property, value);
    }

    /**
     * 小于
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> lt(Boolean condition, String property, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(property, value, SqlKeywordEnum.LT));
        }
        return this;
    }

    /**
     * 小于等于
     *
     * @param property 列名
     * @param value    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> le(String property, Serializable value) {
        return this.le(Boolean.TRUE, property, value);
    }

    /**
     * 小于等于
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> le(Boolean condition, String property, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(property, value, SqlKeywordEnum.LE));
        }
        return this;
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param property 列名
     * @param value1   值1
     * @param value2   值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> between(String property, Serializable value1, Serializable value2) {
        return this.between(Boolean.TRUE, property, value1, value2);
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param value1    值1
     * @param value2    值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> between(Boolean condition, String property, Serializable value1, Serializable value2) {
        if (condition) {
            expressions.add(new BetweenExpression(property, value1, value2));
        }
        return this;
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param property 列名
     * @param values   值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(String property, Collection<Serializable> values) {
        return this.in(Boolean.TRUE, property, values);
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param property 列名
     * @param values   值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(String property, Serializable... values) {
        if (ObjectUtils.isEmpty(values)) {
            return this;
        }
        return this.in(Boolean.TRUE, property, values);
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(Boolean condition, String property, Collection<Serializable> values) {
        if (condition) {
            expressions.add(new InExpression(property, values));
        }
        return this;
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param condition 条件，为true时会组装条件
     * @param property  列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(Boolean condition, String property, Serializable... values) {
        if (condition && !ObjectUtils.isEmpty(values)) {
            expressions.add(new InExpression(property, List.of(values)));
        }
        return this;
    }

    /**
     * 等于
     *
     * @param property 列名
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> isNull(String property) {
        expressions.add(new SimpleExpression(property, null, SqlKeywordEnum.IS_NULL));
        return this;
    }

    /**
     * 等于
     *
     * @param property 列名
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> isNotNull(String property) {
        expressions.add(new SimpleExpression(property, null, SqlKeywordEnum.IS_NOT_NULL));
        return this;
    }
}
