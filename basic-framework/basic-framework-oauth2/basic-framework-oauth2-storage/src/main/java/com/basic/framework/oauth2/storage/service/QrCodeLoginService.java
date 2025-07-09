package com.basic.framework.oauth2.storage.service;

import com.basic.framework.oauth2.storage.domain.request.QrCodeConfirmRequest;
import com.basic.framework.oauth2.storage.domain.request.QrCodeScanRequest;
import com.basic.framework.oauth2.storage.domain.response.QrCodeScanResponse;
import com.basic.framework.oauth2.storage.domain.response.QrInitResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 二维码登录 Service 接口
 *
 * @author vains
 */
public interface QrCodeLoginService {

    /**
     * 生成二维码
     *
     * @return QrInitResponse 包含二维码的初始化信息
     */
    QrInitResponse generateQrCode();

    /**
     * 轮询二维码状态
     *
     * @param token 二维码唯一标识
     * @return SseEmitter 实时响应二维码状态
     */
    SseEmitter poll(String token);

    /**
     * 移动端扫码
     *
     * @param request 二维码扫码请求
     * @return QrCodeScanResponse 包含扫码结果
     */
    QrCodeScanResponse scan(@Validated QrCodeScanRequest request);

    /**
     * 移动端确认登录
     *
     * @param request 二维码确认请求
     */
    void confirm(@Validated QrCodeConfirmRequest request);

}
