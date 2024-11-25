package com.basic.framework.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 适用于Spring Data的分页入参bean
 *
 * @author vains
 */
@Data
@Schema(title = "分页请求入参")
@EqualsAndHashCode(callSuper = true)
public abstract class DataPageable extends BasicPageable {

    /**
     * 获取当前页码(适配 Spring Data)
     *
     * @return 当前页码
     */
    public int current() {
        return this.getCurrent().intValue() - 1;
    }

    /**
     * 获取每页行数(适配 Spring Data)
     *
     * @return 当前页码
     */
    public int size() {
        return this.getSize().intValue();
    }

}
