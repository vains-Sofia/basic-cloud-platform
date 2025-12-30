package com.basic.cloud.workflow.api.domain.response;

import com.basic.cloud.workflow.api.enums.RedirectTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 前端重定向相关
 *
 * @author vains
 */
@Data
@AllArgsConstructor
@Schema(title = "RedirectInfo", description = "前端重定向相关")
public class RedirectInfo {

    /**
     * 重定向地址
     */
    private String url;

    /**
     * 重定向类型
     */
    private RedirectTypeEnum type;

    public static RedirectInfo none() {
        return new RedirectInfo(null, RedirectTypeEnum.NONE);
    }

    public static RedirectInfo taskForm(String taskId) {
        return new RedirectInfo("/process/task/" + taskId + "/form", RedirectTypeEnum.TASK_FORM);
    }

    public static RedirectInfo taskList() {
        return new RedirectInfo("/todo", RedirectTypeEnum.TASK_LIST);
    }
}
