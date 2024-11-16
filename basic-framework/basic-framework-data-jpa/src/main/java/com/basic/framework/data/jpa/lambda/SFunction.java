package com.basic.framework.data.jpa.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 支持序列化的 Function
 *
 * @author vains
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
