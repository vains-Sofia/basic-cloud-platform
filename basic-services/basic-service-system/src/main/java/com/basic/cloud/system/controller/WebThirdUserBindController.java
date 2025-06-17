package com.basic.cloud.system.controller;

import com.basic.cloud.system.domain.model.ConfirmSuccessTemplate;
import com.basic.cloud.system.service.SysThirdUserBindService;
import com.basic.framework.core.util.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 三方用户绑定接口-页面
 *
 * @author vains
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/third/user")
public class WebThirdUserBindController {

    private final SysThirdUserBindService thirdUserBindService;

    /**
     * 确认绑定
     *
     * @param confirmToken 确认绑定token
     * @return 结果
     */
    @GetMapping("/confirm")
    @Operation(summary = "三方登录用户确认绑定本地账号", description = "确认绑定后会跳转到绑定成功页面")
    public String confirm(String confirmToken, Model model) {
        ConfirmSuccessTemplate confirm = thirdUserBindService.confirm(confirmToken);
        // TODO 绑定失败处理

        // 将确认成功信息转换为Map并添加到模型中
        Map<String, Object> confirmDataMap = JsonUtils.objectToObject(confirm, Map.class, String.class, Object.class);
        model.addAllAttributes(confirmDataMap);
        return "confirm-success";
    }

}
