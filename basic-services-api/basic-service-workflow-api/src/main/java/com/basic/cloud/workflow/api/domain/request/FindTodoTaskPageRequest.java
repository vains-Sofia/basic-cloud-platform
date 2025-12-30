package com.basic.cloud.workflow.api.domain.request;

import com.basic.framework.core.domain.BasicPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询待办任务列表入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FindTodoTaskPageRequest extends BasicPageable {
}
