package com.basic.framework.oauth2.authorization.server.mixin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * 邮件认证token反序列化mixin
 *
 * @author vains
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = QrCodeLoginAuthenticationTokenDeserializer.class)
public abstract class QrCodeLoginAuthenticationTokenMixin {
}
