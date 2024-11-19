package com.basic.framework.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 适用于Spring Data的分页入参bean
 *
 * @author vains
 */
@Schema(title = "分页请求入参")
public abstract class DataPageable extends Pageable {

    @Override
    public Long getCurrent() {
        // Spring data中分页默认是从0开始，与前端分页从1开始的习惯不符，适配一下
        return super.getCurrent() - 1;
    }

    /**
     * 获取当前页码(适配 Spring Data)
     *
     * @return 当前页码
     */
    public int current() {
        return this.getCurrent().intValue();
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
