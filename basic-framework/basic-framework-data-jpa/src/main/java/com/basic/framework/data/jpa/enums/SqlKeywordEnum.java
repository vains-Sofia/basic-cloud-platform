package com.basic.framework.data.jpa.enums;

import com.basic.framework.core.enums.BasicEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * SQL 关键字枚举
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum SqlKeywordEnum implements BasicEnum<String, SqlKeywordEnum> {

    /**
     * 等于
     */
    EQ("=", "等于") {
        @Override
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.equal(root.get(property), value);
        }
    },

    /**
     * 不等于
     */
    NE("<>", "不等于") {
        @Override
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.notEqual(root.get(property), value);
        }
    },

    /**
     * 模糊查询
     */
    LIKE("LIKE", "模糊匹配") {
        @Override
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.like(root.get(property), "%" + value + "%");
        }
    },

    /**
     * 大于
     */
    GT(">", "大于") {
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.greaterThan(root.get(property), (Comparable) value);
        }
    },

    /**
     * 大于等于
     */
    GE(">=", "大于等于") {
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.greaterThanOrEqualTo(root.get(property), (Comparable) value);
        }
    },

    /**
     * 小于
     */
    LT("<", "小于") {
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.lessThan(root.get(property), (Comparable) value);
        }
    },

    /**
     * 小于等于
     */
    LE("<=", "小于等于") {
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.lessThanOrEqualTo(root.get(property), (Comparable) value);
        }
    },

    /**
     * 是 null
     */
    IS_NULL("IS NULL", "是 null") {
        @Override
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.isNull(root.get(property));
        }
    },

    /**
     * 不是 null
     */
    IS_NOT_NULL("IS NOT NULL", "不是 null") {
        @Override
        public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
            return cb.isNotNull(root.get(property));
        }
    },

    /**
     * 在...与...之间
     */
    BETWEEN("BETWEEN AND", "在...与...之间") {
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Predicate toPredicate(String property, Serializable value1, Serializable value2, Root<?> root, CriteriaBuilder cb) {
            return cb.between(root.get(property), (Comparable) value1, (Comparable) value2);
        }
    },

    /**
     * 不在...与...之间
     */
    NOT_BETWEEN("NOT BETWEEN AND", "在...与...之间") {
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Predicate toPredicate(String property, Serializable value1, Serializable value2, Root<?> root, CriteriaBuilder cb) {
            return cb.not(cb.between(root.get(property), (Comparable) value1, (Comparable) value2));
        }
    },

    /**
     * 批量操作
     */
    IN("IN", "批量操作") {
        @Override
        public Predicate toPredicate(String property, Collection<?> values, Root<?> root) {
            return root.get(property).in(values);
        }
    },

    /**
     * 批量操作
     */
    NOT_IN("NOT IN", "批量操作") {
        @Override
        public Predicate toPredicate(String property, Collection<?> values, Root<?> root) {
            return root.get(property).in(values).not();
        }
    };

    /**
     * SQL关键字
     */
    private final String value;

    /**
     * SQL 关键字描述
     */
    private final String description;

    public Predicate toPredicate(String property, Serializable value, Root<?> root, CriteriaBuilder cb) {
        throw new UnsupportedOperationException("该关键字不支持该操作！");
    }

    public Predicate toPredicate(String property, Serializable value1, Serializable value2, Root<?> root, CriteriaBuilder cb) {
        throw new UnsupportedOperationException("该关键字不支持该操作！");
    }

    public Predicate toPredicate(String property, Collection<?> values, Root<?> root) {
        throw new UnsupportedOperationException("该关键字不支持该操作！");
    }

}
