package com.basic.cloud.authorization.server.controller;

import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.storage.domain.request.QrCodeConfirmRequest;
import com.basic.framework.oauth2.storage.domain.request.QrCodeScanRequest;
import com.basic.framework.oauth2.storage.domain.response.QrCodeScanResponse;
import com.basic.framework.oauth2.storage.domain.response.QrInitResponse;
import com.basic.framework.oauth2.storage.service.QrCodeLoginService;
import com.basic.framework.redis.annotation.RedisLock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 二维码登录相关接口
 * PC浏览器会调用前两个接口生成二维码和轮询二维码状态，
 * 移动端App会调用后两个接口进行扫码和确认登录。
 * 后端收到二维码状态变化后，会通过SSE推送给前端，
 * PC接收到二维码状态变化后会更新二维码状态，并且调用
 * 二维码登录接口(自定义登录方式)进行登录，所以
 * 需要缓存用户的email。
 * * 1. 生成二维码：`/qr-code/init`
 * * 2. 轮询二维码状态：`/qr-code/poll`
 * * 3. 移动端扫码：`/qr-code/app/scan`
 * * 4. 移动端确认登录：`/qr-code/app/confirm`
 * * 5. 通过SSE推送二维码状态变化给前端
 * * 6. PC接收到二维码状态变化后，更新二维码状态
 * * 7. PC调用二维码登录接口进行登录
 * * 这些接口结合使用，实现了二维码登录的完整流程。
 *
 * @author vains
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/qr-code")
@Tag(name = "二维码登录接口", description = "二维码登录相关接口")
public class QrCodeAuthorizationController {

    private final QrCodeLoginService qrCodeLoginService;

    @GetMapping("/init")
    @Operation(summary = "生成二维码", description = "生成一个新的二维码")
    public Result<QrInitResponse> generateQrCode() {
        QrInitResponse initResponse = this.qrCodeLoginService.generateQrCode();
        return Result.success(initResponse);
    }

    @GetMapping(value = "/poll", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "轮询二维码状态", description = "用于轮询二维码的状态，返回 SSE 连接")
    public SseEmitter poll(String token) {

        return this.qrCodeLoginService.poll(token);
    }

    @RedisLock
    @PostMapping("/app/scan")
    @Operation(summary = "移动端扫码", description = "移动端App扫描二维码进行登录")
    public Result<QrCodeScanResponse> scan(@RequestBody @Validated QrCodeScanRequest request) {
        QrCodeScanResponse scanResponse = this.qrCodeLoginService.scan(request);
        return Result.success(scanResponse);
    }

    @RedisLock
    @PostMapping("/app/confirm")
    @Operation(summary = "移动端确认登录", description = "移动端App确认登录")
    public Result<String> confirm(@RequestBody @Validated QrCodeConfirmRequest request) {

        this.qrCodeLoginService.confirm(request);
        return Result.success();
    }

}
