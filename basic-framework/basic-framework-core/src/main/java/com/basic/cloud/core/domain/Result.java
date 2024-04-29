package com.basic.cloud.core.domain;

import com.basic.cloud.core.constants.HttpCodeConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应Bean
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(name = "统一响应类", description = "统一响应类")
public class Result<T> implements Serializable {

    /**
     * 响应数据
     */
    @Schema(description = "接口响应数据")
    private T data;

    /**
     * 状态码
     */
    @Schema(description = "接口状态码")
    private Integer code;

    /**
     * 响应信息
     */
    @Schema(description = "接口响应信息")
    private String message;

    /**
     * 当前时间戳
     */
    @Schema(description = "接口响应时间戳")
    private Long timestamp;

    /**
     * 生成一个统一响应实例
     *
     * @param data    响应数据
     * @param code    状态码
     * @param message 响应信息
     * @param <T>     响应数据类型
     * @return 统一响应实例
     */
    public static <T> Result<T> of(T data, Integer code, String message) {
        return new Result<>(data, code, message, System.currentTimeMillis());
    }

    /**
     * 默认响应成功实例
     *
     * @return 统一响应实例
     */
    public static Result<String> success() {
        return Result.of(null, HttpCodeConstants.HTTP_OK, "操作成功.");
    }

    /**
     * 默认响应成功实例
     *
     * @param data 响应数据
     * @param <T>  响应数据data的类型
     * @return 统一响应实例
     */
    public static <T> Result<T> success(T data) {
        return Result.of(data, HttpCodeConstants.HTTP_OK, "操作成功.");
    }

    /**
     * 默认响应成功实例
     *
     * @param data    响应数据
     * @param message 响应信息
     * @param <T>     响应数据data的类型
     * @return 统一响应实例
     */
    public static <T> Result<T> success(T data, String message) {
        return Result.of(data, HttpCodeConstants.HTTP_OK, message);
    }

    /**
     * 默认响应成功实例
     *
     * @param data    响应数据
     * @param code    响应状态码
     * @param message 响应信息
     * @param <T>     响应数据data的类型
     * @return 统一响应实例
     */
    public static <T> Result<T> success(T data, Integer code, String message) {
        return Result.of(data, code, message);
    }

}
