package com.basic.framework.core.constants;

/**
 * 这个类定义了HTTP协议中常用的HTTP状态码常量，方便在项目中直接引用。
 *
 * @author vains
 */
public class HttpCodeConstants {

    /**
     * 请求已成功处理，请求所希望的响应头或数据体将随此响应返回。
     */
    public static final int HTTP_OK = 200;

    /**
     * 已经创建了资源。主要用于创建RESTful API时的新资源创建操作。
     */
    public static final int HTTP_CREATED = 201;

    /**
     * 请求已被接受，但服务器尚未对其进行处理。这通常用于异步处理的场合。
     */
    public static final int HTTP_ACCEPTED = 202;

    /**
     * 服务器成功处理了请求，但没有返回任何内容。主要用于仅需响应头而不需响应体的场景。
     */
    public static final int HTTP_NO_CONTENT = 204;

    /**
     * 服务器已经完成了请求的处理，并且为了进一步执行用户代理（如浏览器）应重置当前已经浏览过的文档视图（例如：清除表单内容）。
     */
    public static final int HTTP_RESET_CONTENT = 205;

    /**
     * 资源未被修改，客户端仍然可以继续使用其缓存版本。
     */
    public static final int HTTP_NOT_MODIFIED = 304;

    /**
     * 服务器无法或不会处理该请求，因为语法错误。
     */
    public static final int HTTP_BAD_REQUEST = 400;

    /**
     * 客户端需要通过HTTP认证。
     */
    public static final int HTTP_UNAUTHORIZED = 401;

    /**
     * 服务器理解客户端的请求，但是拒绝执行此请求。
     */
    public static final int HTTP_FORBIDDEN = 403;

    /**
     * 客户端请求的资源在服务器上未找到。
     */
    public static final int HTTP_NOT_FOUND = 404;

    /**
     * 由于和先前的某个请求发生冲突，导致当前请求无法完成。
     */
    public static final int HTTP_CONFLICT = 409;

    /**
     * 所请求的资源已被永久删除，或者从未存在。
     */
    public static final int HTTP_GONE = 410;

    /**
     * 服务器拒绝处理请求，因为请求实体的Content-Length无效或缺失。
     */
    public static final int HTTP_LENGTH_REQUIRED = 411;

    /**
     * 客户端发送的先决条件请求失败。
     */
    public static final int HTTP_PRECONDITION_FAILED = 412;

    /**
     * 服务器在等待请求时超时。
     */
    public static final int HTTP_REQUEST_TIMEOUT = 408;

    /**
     * 服务器拒绝处理请求，因为请求实体太大。
     */
    public static final int HTTP_REQUEST_ENTITY_TOO_LARGE = 413;

    /**
     * 服务器拒绝处理请求，因为URI太长。
     */
    public static final int HTTP_REQUEST_URI_TOO_LONG = 414;

    /**
     * 服务器不支持请求中所包含的媒体类型。
     */
    public static final int HTTP_UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * 如果包含Range请求头的范围不满足，则服务器会返回此响应代码。
     */
    public static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    /**
     * 服务器未能满足Expect请求头信息的要求。
     */
    public static final int HTTP_EXPECTATION_FAILED = 417;

    /**
     * 服务器遇到了一个意外情况，无法完成请求。
     */
    public static final int HTTP_INTERNAL_ERROR = 500;

    /**
     * 服务器不支持请求的功能，无法完成请求。
     */
    public static final int HTTP_NOT_IMPLEMENTED = 501;

    /**
     * 作为网关或代理工作的服务器尝试执行请求时，从上游服务器接收到无效的响应。
     */
    public static final int HTTP_BAD_GATEWAY = 502;

    /**
     * 服务器暂时无法处理请求（由于超载或进行维护）。
     */
    public static final int HTTP_UNAVAILABLE = 503;

    /**
     * 作为网关或代理工作的服务器在与上游服务器通信时，请求超时。
     */
    public static final int HTTP_GATEWAY_TIMEOUT = 504;

    /**
     * 服务器不支持请求中所使用的HTTP协议版本。
     */
    public static final int HTTP_HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * 存储空间不足，服务器无法存储完成请求所必须的内容。
     */
    public static final int HTTP_INSUFFICIENT_STORAGE = 507;

    /**
     * 服务器遇到内部配置错误，无法完成变体协商。
     */
    public static final int HTTP_VARIANT_ALSO_NEGOTIATES = 507;
}