package com.basic.framework.oauth2.authorization.server.mixin;

import com.basic.framework.oauth2.authorization.server.email.EmailCaptchaLoginAuthenticationToken;
import com.basic.framework.oauth2.authorization.server.qrcode.QrCodeLoginAuthenticationToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 注册模块中封装的mixin
 *
 * @author vains
 */
public class BasicAuthorizationServerJackson2Module extends SimpleModule {

    public BasicAuthorizationServerJackson2Module() {
        super(BasicAuthorizationServerJackson2Module.class.getName(), new Version(0, 0, 1, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(QrCodeLoginAuthenticationToken.class, QrCodeLoginAuthenticationTokenMixin.class);
        context.setMixInAnnotations(EmailCaptchaLoginAuthenticationToken.class, EmailCaptchaLoginAuthenticationTokenMixin.class);
    }
}
