package com.basic.framework.data.jpa.specification;

import com.basic.framework.data.jpa.enums.SqlKeywordEnum;
import com.basic.framework.data.jpa.expression.BetweenExpression;
import com.basic.framework.data.jpa.expression.InExpression;
import com.basic.framework.data.jpa.expression.SimpleExpression;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.lambda.SFunction;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.config.Customizer;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Specification 条件构造器
 *
 * @param <T> 实体类型
 * @author vains
 * @see org.springframework.data.jpa.domain.Specification
 */
@Getter
@Setter
public class SpecificationBuilder<T> implements Specification<T> {

    private final List<SimpleExpression> expressions = new ArrayList<>();

    private final List<SpecificationBuilder<T>> orBuilders = new ArrayList<>();

    @Override
    public Predicate toPredicate(@Nullable Root<T> root, CriteriaQuery<?> query, @Nullable CriteriaBuilder criteriaBuilder) {
        // 默认使用and
        if (criteriaBuilder == null) {
            return null;
        }
        if (expressions.isEmpty() && orBuilders.isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        Predicate andPredicate = null;
        if (!expressions.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (SimpleExpression c : expressions) {
                predicates.add(c.toPredicate(root, query, criteriaBuilder));
            }
            // 将所有条件用 and 联合起来
            andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        if (orBuilders.isEmpty()) {
            if (andPredicate == null) {
                return criteriaBuilder.conjunction();
            } else {
                return andPredicate;
            }
        }
        // 如果or表达式不为空则生成or条件
        // 生成的or表达式列表
        List<List<Predicate>> orPredicatesList = new ArrayList<>();
        // 获取所有的or表达式
        List<List<SimpleExpression>> orExpressionsList = orBuilders
                .stream()
                .map(SpecificationBuilder::getExpressions)
                .toList();
        for (List<SimpleExpression> orExpressions : orExpressionsList) {
            // 组装，转为框架内的表达式
            List<Predicate> orPredicates = new ArrayList<>();
            for (SimpleExpression c : orExpressions) {
                orPredicates.add(c.toPredicate(root, query, criteriaBuilder));
            }
            orPredicatesList.add(orPredicates);
        }

        // 所有的or表达式
        Predicate[] orPredicateArray = orPredicatesList.stream()
                .map(e -> criteriaBuilder.or(e.toArray(new Predicate[0])))
                .toList().toArray(new Predicate[0]);

        if (andPredicate == null) {
            // 如果and表达式为空则只返回or表达式
            return query.where(orPredicateArray).getRestriction();
        }

        // 将or和and表达式转到一个数组中
        Predicate[] orAndPredicateArray = Arrays.copyOf(orPredicateArray, orPredicateArray.length + 1);
        orAndPredicateArray[orPredicateArray.length] = andPredicate;
        // 拼接and与or表达式
        return query.where(orAndPredicateArray).getRestriction();
    }

    /**
     * 等于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> eq(SFunction<T, ?> column, Serializable value) {
        return this.eq(LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 等于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> eq(String column, Serializable value) {
        return this.eq(Boolean.TRUE, column, value);
    }

    /**
     * 等于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> eq(Boolean condition, SFunction<T, ?> column, Serializable value) {
        return this.eq(condition, LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 等于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> eq(Boolean condition, String column, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(column, value, SqlKeywordEnum.EQ));
        }
        return this;
    }

    /**
     * 不等于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ne(SFunction<T, ?> column, Serializable value) {
        return this.ne(LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 不等于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ne(String column, Serializable value) {
        return this.ne(Boolean.TRUE, column, value);
    }

    /**
     * 不等于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ne(Boolean condition, SFunction<T, ?> column, Serializable value) {
        return this.ne(condition, LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 不等于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ne(Boolean condition, String column, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(column, value, SqlKeywordEnum.NE));
        }
        return this;
    }

    /**
     * 模糊匹配
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> like(SFunction<T, ?> column, Serializable value) {
        return this.like(LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 模糊匹配
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> like(String column, Serializable value) {
        return this.like(Boolean.TRUE, column, value);
    }

    /**
     * 模糊匹配
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> like(Boolean condition, SFunction<T, ?> column, Serializable value) {
        return this.like(condition, LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 模糊匹配
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> like(Boolean condition, String column, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(column, value, SqlKeywordEnum.LIKE));
        }
        return this;
    }

    /**
     * 大于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> gt(SFunction<T, ?> column, Serializable value) {
        return this.gt(LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 大于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> gt(String column, Serializable value) {
        return this.gt(Boolean.TRUE, column, value);
    }

    /**
     * 大于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> gt(Boolean condition, SFunction<T, ?> column, Serializable value) {
        return this.gt(condition, LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 大于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> gt(Boolean condition, String column, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(column, value, SqlKeywordEnum.GT));
        }
        return this;
    }

    /**
     * 大于等于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ge(SFunction<T, ?> column, Serializable value) {
        return this.ge(LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 大于等于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ge(String column, Serializable value) {
        return this.ge(Boolean.TRUE, column, value);
    }

    /**
     * 大于等于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ge(Boolean condition, SFunction<T, ?> column, Serializable value) {
        return this.ge(condition, LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 大于等于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> ge(Boolean condition, String column, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(column, value, SqlKeywordEnum.GE));
        }
        return this;
    }

    /**
     * 小于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> lt(SFunction<T, ?> column, Serializable value) {
        return this.lt(LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 小于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> lt(String column, Serializable value) {
        return this.lt(Boolean.TRUE, column, value);
    }

    /**
     * 小于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> lt(Boolean condition, SFunction<T, ?> column, Serializable value) {
        return this.lt(condition, LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 小于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> lt(Boolean condition, String column, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(column, value, SqlKeywordEnum.LT));
        }
        return this;
    }

    /**
     * 小于等于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> le(SFunction<T, ?> column, Serializable value) {
        return this.le(LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 小于等于
     *
     * @param column 列名
     * @param value  值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> le(String column, Serializable value) {
        return this.le(Boolean.TRUE, column, value);
    }

    /**
     * 小于等于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> le(Boolean condition, SFunction<T, ?> column, Serializable value) {
        return this.le(condition, LambdaUtils.extractMethodToProperty(column), value);
    }

    /**
     * 小于等于
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value     值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> le(Boolean condition, String column, Serializable value) {
        if (condition) {
            expressions.add(new SimpleExpression(column, value, SqlKeywordEnum.LE));
        }
        return this;
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param column 列名
     * @param value1 值1
     * @param value2 值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> between(SFunction<T, ?> column, Serializable value1, Serializable value2) {
        return this.between(LambdaUtils.extractMethodToProperty(column), value1, value2);
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param column 列名
     * @param value1 值1
     * @param value2 值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> between(String column, Serializable value1, Serializable value2) {
        return this.between(Boolean.TRUE, column, value1, value2);
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value1    值1
     * @param value2    值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> between(Boolean condition, SFunction<T, ?> column, Serializable value1, Serializable value2) {
        return this.between(condition, LambdaUtils.extractMethodToProperty(column), value1, value2);
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value1    值1
     * @param value2    值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> between(Boolean condition, String column, Serializable value1, Serializable value2) {
        if (condition) {
            expressions.add(new BetweenExpression(column, value1, value2));
        }
        return this;
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param column 列名
     * @param value1 值1
     * @param value2 值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notBetween(SFunction<T, ?> column, Serializable value1, Serializable value2) {
        return this.notBetween(LambdaUtils.extractMethodToProperty(column), value1, value2);
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param column 列名
     * @param value1 值1
     * @param value2 值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notBetween(String column, Serializable value1, Serializable value2) {
        return this.notBetween(Boolean.TRUE, column, value1, value2);
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value1    值1
     * @param value2    值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notBetween(Boolean condition, SFunction<T, ?> column, Serializable value1, Serializable value2) {
        return this.notBetween(condition, LambdaUtils.extractMethodToProperty(column), value1, value2);
    }

    /**
     * 在 `value1` 和 `value2`之间
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param value1    值1
     * @param value2    值2
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notBetween(Boolean condition, String column, Serializable value1, Serializable value2) {
        if (condition) {
            expressions.add(new BetweenExpression(column, value1, value2, SqlKeywordEnum.NOT_BETWEEN));
        }
        return this;
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param column 列名
     * @param values 值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(SFunction<T, ?> column, Collection<?> values) {
        return this.in(LambdaUtils.extractMethodToProperty(column), values);
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param column 列名
     * @param values 值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(String column, Collection<?> values) {
        return this.in(Boolean.TRUE, column, values);
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param column 列名
     * @param values 值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(SFunction<T, ?> column, Serializable... values) {
        if (ObjectUtils.isEmpty(values)) {
            return this;
        }
        return this.in(LambdaUtils.extractMethodToProperty(column), values);
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param column 列名
     * @param values 值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(String column, Serializable... values) {
        if (ObjectUtils.isEmpty(values)) {
            return this;
        }
        return this.in(Boolean.TRUE, column, values);
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(Boolean condition, SFunction<T, ?> column, Collection<?> values) {
        return this.in(condition, LambdaUtils.extractMethodToProperty(column), values);
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(Boolean condition, SFunction<T, ?> column, Serializable... values) {
        return this.in(condition, LambdaUtils.extractMethodToProperty(column), values);
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(Boolean condition, String column, Collection<?> values) {
        if (condition) {
            expressions.add(new InExpression(column, values));
        }
        return this;
    }

    /**
     * 检查列的值是否在指定值列表中
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> in(Boolean condition, String column, Serializable... values) {
        if (condition && !ObjectUtils.isEmpty(values)) {
            expressions.add(new InExpression(column, List.of(values)));
        }
        return this;
    }

    /**
     * 检索列的值是不在在指定值列表中的数据
     *
     * @param column 列名
     * @param values 值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notIn(SFunction<T, ?> column, Collection<Serializable> values) {
        return this.notIn(LambdaUtils.extractMethodToProperty(column), values);
    }

    /**
     * 检索列的值是不在在指定值列表中的数据
     *
     * @param column 列名
     * @param values 值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notIn(String column, Collection<Serializable> values) {
        return this.notIn(Boolean.TRUE, column, values);
    }

    /**
     * 检索列的值是不在在指定值列表中的数据
     *
     * @param column 列名
     * @param values 值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notIn(SFunction<T, ?> column, Serializable... values) {
        if (ObjectUtils.isEmpty(values)) {
            return this;
        }
        return this.notIn(LambdaUtils.extractMethodToProperty(column), values);
    }

    /**
     * 检索列的值是不在在指定值列表中的数据
     *
     * @param column 列名
     * @param values 值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notIn(String column, Serializable... values) {
        if (ObjectUtils.isEmpty(values)) {
            return this;
        }
        return this.notIn(Boolean.TRUE, column, values);
    }

    /**
     * 检索列的值是不在在指定值列表中的数据
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notIn(Boolean condition, String column, Collection<Serializable> values) {
        if (condition) {
            expressions.add(new InExpression(column, values, SqlKeywordEnum.NOT_IN));
        }
        return this;
    }

    /**
     * 检索列的值是不在在指定值列表中的数据
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notIn(Boolean condition, String column, Serializable... values) {
        if (condition && !ObjectUtils.isEmpty(values)) {
            expressions.add(new InExpression(column, List.of(values), SqlKeywordEnum.NOT_IN));
        }
        return this;
    }

    /**
     * 检索列的值是不在在指定值列表中的数据
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notIn(Boolean condition, SFunction<T, ?> column, Collection<Serializable> values) {
        return this.notIn(condition, LambdaUtils.extractMethodToProperty(column), values);
    }

    /**
     * 检索列的值是不在在指定值列表中的数据
     *
     * @param condition 条件，为true时会组装条件
     * @param column    列名
     * @param values    值
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> notIn(Boolean condition, SFunction<T, ?> column, Serializable... values) {
        return this.notIn(condition, LambdaUtils.extractMethodToProperty(column), values);
    }

    /**
     * 等于null
     *
     * @param column 列名
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> isNull(SFunction<T, ?> column) {
        expressions.add(new SimpleExpression(LambdaUtils.extractMethodToProperty(column), null, SqlKeywordEnum.IS_NULL));
        return this;
    }

    /**
     * 等于null
     *
     * @param column 列名
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> isNull(String column) {
        expressions.add(new SimpleExpression(column, null, SqlKeywordEnum.IS_NULL));
        return this;
    }

    /**
     * 不等于null
     *
     * @param column 列名
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> isNotNull(SFunction<T, ?> column) {
        expressions.add(new SimpleExpression(LambdaUtils.extractMethodToProperty(column), null, SqlKeywordEnum.IS_NOT_NULL));
        return this;
    }

    /**
     * 不等于null
     *
     * @param column 列名
     * @return 当前对象，链式调用
     */
    public SpecificationBuilder<T> isNotNull(String column) {
        expressions.add(new SimpleExpression(column, null, SqlKeywordEnum.IS_NOT_NULL));
        return this;
    }

    /**
     * 添加or表达式
     *
     * @param builderCustomizer 自定义构建or表达式入口
     * @return 当前builder
     */
    public SpecificationBuilder<T> or(Customizer<SpecificationBuilder<T>> builderCustomizer) {
        SpecificationBuilder<T> orBuilder = new SpecificationBuilder<>();
        builderCustomizer.customize(orBuilder);
        if (ObjectUtils.isEmpty(orBuilder.getExpressions())) {
            return this;
        }
        orBuilders.add(orBuilder);
        return this;
    }
}
