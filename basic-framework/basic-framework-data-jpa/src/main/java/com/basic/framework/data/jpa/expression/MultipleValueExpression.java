package com.basic.framework.data.jpa.expression;

import com.basic.framework.data.jpa.enums.SqlKeywordEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

/**
 * 多值表达式
 *
 * @author vains
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class MultipleValueExpression extends SimpleExpression {

    /**
     * 值
     */
    private Collection<?> values;

    /**
     * 子类必须提供该构造器以初始化列名、值和SQL关键字
     *
     * @param property 列名
     * @param keyword  SQL关键字
     * @param values   值
     */
    public MultipleValueExpression(String property, SqlKeywordEnum keyword, Collection<?> values) {
        super(property, null, keyword);
        this.values = values;
    }
}
