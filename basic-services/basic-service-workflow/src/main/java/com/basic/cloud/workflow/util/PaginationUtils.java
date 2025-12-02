package com.basic.cloud.workflow.util;

import lombok.experimental.UtilityClass;

/**
 * 流程分页工具类
 *
 * @author vains
 */
@UtilityClass
public class PaginationUtils {

    /**
     * 计算分页参数
     *
     * @param current  页码（从 1 开始）
     * @param pageSize 每页数量
     * @return PageParam
     */
    public static PageParam calc(Integer current, Integer pageSize) {
        // 默认值
        int safePageNum = (current == null || current < 1) ? 1 : current;
        int safePageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        // 限制最大分页大小，防止恶意拉超大列表
        int maxPageSize = 200;
        if (safePageSize > maxPageSize) {
            safePageSize = maxPageSize;
        }

        int firstResult = (safePageNum - 1) * safePageSize;

        return new PageParam(firstResult, safePageSize);
    }

    /**
     * 封装分页参数
     */
    public record PageParam(int firstResult, int maxResults) {
    }

}
