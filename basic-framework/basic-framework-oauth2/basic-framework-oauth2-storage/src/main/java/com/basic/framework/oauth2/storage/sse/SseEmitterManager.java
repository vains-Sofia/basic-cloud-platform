package com.basic.framework.oauth2.storage.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SseEmitter 管理器，用于管理和发送 Server-Sent Events (SSE)
 * <p>
 * 该类提供了添加、获取、移除 SseEmitter 的方法，并支持向特定用户发送事件。
 * 使用 ConcurrentHashMap 存储 SseEmitter 实例，确保线程安全。
 *
 * @author vains
 */
public class SseEmitterManager {

    /**
     * 存储 SseEmitter 的映射，键为用户代码，值为对应的 SseEmitter 实例
     * Map<user_code, SseEmitter>
     */
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    /**
     * 添加一个新的 SseEmitter
     *
     * @param userCode      用户代码，用于标识 SseEmitter
     * @param timeoutMillis 超时时间，单位毫秒
     * @return 新创建的 SseEmitter 实例
     */
    public SseEmitter addEmitter(String userCode, long timeoutMillis) {
        SseEmitter emitter = new SseEmitter(timeoutMillis);
        this.emitterMap.put(userCode, emitter);

        emitter.onCompletion(() -> this.removeEmitter(userCode));
        emitter.onTimeout(() -> this.removeEmitter(userCode));
        emitter.onError((ex) -> this.removeEmitter(userCode));

        emitter.onTimeout(() -> {
            emitter.complete();
            emitterMap.remove(userCode);
        });

        try {
            // 初始化时设置重连间隔为 100 毫秒
            emitter.send(SseEmitter.event()
                    .comment("init")
                    // 设置事件的重连时间为 100 毫秒
                    .reconnectTime(100));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    /**
     * 获取指定用户代码的 SseEmitter
     *
     * @param userCode 用户代码
     * @return 对应的 SseEmitter 实例，如果不存在则返回 null
     */
    public SseEmitter getEmitter(String userCode) {
        return this.emitterMap.get(userCode);
    }

    /**
     * 移除指定用户代码的 SseEmitter
     *
     * @param userCode 用户代码
     */
    public void removeEmitter(String userCode) {
        this.emitterMap.remove(userCode);
    }

    /**
     * 发送消息到指定用户的 SseEmitter
     *
     * @param userCode  用户代码
     * @param eventName 事件名称
     * @param data      发送的数据
     * @return 如果发送成功返回 true，否则返回 false
     */
    public boolean sendMessage(String userCode, String eventName, Object data) {
        SseEmitter emitter = this.getEmitter(userCode);
        if (emitter == null) {
            return false;
        }
        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data)
            );
            return true;
        } catch (IOException e) {
            emitter.completeWithError(e);
            this.removeEmitter(userCode);
            return false;
        }
    }
}