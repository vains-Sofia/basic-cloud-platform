package com.basic.framework.oauth2.storage.core.domain;

import com.basic.framework.oauth2.storage.core.enums.TimeToLiveUnitEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

/**
 * 客户端对应的token生成设置
 *
 * @author vains
 */
@Data
@Schema(title = "客户端对应的token生成设置")
public class OAuth2TokenSettings {

    /**
     * 授权码有效时长
     */
    @Schema(title = "授权码有效时长")
    private Long authorizationCodeTimeToLive;

    /**
     * 授权码有效时长的单位
     */
    @Schema(title = "授权码有效时长的单位")
    private TimeToLiveUnitEnum authorizationCodeTimeToLiveUnit;

    /**
     * access token有效时长
     */
    @Schema(title = "access token有效时长")
    private Long accessTokenTimeToLive;

    /**
     * access token有效时长的单位
     */
    @Schema(title = "access token有效时长的单位")
    private TimeToLiveUnitEnum accessTokenTimeToLiveUnit;

    /**
     * 设置access token的格式，
     * Jwt token ：{@link OAuth2TokenFormat#SELF_CONTAINED}
     * Opaque token ：{@link OAuth2TokenFormat#REFERENCE}
     */
    @Schema(description = """
                设置access token的格式，
                Jwt token ：OAuth2TokenFormat#SELF_CONTAINED
                Opaque token ：OAuth2TokenFormat#REFERENCE
            """)
    private String accessTokenFormat;

    /**
     * 设备码有效时长
     */
    @Schema(title = "设备码有效时长")
    private Long deviceCodeTimeToLive;

    /**
     * 设备码有效时长的单位
     */
    @Schema(title = "设备码有效时长的单位")
    private TimeToLiveUnitEnum deviceCodeTimeToLiveUnit;

    /**
     * 设置refresh token是否可重复使用
     */
    @Schema(title = "设置refresh token是否可重复使用")
    private Boolean reuseRefreshTokens;

    /**
     * refresh token有效时长
     */
    @Schema(title = "refresh token有效时长")
    private Long refreshTokenTimeToLive;

    /**
     * refresh token有效时长的单位
     */
    @Schema(title = "refresh token有效时长的单位")
    private TimeToLiveUnitEnum refreshTokenTimeToLiveUnit;

    /**
     * 对ID Token进行签名的JWS算法。
     */
    @Schema(title = "对ID Token进行签名的JWS算法。")
    private String idTokenSignatureAlgorithm;

    /**
     * 如果访问令牌必须绑定到客户端x509使用tls_client_auth或self_signed_tls_client_auth方法进行客户端身份验证期间接收的证书，则设置为true。
     */
    @Schema(description = """
                如果访问令牌必须绑定到客户端x509使用tls_client_auth或self_signed_tls_client_auth方法进行客户端身份验证期间接收的证书，则设置为true。
            """)
    private Boolean x509CertificateBoundAccessTokens;

}
