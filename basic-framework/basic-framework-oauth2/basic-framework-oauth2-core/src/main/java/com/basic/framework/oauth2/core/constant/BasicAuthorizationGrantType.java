package com.basic.framework.oauth2.core.constant;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * 额外添加资源所有者密码凭据和资源所有者短信凭据的类型。
 * 授权授予是一种凭据，表示资源所有者对客户端的授权（以访问其受保护的资源），并由客户端用于获取访问令牌。
 * OAuth 2.0 授权框架定义了四种标准授权类型：授权代码和客户端凭据。它还提供了用于定义其他授权类型的扩展性机制。
 *
 * @author vains
 */
public class BasicAuthorizationGrantType {

    /**
     * 邮件模式
     */
    public static final AuthorizationGrantType EMAIL = new AuthorizationGrantType("email");

    /**
     * 密码模式
     */
    public static final AuthorizationGrantType PASSWORD = new AuthorizationGrantType("password");

}
