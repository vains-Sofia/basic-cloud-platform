package com.basic.framework.data.jpa.expression;

import com.basic.framework.data.jpa.enums.SqlKeywordEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
    private Collection<Serializable> values;

    public MultipleValueExpression(String property, SqlKeywordEnum keyword, Collection<Serializable> values) {
        super(property, null, keyword);
        this.values = values;
    }
}
