package com.basic.cloud.workflow.util;

import org.flowable.common.engine.api.query.Query;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 适用于 Flowable 内置查询的条件构造器
 *
 * @param query 查询对象，e.g. ProcessInstanceQuery、DeploymentQuery、TaskQuery
 * @param <Q>   query 具体的类型是什么
 * @see Query
 * @author vains
 */
public record QueryBuilder<Q extends Query<?, ?>>(Q query) {

    public static <Q extends Query<?, ?>> QueryBuilder<Q> of(Q query) {
        return new QueryBuilder<>(query);
    }

    // =====================
    //       LIKE
    // =====================
    public QueryBuilder<Q> like(Consumer<String> likeFunc, String value) {
        if (value != null && !value.isBlank()) {
            likeFunc.accept("%" + value.trim() + "%");
        }
        return this;
    }

    // =====================
    //       EQUALS
    // =====================
    public <V> QueryBuilder<Q> eq(Consumer<V> eqFunc, V value) {
        if (value != null) {
            eqFunc.accept(value);
        }
        return this;
    }

    // =====================
    //       BETWEEN
    // =====================
    public <V> QueryBuilder<Q> between(Consumer<V> startFunc, Consumer<V> endFunc, V start, V end) {
        if (start != null) {
            startFunc.accept(start);
        }
        if (end != null) {
            endFunc.accept(end);
        }
        return this;
    }

    // =====================
    //        IN
    // =====================
    public <V> QueryBuilder<Q> in(BiConsumer<Q, V> inFunc, Collection<V> values) {
        if (values != null && !values.isEmpty()) {
            for (V v : values) {
                inFunc.accept(query, v);
            }
        }
        return this;
    }

    // =====================
    //     自定义条件
    // =====================
    public QueryBuilder<Q> apply(Consumer<Q> func) {
        func.accept(query);
        return this;
    }

    // =====================
    //      构造输出
    // =====================
    public Q build() {
        return query;
    }
}
