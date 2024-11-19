package com.basic.framework.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 适用于Spring Data的分页数据响应
 *
 * @author vains
 */
@Schema(title = "分页请求出参")
public class DataPageResult<T> extends PageResult<T> {

    public DataPageResult(int current, int size, Long total, List<T> records) {
        super(((long) current + 1), (long) size, total, records);
    }

    /**
     * 根据分页对象构建响应对象
     *
     * @param current 当前页码
     * @param size    每页行数
     * @param total   总数据数量
     * @param records 分页后的具体数据
     * @param <T>     泛型，数据的类型
     * @return 公共分页响应bean
     */
    public static <T> DataPageResult<T> of(int current, int size, Long total, List<T> records) {
            return new DataPageResult<>(current, size, total, records);
    }

}
