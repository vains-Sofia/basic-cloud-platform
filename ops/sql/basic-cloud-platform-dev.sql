SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_application
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_application`;
CREATE TABLE `oauth2_application`
(
    `id`                            bigint                                                  NOT NULL COMMENT '数据主键',
    `client_id`                     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '客户端id',
    `client_id_issued_at`           timestamp                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '客户端id签发时间',
    `client_secret`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '客户端秘钥',
    `client_secret_expires_at`      timestamp                                               NULL     DEFAULT NULL COMMENT '客户端秘钥过期时间',
    `client_name`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '客户端名称',
    `client_logo`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '客户端logo',
    `client_authentication_methods` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端支持的认证方式',
    `description`                   varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '客户端描述',
    `authorization_grant_types`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端支持的授权模式',
    `redirect_uris`                 varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '回调地址',
    `post_logout_redirect_uris`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT 'Open Connection登出后跳转地址',
    `scopes`                        varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端拥有的scope',
    `client_settings`               varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端设置',
    `token_settings`                varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端生成token设置',
    `create_by`                     bigint                                                  NULL     DEFAULT NULL COMMENT '创建人',
    `update_by`                     bigint                                                  NULL     DEFAULT NULL COMMENT '修改人',
    `create_time`                   datetime                                                NULL     DEFAULT NULL COMMENT '创建时间',
    `update_time`                   datetime                                                NULL     DEFAULT NULL COMMENT '修改时间',
    `create_name`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL     DEFAULT NULL COMMENT '创建人名称',
    `update_name`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL     DEFAULT NULL COMMENT '修改人名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_application
-- ----------------------------
INSERT INTO `oauth2_application`
VALUES (1849006457251749895, 'pkce-message-client', '2024-10-23 16:34:27', NULL, NULL, 'PKCE流程',
        'https://minio.cloudflow.top/user-picture/aaa.6e9bf775-9537-4b06-a026-637c16e6a32d.jpg', '[\"none\"]',
        '这是一个描述', '[\"refresh_token\",\"authorization_code\"]',
        '[\"http://127.0.0.1:5173/base/PkceRedirect\",\"https://authorization-example.vercel.app/PkceRedirect\",\"https://j1zr8ren8w.51xd.pub/PkceRedirect\",\"https://flow-cloud.love/PkceRedirect\"]',
        '[\"http://127.0.0.1:8080/getCaptcha\"]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":true,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":\"http://127.0.0.1:8000/jwkSet\",\"tokenEndpointAuthenticationSigningAlgorithm\":\"RS256\",\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, NULL, NULL, '2024-10-23 16:34:27', '2024-10-23 16:38:37');
INSERT INTO `oauth2_application`
VALUES (1849006886790406145, 'oidc-client', '2024-10-23 16:36:09',
        '{bcrypt}$2a$10$ueQ4/qoUpY/lScQLWnQDhe6057YrAFGPiNy/Fs1Xz6vR0Cx5YH8Gu', NULL, 'oidc-client',
        'https://minio.cloudflow.top/user-picture/aaa.6e9bf775-9537-4b06-a026-637c16e6a32d.jpg',
        '[\"client_secret_basic\"]', '这是一个描述',
        '[\"refresh_token\",\"password\",\"authorization_code\",\"email\"]',
        '[\"http://127.0.0.1:8080/login/oauth2/code/oidc-client\",\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"https://www.baidu.com\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, NULL, NULL, NULL, '2024-10-23 16:36:10', '2024-10-23 16:42:55');
INSERT INTO `oauth2_application`
VALUES (1849006886790406146, 'swagger-client', '2024-10-23 16:36:09',
        '{bcrypt}$2a$10$wHHrjvNBDsye/hLuRGOzrODni1b8VzgKmMPpfZQQqrFoscGlUgeI6', NULL, 'swagger-client',
        'https://minio.cloudflow.top/user-picture/aaa.6e9bf775-9537-4b06-a026-637c16e6a32d.jpg',
        '[\"client_secret_post\"]', '这是一个描述', '[\"refresh_token\",\"password\",\"authorization_code\",\"email\"]',
        '[\"http://127.0.0.1:8080/login/oauth2/code/oidc-client\",\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:8080/auth/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"http://127.0.0.1:9000/webjars/swagger-ui/oauth2-redirect.html\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, NULL, NULL, '2024-10-23 16:36:10', '2024-10-29 16:11:16');
INSERT INTO `oauth2_application`
VALUES (1849006886790406147, 'device-messaging-client', '2024-10-23 16:36:09', NULL, NULL, 'device-messaging-client',
        'https://minio.cloudflow.top/user-picture/aaa.6e9bf775-9537-4b06-a026-637c16e6a32d.jpg', '[\"none\"]',
        '这是一个描述', '[\"refresh_token\",\"urn:ietf:params:oauth:grant-type:device_code\"]', '[]', '[]',
        '[\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, NULL, NULL, '2024-10-23 16:36:10', '2024-10-23 16:38:37');
INSERT INTO `oauth2_application`
VALUES (1849006886790406148, 'messaging-client', '2024-10-23 16:36:09',
        '{bcrypt}$2a$10$2V.a.YAI1UIbyO1OGxjXoehW98xK5kig9L7CidXOorhyuEdSfigAq', NULL, 'messaging-client',
        'https://minio.cloudflow.top/user-picture/aaa.6e9bf775-9537-4b06-a026-637c16e6a32d.jpg',
        '[\"client_secret_basic\"]', '这是一个描述',
        '[\"refresh_token\",\"password\",\"authorization_code\",\"email\",\"client_credentials\"]',
        '[\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"http://172.20.10.3:8000/login/oauth2/code/client-oidc\",\"https://www.baidu.com\",\"http://127.0.0.1:9000/monitor/login/oauth2/code/messaging-client-oidc\",\"http://172.20.10.3:9000/monitor/login/oauth2/code/messaging-client-oidc\",\"http://172.20.10.4:9000/monitor/login/oauth2/code/messaging-client-oidc\",\"http://127.0.0.1:9000/monitor/login/oauth2/code/messaging-client-oidc\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, NULL, NULL, NULL, '2024-10-23 16:36:10', '2024-11-07 15:37:58');
INSERT INTO `oauth2_application`
VALUES (1849006886790406149, 'private-key-jwt-client', '2024-10-23 16:36:09', '{noop}12345678', NULL,
        'private-key-jwt-client',
        'https://minio.cloudflow.top/user-picture/aaa.6e9bf775-9537-4b06-a026-637c16e6a32d.jpg',
        '[\"private_key_jwt\",\"client_secret_basic\"]', '这是一个描述',
        '[\"refresh_token\",\"password\",\"authorization_code\",\"email\"]',
        '[\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:8000/login/oauth2/code/private-key-client-oidc\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"https://www.baidu.com\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":\"http://127.0.0.1:8000/jwkSet\",\"tokenEndpointAuthenticationSigningAlgorithm\":\"RS256\",\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, NULL, NULL, '2024-10-23 16:36:10', '2024-11-01 17:25:41');
INSERT INTO `oauth2_application`
VALUES (1849006886790406151, 'opaque-client', '2024-10-23 16:36:09',
        '{bcrypt}$2a$10$mSLgt/W4rLvMpQuxbagp7erwLQ83lKQf9AhibyllqijiaJ5kNl0RS', NULL, '匿名token',
        'https://minio.cloudflow.top/user-picture/aaa.6e9bf775-9537-4b06-a026-637c16e6a32d.jpg',
        '[\"client_secret_basic\"]', '这是一个描述',
        '[\"refresh_token\",\"client_credentials\",\"authorization_code\"]',
        '[\"https://flow-cloud.love/OAuth2Redirect\",\"https://authorization-example.vercel.app/OAuth2Redirect\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"https://j1zr8ren8w.51xd.pub/OAuth2Redirect\"]',
        '[]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"reference\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, NULL, NULL, NULL, '2024-10-23 16:36:10', '2024-11-22 09:40:29');

-- ----------------------------
-- Table structure for oauth2_authorization
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization`
(
    `id`                            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NOT NULL COMMENT '数据id',
    `registered_client_id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT 'oauth2授权登录时使用的客户端id',
    `principal_id`                  bigint                                                  NULL DEFAULT NULL COMMENT 'oauth2授权登录时授权用户id',
    `principal_name`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT 'oauth2授权登录时授权用户名',
    `authorization_grant_type`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT 'oauth2授权登录时使用的授权模式',
    `authorized_scopes`             varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'oauth2授权登录时申请的权限',
    `attributes`                    text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'oauth2授权登录属性',
    `state`                         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT 'oauth2授权登录时的state',
    `authorization_code_value`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT '授权码的值',
    `authorization_code_issued_at`  timestamp                                               NULL DEFAULT NULL COMMENT '授权码签发时间',
    `authorization_code_expires_at` timestamp                                               NULL DEFAULT NULL COMMENT '授权码过期时间',
    `authorization_code_metadata`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT '授权码元数据',
    `access_token_value`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'access token的值',
    `access_token_issued_at`        timestamp                                               NULL DEFAULT NULL COMMENT 'access token签发时间',
    `access_token_expires_at`       timestamp                                               NULL DEFAULT NULL COMMENT 'access token过期时间',
    `access_token_metadata`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'access token元数据',
    `access_token_type`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT 'access token类型(一般是bearer)',
    `access_token_scopes`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'access token中包含的scope',
    `refresh_token_value`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT '认证后签发的 refresh token',
    `refresh_token_issued_at`       timestamp                                               NULL DEFAULT NULL COMMENT 'refresh token 签发时间',
    `refresh_token_expires_at`      timestamp                                               NULL DEFAULT NULL COMMENT 'refresh token 过期时间',
    `refresh_token_metadata`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'refresh token 元数据',
    `oidc_id_token_value`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT '认证后签发的 oidc id token',
    `oidc_id_token_issued_at`       timestamp                                               NULL DEFAULT NULL COMMENT 'oidc id token 签发时间',
    `oidc_id_token_expires_at`      timestamp                                               NULL DEFAULT NULL COMMENT 'oidc id token 过期时间',
    `oidc_id_token_metadata`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'oidc id token 元数据',
    `oidc_id_token_claims`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'oidc id token 声明(Claims)信息(一般情况下是用户数据)',
    `user_code_value`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT '设备码模式(Device Flow)中的 user code',
    `user_code_issued_at`           timestamp                                               NULL DEFAULT NULL COMMENT 'user code 签发时间',
    `user_code_expires_at`          timestamp                                               NULL DEFAULT NULL COMMENT 'user code 过期时间',
    `user_code_metadata`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'user code 元数据',
    `device_code_value`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT '设备码模式(Device Flow)中的 device code',
    `device_code_issued_at`         timestamp                                               NULL DEFAULT NULL COMMENT 'device code 签发时间',
    `device_code_expires_at`        timestamp                                               NULL DEFAULT NULL COMMENT 'device code 过期时间',
    `device_code_metadata`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL COMMENT 'device code 元数据',
    `create_time`                   datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`                   datetime                                                NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for oauth2_authorization_consent
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent`
(
    `id`                   bigint                                                  NOT NULL COMMENT '主键',
    `registered_client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '针对该客户端id对应的客户端的授权确认',
    `principal_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '授权确认的用户',
    `authorities`          varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '授权确认的scope',
    `create_by`            bigint                                                  NULL DEFAULT NULL COMMENT '创建人',
    `update_by`            bigint                                                  NULL DEFAULT NULL COMMENT '修改人',
    `create_time`          datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`          datetime                                                NULL DEFAULT NULL COMMENT '修改时间',
    `create_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '修改人名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for oauth2_scope
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_scope`;
CREATE TABLE `oauth2_scope`
(
    `id`          bigint                                                 NOT NULL COMMENT '主键id',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT 'scope 名称',
    `scope`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'scope 编码',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'scope 描述',
    `enabled`     tinyint(1)                                             NULL DEFAULT NULL COMMENT '是否启用',
    `create_by`   bigint                                                 NULL DEFAULT NULL COMMENT '创建人',
    `update_by`   bigint                                                 NULL DEFAULT NULL COMMENT '修改人',
    `create_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '修改人名称',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'oauth2客户端的scope'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_scope
-- ----------------------------
INSERT INTO `oauth2_scope`
VALUES (1856524382825115650, '用户信息详情', 'profile',
        'This application will be able to read your profile information.', 1, 123, 1, NULL, '云逸',
        '2024-11-13 10:28:00', '2025-05-29 17:58:27');
INSERT INTO `oauth2_scope`
VALUES (1856525468709421058, '读取数据范围', 'message.read', 'This application will be able to read your message.', 1,
        123, 1, NULL, '云逸', '2024-11-13 10:32:19', '2025-05-29 17:58:11');
INSERT INTO `oauth2_scope`
VALUES (1856525597751377922, '修改数据范围', 'message.write',
        'This application will be able to add new messages. It will also be able to edit and delete existing messages.',
        1, 123, 1, NULL, '云逸', '2024-11-13 10:32:50', '2025-05-29 17:58:17');
INSERT INTO `oauth2_scope`
VALUES (1856525703913406465, '查询用户范围', 'user.read',
        'This application will be able to read your user information.', 1, 123, 1, NULL, '云逸', '2024-11-13 10:33:15',
        '2025-05-29 17:58:04');
INSERT INTO `oauth2_scope`
VALUES (1856578472980570113, 'OpenID Connect', 'openid',
        'The openid scope is required for OpenID Connect Authentication Requests1111.', 1, NULL, 1, NULL, '云逸',
        '2024-11-13 10:33:15', '2025-05-29 17:53:54');
INSERT INTO `oauth2_scope`
VALUES (1856578945196253186, 'Email权限', 'email',
        'The email scope requests access to the email and email_verified claims.', 1, 123, 1, NULL, '云逸',
        '2024-11-13 14:04:49', '2025-05-29 17:57:41');
INSERT INTO `oauth2_scope`
VALUES (1928029770206277634, '测试添加', 'test', '测试添加', 1, 1, 1, '云逸', '云逸', '2025-05-29 18:04:53',
        '2025-05-29 18:04:53');

-- ----------------------------
-- Table structure for oauth2_scope_permission
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_scope_permission`;
CREATE TABLE `oauth2_scope_permission`
(
    `id`            bigint                                                 NOT NULL COMMENT '主键id',
    `scope`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'oauth2 scope名',
    `permission_id` bigint                                                 NULL DEFAULT NULL COMMENT '权限id',
    `create_by`     bigint                                                 NULL DEFAULT NULL COMMENT '创建人',
    `update_by`     bigint                                                 NULL DEFAULT NULL COMMENT '修改人',
    `create_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '修改人名称',
    `create_time`   datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                               NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'oauth2 scope与权限关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth2_scope_permission
-- ----------------------------
INSERT INTO `oauth2_scope_permission`
VALUES (1870114121892024321, 'message.read', 1870104848520814594, 1, 1, '云逸', '云逸', '2024-12-20 22:28:46',
        '2024-12-20 22:28:46');

-- ----------------------------
-- Table structure for sys_basic_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_basic_user`;
CREATE TABLE `sys_basic_user`
(
    `id`                    bigint                                                 NOT NULL COMMENT '自增id',
    `username`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '账号',
    `nickname`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名、昵称',
    `profile`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户个人资料页面的 URL。',
    `picture`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户个人资料图片的 URL。此 URL 必须指向图像文件（例如，PNG、JPEG 或 GIF 图像文件），而不是指向包含图像的网页。',
    `email`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '用户的首选电子邮件地址。其值必须符合RFC 5322 [RFC5322] addr-spec 语法',
    `email_verified`        tinyint(1)                                             NULL DEFAULT NULL COMMENT '邮箱是否验证过',
    `gender`                tinyint(1)                                             NULL DEFAULT NULL COMMENT '用户性别',
    `password`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
    `birthdate`             date                                                   NULL DEFAULT NULL COMMENT '出生日期，以 ISO 8601-1 [ISO8601‑1] YYYY-MM-DD 格式表示。',
    `phone_number`          varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '手机号',
    `phone_number_verified` tinyint(1)                                             NULL DEFAULT NULL COMMENT '手机号是否已验证',
    `address`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户的首选邮政地址',
    `deleted`               tinyint(1)                                             NULL DEFAULT NULL COMMENT '是否已删除',
    `account_platform`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户来源',
    `create_by`             bigint                                                 NULL DEFAULT NULL COMMENT '创建人',
    `update_by`             bigint                                                 NULL DEFAULT NULL COMMENT '修改人',
    `create_name`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '修改人名称',
    `create_time`           datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`           datetime                                               NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '基础用户信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_basic_user
-- ----------------------------
INSERT INTO sys_basic_user (id, username, nickname, profile, picture, email, email_verified, gender, password,
                            birthdate, phone_number, phone_number_verified, address, deleted, account_platform,
                            create_by, update_by, create_name, update_name, create_time, update_time)
VALUES (1, 'admin', '云逸', NULL,
        'https://minio.cloudflow.top/user-picture/aaa.6e9bf775-9537-4b06-a026-637c16e6a32d.jpg', '17683906991@163.com',
        0, 1, '{bcrypt}$2a$10$PSkQfdN7.MDZ4/8hh2gJleQ4hnl.eppYr7zkU0ZCqR6u9GbaYg0zy', '2024-10-01', '17683906991', 1,
        '地址', 0, 'system', 1, 1, 'system', '云逸', '2024-11-22 23:43:47', '2025-05-16 14:03:14');
INSERT INTO sys_basic_user (id, username, nickname, profile, picture, email, email_verified, gender, password,
                            birthdate, phone_number, phone_number_verified, address, deleted, account_platform,
                            create_by, update_by, create_name, update_name, create_time, update_time)
VALUES (1862332106783637506, 'test01', '注册01', NULL,
        'https://minio.cloudflow.top/user-picture/smileboy.56cdf42f-b42c-4f00-b24e-1caf796ecd5f.png',
        '17683906001@163.com', 0, 1, '{bcrypt}$2a$10$MbAbp69nydLks4BNSV7jsOHwsmrJkSiIB/mYBE7WXdYBnES0LG6v6',
        '2010-05-08', '17683906001', 1, '111111', 0, 'system', 1862332106783637506, 1, '注册01', '云逸',
        '2024-11-29 11:05:49', '2025-05-29 16:41:22');
INSERT INTO sys_basic_user (id, username, nickname, profile, picture, email, email_verified, gender, password,
                            birthdate, phone_number, phone_number_verified, address, deleted, account_platform,
                            create_by, update_by, create_name, update_name, create_time, update_time)
VALUES (1920404587435859970, 'test02', '测试添加', NULL,
        'https://minio.cloudflow.top/user-picture/blue-girl.2e947cf1-4e73-4f29-a6cf-534b70c97b3f.png',
        '17683906002@163.com', 0, 2, '{bcrypt}$2a$10$RHRRWM73YQI0iYARMakJ9OvHfL/F2LvDpBUzBCZaaKUtQTJcsEflC',
        '2000-05-08', '17683906002', 1, '2222222221', 0, 'system', 1, 1, '云逸', '云逸', '2025-05-08 17:05:08',
        '2025-07-25 10:31:16');
INSERT INTO sys_basic_user (id, username, nickname, profile, picture, email, email_verified, gender, password,
                            birthdate, phone_number, phone_number_verified, address, deleted, account_platform,
                            create_by, update_by, create_name, update_name, create_time, update_time)
VALUES (1934450355763220481, 'cxw', 'ChenXw', NULL,
        'https://minio.cloudflow.top/user-picture/draw-girl.4a3caa10-60c9-4c49-9423-17c2e1a2ad81.png',
        '513087451@qq.com', 0, 2, '{bcrypt}$2a$10$A9etrG44XVVTy97WqZp7k.f9pW2uXllycwusTwF./ZPvCZCtvycbG', '2025-07-03',
        '', 0, '', 0, 'system', 1934450355763220481, 1, '嗨嗨嗨', '云逸', '2025-06-16 11:18:00', '2025-07-25 10:31:10');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`                  bigint                                                  NOT NULL COMMENT '主键id',
    `title`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的locales文件夹下对应添加）',
    `name`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '权限名',
    `permission`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '权限码',
    `path`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '路径',
    `request_method`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT 'HTTP请求方式',
    `permission_type`     tinyint(1)                                              NOT NULL COMMENT '0:菜单,1:接口,2:其它',
    `module_name`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '所属模块名字',
    `description`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '描述',
    `need_authentication` tinyint(1)                                              NULL DEFAULT 0 COMMENT '是否需要鉴权',
    `parent_id`           bigint                                                  NULL DEFAULT 0 COMMENT '父节点id',
    `component`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '组件路径',
    `redirect`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '路由重定向',
    `deleted`             tinyint(1)                                              NULL DEFAULT NULL COMMENT '是否已删除',
    `icon`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '菜单图标',
    `extra_icon`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '右侧图标',
    `enter_transition`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '进场动画（页面加载动画）',
    `leave_transition`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '离场动画（页面加载动画）',
    `frame_src`           varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '链接地址（需要内嵌的iframe链接地址）',
    `frame_loading`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '加载动画（内嵌的iframe页面是否开启首次加载动画）',
    `keep_alive`          tinyint(1)                                              NULL DEFAULT NULL COMMENT '缓存页面（是否缓存该路由页面，开启后会保存该页面的整体状态，刷新后会清空状态）',
    `show_link`           tinyint(1)                                              NULL DEFAULT NULL COMMENT '是否显示该菜单',
    `hidden_tag`          tinyint(1)                                              NULL DEFAULT NULL COMMENT '隐藏标签页（当前菜单名称或自定义信息禁止添加到标签页）',
    `fixed_tag`           tinyint(1)                                              NULL DEFAULT NULL COMMENT '固定标签页（当前菜单名称是否固定显示在标签页且不可关闭）',
    `show_parent`         tinyint(1)                                              NULL DEFAULT NULL COMMENT '是否显示父级菜单',
    `rank`                decimal(10, 0)                                          NULL DEFAULT NULL COMMENT '菜单排序',
    `create_by`           bigint                                                  NULL DEFAULT NULL COMMENT '创建人',
    `update_by`           bigint                                                  NULL DEFAULT NULL COMMENT '修改人',
    `create_name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '修改人名称',
    `create_time`         datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime                                                NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'RBAC权限表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1864241504623833089, '修改权限信息', '修改权限信息', 'permission:updatePermission',
        '/permission/updatePermission', 'PUT', 3, NULL, NULL, 1, 1919661224031989762, '', '', 0, 'ep:bell', 'ep:bell',
        '', '', '', '', 0, 1, 0, 0, 0, 9, 1, 1, '云逸', '云逸', '2024-12-04 17:35:50', '2025-08-06 09:57:30');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1870104848520814594, '查询权限详情', '查询权限详情', 'permission:permissionDetails',
        '/permission/permissionDetails/{id}', 'GET', 3, NULL, NULL, 1, 1919661224031989762, '', '', 1, 'ep:bell',
        'ep:bell', '', '', '', 'true', 0, 1, 0, 0, 0, 7, 1, 1, '云逸', '云逸', '2024-12-20 21:51:55',
        '2025-08-06 09:52:40');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1919658873598554113, 'menus.pureSysManagement', 'System', 'menu:system', '/system', NULL, 0, NULL, NULL, NULL,
        0, '', '', 0, 'ri:settings-3-line', '', '', '', '', '', 0, 1, 0, 0, 1, 1, 1, 1, '云逸', '云逸',
        '2025-05-06 15:41:56', '2025-08-04 17:46:35');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1919660730064613377, 'menus.pureUser', 'SystemUser', 'menu:system:user', '/system/user/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/user/index', '', 0, 'ri:admin-line', '', 'pulse', 'flash', '', '', 0,
        1, 0, 0, 1, 1, 1, 1, '云逸', '云逸', '2025-05-06 15:49:18', '2025-05-06 15:58:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1919660995832492033, 'menus.pureRole', 'SystemRole', 'menu:system:role', '/system/role/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/role/index', '', 0, 'ri:admin-fill', '', 'pulse', 'flash', '', '', 0,
        1, 0, 0, 1, 2, 1, 1, '云逸', '云逸', '2025-05-06 15:50:22', '2025-05-06 15:59:02');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1919661224031989762, 'menus.pureSystemMenu', 'SystemMenu', 'menu:system:menu', '/system/menu/index', NULL, 0,
        NULL, NULL, NULL, 1919658873598554113, '/system/menu/index', '', 0, 'ep:menu', '', 'pulse', 'flash', '', '', 0,
        1, 0, 0, 1, 3, 1, 1, '云逸', '云逸', '2025-05-06 15:51:16', '2025-08-06 09:44:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1919661595269836801, 'menus.pureDept', 'SystemDept', 'menu:system:dept', '/system/dept/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/dept/index', '', 1, 'ri:git-branch-line', '', 'pulse', 'flash', '',
        '', 0, 1, 0, 0, 1, 4, 1, 1, '云逸', '云逸', '2025-05-06 15:52:45', '2025-05-06 15:59:16');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952306933212164097, 'menus.pureSystemDict', 'SystemDict', 'system:dict:index', '/system/dict/index', '', 0,
        'system', NULL, 1, 1919658873598554113, '/system/dict/index', '', 0, 'ri:book-2-line', '', '', '', '', '', 1, 1,
        0, 0, 0, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:53:40', '2025-08-06 09:44:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184129, '修改scope信息', '修改scope信息', 'scope:update', '/scope/update', 'PUT', 3, 'auth', NULL,
        0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 1,
        1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 17:36:03');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184130, '重置scope对应的权限', '重置scope对应的权限', 'scope:resetScopePermission',
        '/scope/resetScopePermission', 'PUT', 3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-05 18:05:16');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184131, '更新客户端信息', '更新客户端信息', 'application:update', '/application/update', 'PUT', 3,
        'auth', NULL, 0, 1952658080221388801, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 10:02:38');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184132, '测试Json请求验证', '测试Json请求验证', 'test:validateJson', '/test/validateJson', 'POST',
        3, 'auth', NULL, 0, 1952661871538835457, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 17:24:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184133, '测试表单验证', '测试表单验证', 'test:validateForm', '/test/validateForm', 'POST', 3,
        'auth', NULL, 0, 1952661871538835457, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 17:24:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184134, '保存scope信息', '保存scope信息', 'scope:save', '/scope/save', 'POST', 3, 'auth', NULL, 0,
        1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 17:36:03');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184135, '移动端扫码', '移动端扫码', 'qr-code:app:scan', '/qr-code/app/scan', 'POST', 3, 'auth',
        NULL, 0, 1952671270177181698, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:01:40');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184136, '移动端确认登录', '移动端确认登录', 'qr-code:app:confirm', '/qr-code/app/confirm', 'POST',
        3, 'auth', NULL, 0, 1952671270177181698, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:01:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184137, '保存客户端信息', '保存客户端信息', 'application:save', '/application/save', 'POST', 3,
        'auth', NULL, 0, 1952658080221388801, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 10:02:35');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184138, '测试手机号验证', '测试手机号验证', 'test:validatePhone', '/test/validatePhone', 'GET', 3,
        'auth', NULL, 0, 1952661871538835457, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 17:24:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184139, '测试接口，需要有profile权限', '测试接口，需要有profile权限', 'test:test01', '/test/test01',
        'GET', 3, 'auth', NULL, 0, 1952661871538835457, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 17:24:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184140, '查询所有的scope', '查询所有的scope', 'scope:findScopeList', '/scope/findScopeList', 'GET',
        3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 17:36:03');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184141, '查询scope对应的权限', '查询scope对应的权限', 'scope:findPermissionIdsByScope',
        '/scope/findPermissionIdsByScope/*', 'GET', 3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-05 17:36:03');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184142, '根据入参分页查询scope信息', '根据入参分页查询scope信息', 'scope:findByPage',
        '/scope/findByPage', 'GET', 3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-05 17:36:03');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184143, '轮询二维码状态', '轮询二维码状态', 'qr-code:poll', '/qr-code/poll', 'GET', 3, 'auth',
        NULL, 0, 1952671270177181698, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:01:35');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184144, '生成二维码', '生成二维码', 'qr-code:init', '/qr-code/init', 'GET', 3, 'auth', NULL, 0,
        1952671270177181698, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:01:35');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184145, '授权确认页面', '授权确认页面', 'oauth2:consent', '/oauth2/consent', 'GET', 3, 'auth',
        NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9, 1, 1, '云逸',
        '云逸', '2025-08-04 17:54:29', '2025-08-05 17:24:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184146, '登录页面', '登录页面', 'login', '/login', 'GET', 3, 'auth', NULL, 0, 0, NULL, NULL, 1,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1, '云逸', '云逸',
        '2025-08-04 17:54:29', '2025-08-05 18:00:30');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184147, '获取短信验证码', '获取短信验证码', 'getSmsCaptcha', '/getSmsCaptcha', 'GET', 3, 'auth',
        NULL, 0, 1952671616769298434, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:03:01');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184148, '获取邮件验证码', '获取邮件验证码', 'getEmailCaptcha', '/getEmailCaptcha', 'GET', 3,
        'auth', NULL, 0, 1952671616769298434, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:02:57');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184149, '获取验证码', '获取验证码', 'getCaptcha', '/getCaptcha', 'GET', 3, 'auth', NULL, 0,
        1952671616769298434, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:03:01');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184150, '检查是否登录过', '检查是否登录过', 'check:login', '/check/login', 'GET', 3, 'auth', NULL,
        0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 1, 1, '云逸', '云逸',
        '2025-08-04 17:54:29', '2025-08-05 18:03:01');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184151, '根据入参分页查询认证信息', '根据入参分页查询认证信息', 'authorization:findByPage',
        '/authorization/findByPage', 'GET', 3, 'auth', NULL, 0, 1952657828596703234, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:43:57');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184152, '根据id查询认证信息详情', '根据id查询认证信息详情', 'authorization:findById',
        '/authorization/findById/*', 'GET', 3, 'auth', NULL, 0, 1952657828596703234, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-05 18:03:37');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184153, '根据入参分页查询客户端信息', '根据入参分页查询客户端信息', 'application:findByPage',
        '/application/findByPage', 'GET', 3, 'auth', NULL, 0, 1952657433640067074, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 10:02:48');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184154, '根据id查询客户端信息', '根据id查询客户端信息', 'application:findById',
        '/application/findById/*', 'GET', 3, 'auth', NULL, 0, 1952658080221388801, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 10:02:38');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184155, '根据客户端id查询客户端信息', '根据客户端id查询客户端信息', 'application:findByClientId',
        '/application/findByClientId/*', 'GET', 3, 'auth', NULL, 0, 1952657433640067074, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 10:02:48');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184156, '获取应用卡片列表数据', '获取应用卡片列表数据', 'application:cardListPage',
        '/application/cardListPage', 'GET', 3, 'auth', NULL, 0, 1952657433640067074, '', '', 0, '', '', '', '', '', '',
        0, 1, 0, 0, 0, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 10:02:48');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184157, '设备码-设备码验证成功页面', '设备码-设备码验证成功页面', 'activated', '/activated', 'GET',
        3, 'auth', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:04:03');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184158, '设备码-设备码验证页面', '设备码-设备码验证页面', 'activate', '/activate', 'GET', 3,
        'auth', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-05 18:04:03');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184159, '设备码-设备码验证成功页面', '设备码-设备码验证成功页面', '', '/', 'GET', 3, 'auth', NULL,
        0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9, 1, 1, '云逸', '云逸',
        '2025-08-04 17:54:29', '2025-08-05 18:04:03');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184160, '根据id删除scope', '根据id删除scope', 'scope:removeById', '/scope/removeById/*', 'DELETE',
        3, 'auth', NULL, 0, 1952657633259577345, '', '', 0, '', '', '', '', '', '', 0, 1, 0, 0, 0, 6, 1, 1, '云逸',
        '云逸', '2025-08-04 17:54:29', '2025-08-05 18:05:16');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184161, '踢出登录', '踢出登录', 'authorization:offline', '/authorization/offline', 'DELETE', 3,
        'auth', NULL, 0, 1952657828596703234, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:43:57');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184162, '根据数据id删除客户端', '根据数据id删除客户端', 'application:remove',
        '/application/remove/*', 'DELETE', 3, 'auth', NULL, 0, 1952657433640067074, '', '', 0, '', '', '', '', '', '',
        0, 1, 0, 0, 0, 5, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 10:03:07');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184163, 'Valid when multiple issuers are allowed', 'Valid when multiple issuers are allowed',
        '.well-known:oauth-authorization-server', '/.well-known/oauth-authorization-server/*', 'GET', 3, 'auth', NULL,
        0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, 1, 1, '云逸', '云逸',
        '2025-08-04 17:54:29', '2025-08-05 18:05:30');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184164, 'Valid when multiple issuers are allowed', 'Valid when multiple issuers are allowed',
        '.well-known:openid-configuration', '/*/.well-known/openid-configuration', 'GET', 3, 'auth', NULL, 0, 0, NULL,
        NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9, 1, 1, '云逸', '云逸',
        '2025-08-04 17:54:29', '2025-08-05 18:05:30');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184165, '更新用户角色', '更新用户角色', 'user:updateUserRoles', '/user/updateUserRoles', 'PUT', 3,
        'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 7, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:56:57');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184166, '修改用户信息', '修改用户信息', 'user:updateBasicUser', '/user/updateBasicUser', 'PUT', 3,
        'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:49:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184167, '重置密码', '重置密码', 'user:resetPassword', '/user/resetPassword', 'PUT', 3, 'system',
        NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        5, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:49:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184168, '修改角色信息', '修改角色信息', 'role:updateRole', '/role/updateRole', 'PUT', 3, 'system',
        NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        6, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:57:14');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184169, '更新角色权限', '更新角色权限', 'role:updateRolePermissions',
        '/role/updateRolePermissions', 'PUT', 3, 'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:57:14');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184170, '批量修改权限信息', '批量修改权限信息', 'permission:batchUpdatePermissions',
        '/permission/batchUpdatePermissions', 'PUT', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:53:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184171, '根据字典类型ID查询字典类型', '根据字典类型ID查询字典类型', 'dict:type', '/dict/type/*',
        'GET', 3, 'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 7, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184172, '更新字典类型', '更新字典类型', 'dict:type', '/dict/type/*', 'PUT', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184173, '删除字典类型', '删除字典类型', 'dict:type', '/dict/type/*', 'DELETE', 3, 'system', NULL,
        0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 11, 1,
        1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184174, '根据字典项ID查询字典项', '根据字典项ID查询字典项', 'dict:item', '/dict/item/*', 'GET', 3,
        'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 8, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184175, '更新字典项', '更新字典项', 'dict:item', '/dict/item/*', 'PUT', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 10, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184176, '删除字典项', '删除字典项', 'dict:item', '/dict/item/*', 'DELETE', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 12, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184177, '文件下载预签名', '文件下载预签名', 'common:pre:signed', '/common/pre/signed', 'GET', 3,
        'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:45:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184178, '文件上传预签名', '文件上传预签名', 'common:pre:signed', '/common/pre/signed', 'PUT', 3,
        'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:45:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184179, '删除文件预签名', '删除文件预签名', 'common:pre:signed', '/common/pre/signed', 'DELETE', 3,
        'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:45:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184180, '添加API接口信息', '添加API接口信息', 'api-endpoint', '/api-endpoint', 'POST', 3, 'system',
        NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        8, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184181, '修改API接口信息', '修改API接口信息', 'api-endpoint', '/api-endpoint', 'PUT', 3, 'system',
        NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        6, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184182, '设置接口状态为忽略', '设置接口状态为忽略', 'api-endpoint:ignore', '/api-endpoint/ignore',
        'PUT', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 7, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184183, '用户注册', '用户注册', 'user:userRegister', '/user/userRegister', 'POST', 3, 'system',
        NULL, 0, 1919660730064613377, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:46:51');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184184, '添加一条用户信息', '添加一条用户信息', 'user:insertBasicUser', '/user/insertBasicUser',
        'POST', 3, 'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:49:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184185, '重新发送绑定确认', '重新发送绑定确认', 'third:user:resend-bind-confirmation',
        '/third/user/resend-bind-confirmation', 'POST', 3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:46:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184186, '获取增强的三方用户信息', '获取增强的三方用户信息', 'third:user:enhanced-third-user',
        '/third/user/enhanced-third-user', 'POST', 3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:46:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184187, '绑定邮箱', '绑定邮箱', 'third:user:bind-email', '/third/user/bind-email', 'POST', 3,
        'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:46:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184188, '添加角色信息', '添加角色信息', 'role:insertRole', '/role/insertRole', 'POST', 3, 'system',
        NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:57:14');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184189, '添加权限信息', '添加权限信息', 'permission:insertPermission',
        '/permission/insertPermission', 'POST', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:53:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184190, '获取没有父节点的权限id列表', '获取没有父节点的权限id列表',
        'permission:findNonParentPermissions', '/permission/findNonParentPermissions', 'POST', 3, 'system', NULL, 0,
        1919661224031989762, '', '', 0, '', '', '', '', '', '', 0, 1, 0, 0, 0, 3, 1, 1, '云逸', '云逸',
        '2025-08-04 17:54:29', '2025-08-06 09:53:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184191, '创建字典类型', '创建字典类型', 'dict:type', '/dict/type', 'POST', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184192, '创建字典项', '创建字典项', 'dict:item', '/dict/item', 'POST', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184193, '邮件发送', '邮件发送', 'common:email:sender', '/common/email/sender', 'POST', 3, 'system',
        NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 1, 1, '云逸',
        '云逸', '2025-08-04 17:54:29', '2025-08-06 09:47:59');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184194, '扫描系统接口', '扫描系统接口', 'api-endpoint:scan:endpoints',
        '/api-endpoint/scan/endpoints', 'POST', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184195, '查询API接口信息列表', '查询API接口信息列表', 'api-endpoint:list', '/api-endpoint/list',
        'POST', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184196, '批量导入接口到权限表', '批量导入接口到权限表', 'api-endpoint:import',
        '/api-endpoint/import', 'POST', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 10, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184197, '根据扫描批次ID导入接口', '根据扫描批次ID导入接口', 'api-endpoint:import:batch',
        '/api-endpoint/import/batch/*', 'POST', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184198, '查询用户详情', '查询用户详情', 'user:userDetails', '/user/userDetails/*', 'GET', 3,
        'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:49:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184199, '获取登录用户信息', '获取登录用户信息', 'user:loginUserinfo', '/user/loginUserinfo', 'GET',
        3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 1,
        1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:49:18');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184200, '获取注册时使用的邮箱验证码', '获取注册时使用的邮箱验证码', 'user:getRegisterEmailCode',
        '/user/getRegisterEmailCode/*', 'GET', 3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:49:18');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184201, '分页查询基础用户信息列表', '分页查询基础用户信息列表', 'user:findByPage',
        '/user/findByPage', 'GET', 3, 'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:49:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184202, '三方登录用户确认绑定本地账号', '三方登录用户确认绑定本地账号', 'third:user:confirm',
        '/third/user/confirm', 'GET', 3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:49:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184203, '检查当前三方用户是否绑定本地用户', '检查当前三方用户是否绑定本地用户',
        'third:user:check-binding', '/third/user/check-binding', 'GET', 3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:49:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184204, '获取绑定邮箱验证码', '获取绑定邮箱验证码', 'third:user:bind-email-code',
        '/third/user/bind-email-code/*', 'GET', 3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, 6, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:49:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184206, '查询角色详情', '查询角色详情', 'role:roleDetails', '/role/roleDetails/*', 'GET', 3,
        'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:51:01');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184207, '根据条件查询所有角色列表', '根据条件查询所有角色列表', 'role:findRoles',
        '/role/findRoles', 'GET', 3, 'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:51:01');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184208, '根据用户id查询角色id列表', '根据用户id查询角色id列表', 'role:findRoleIdsByUserId',
        '/role/findRoleIdsByUserId/*', 'GET', 3, 'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:50:46');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184209, '分页查询角色信息列表', '分页查询角色信息列表', 'role:findByPage', '/role/findByPage',
        'GET', 3, 'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:51:01');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184210, '查询权限详情', '查询权限详情', 'permission:permissionDetails',
        '/permission/permissionDetails/*', 'GET', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:53:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184211, '查询权限信息列表', '查询权限信息列表', 'permission:findPermissions',
        '/permission/findPermissions', 'GET', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:53:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184212, '根据角色id查询权限id列表', '根据角色id查询权限id列表',
        'permission:findPermissionIdsByRoleId', '/permission/findPermissionIdsByRoleId/*', 'GET', 3, 'system', NULL, 0,
        1919661224031989762, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, 1, 1,
        '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184213, '分页查询权限信息列表', '分页查询权限信息列表', 'permission:findByPage',
        '/permission/findByPage', 'GET', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:53:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184214, '查询所有字典类型', '查询所有字典类型', 'dict:type:page', '/dict/type/page', 'GET', 3,
        'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:39');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184215, '查询所有字典项', '查询所有字典项', 'dict:type:all', '/dict/type/all', 'GET', 3, 'system',
        NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184216, '根据字典类型编码查询字典项', '根据字典类型编码查询字典项', 'dict:item:type',
        '/dict/item/type/*', 'GET', 3, 'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184217, '分页查询字典项', '分页查询字典项', 'dict:item:page', '/dict/item/page', 'GET', 3,
        'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:53:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184218, '查询API扫描记录详情', '查询API扫描记录详情', 'api-scan', '/api-scan/*', 'GET', 3,
        'system', NULL, 0, 1952647718126247937, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:54:06');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184219, '删除API扫描记录信息', '删除API扫描记录信息', 'api-scan', '/api-scan/*', 'DELETE', 3,
        'system', NULL, 0, 1952647718126247937, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 3, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:54:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184220, '分页查询API扫描记录列表', '分页查询API扫描记录列表', 'api-scan:page', '/api-scan/page',
        'GET', 3, 'system', NULL, 0, 1952647718126247937, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:54:56');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184221, '查询API接口详情', '查询API接口详情', 'api-endpoint', '/api-endpoint/*', 'GET', 3,
        'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 4, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184222, '删除API接口信息', '删除API接口信息', 'api-endpoint', '/api-endpoint/*', 'DELETE', 3,
        'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 11, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184223, '分页查询API接口列表', '分页查询API接口列表', 'api-endpoint:page', '/api-endpoint/page',
        'GET', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 2, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184224, '根据扫描批次ID查询接口列表', '根据扫描批次ID查询接口列表', 'api-endpoint:batch',
        '/api-endpoint/batch/*', 'GET', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29',
        '2025-08-06 09:55:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184225, '删除用户信息', '删除用户信息', 'user:removeById', '/user/removeById/*', 'DELETE', 3,
        'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 8, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:56:57');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184226, '删除角色信息', '删除角色信息', 'role:removeById', '/role/removeById/*', 'DELETE', 3,
        'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 7, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:57:14');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952307138347184227, '删除权限信息', '删除权限信息', 'permission:removeById', '/permission/removeById/*',
        'DELETE', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 8, 1, 1, '云逸', '云逸', '2025-08-04 17:54:29', '2025-08-06 09:57:30');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952647718126247937, 'menus.pureApiScan', 'pureApiScan', 'system:api-scan:index', '/system/api-scan/index', '',
        0, 'system', NULL, 1, 1919658873598554113, '/system/api-scan/index', '', 0, 'ri:scan-2-line', '', '', '', '',
        '', 0, 1, 0, 0, 0, 5, 1, 1, '云逸', '云逸', '2025-08-05 16:27:49', '2025-08-06 09:45:12');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952657129544638465, 'menus.platformManagement', 'platform', 'platform', '/platform', '', 0, 'system', NULL, 1,
        0, '/platform', '', 0, 'ri:planet-line', '', '', '', '', '', 0, 1, 0, 0, 0, 2, 1, 1, '云逸', '云逸',
        '2025-08-05 17:05:13', '2025-08-06 09:49:01');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952657433640067074, 'menus.application', 'Application', 'platform:application:index',
        '/platform/application/index', '', 0, 'auth', NULL, 1, 1952657129544638465, '/platform/application/index', '',
        0, 'ri:apps-line', '', '', '', '', '', 0, 1, 0, 0, 0, 1, 1, 1, '云逸', '云逸', '2025-08-05 17:06:25',
        '2025-08-05 17:06:25');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952657633259577345, 'menus.scopeManagement', 'PlatformScope', 'platform:scope:index', '/platform/scope/index',
        '', 0, '', NULL, 1, 1952657129544638465, '/platform/scope/index', '', 0, 'ep:connection', '', '', '', '', '', 0,
        1, 0, 0, 0, 2, 1, 1, '云逸', '云逸', '2025-08-05 17:07:13', '2025-08-05 18:05:57');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952657828596703234, 'menus.authorization', 'Authorization', ':platform:authorization:index',
        '/platform/authorization/index', '', 0, '', NULL, 1, 1952657129544638465, '/platform/authorization/index', '',
        0, 'ri:login-circle-line', '', '', '', '', '', 0, 1, 0, 0, 0, 3, 1, 1, '云逸', '云逸', '2025-08-05 17:08:00',
        '2025-08-05 18:05:57');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952658080221388801, 'menus.applicationDetail', 'ApplicationDetails', 'platform:application:detail',
        '/platform/application/detail', '', 0, '', NULL, 1, 1952657433640067074, '/platform/application/detail', '', 0,
        'ep:help', '', '', '', '', '', 0, 0, 0, 0, 0, 1, 1, 1, '云逸', '云逸', '2025-08-05 17:09:00',
        '2025-08-05 18:03:58');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952658732645376001, 'menus.pureApiEndpoints', 'ApiEndpoints', 'system:api-scan:endpoints',
        '/system/api-scan/endpoints', '', 0, '', NULL, 1, 1952647718126247937, '/system/api-scan/endpoints', '', 0,
        'fa-solid:cubes', '', '', '', '', '', 0, 0, 0, 0, 0, 4, 1, 1, '云逸', '云逸', '2025-08-05 17:11:35',
        '2025-08-06 10:00:42');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952661871538835457, '测试接口', 'test', 'test', 'test', '', 0, '', NULL, 1, 0, 'test', '', 0,
        'ri:test-tube-line', '', '', '', '', '', 0, 0, 0, 0, 0, 3, 1, 1, '云逸', '云逸', '2025-08-05 17:24:04',
        '2025-08-06 09:49:01');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952662576219656194, '登录相关接口', 'login', 'login', 'login', '', 0, '', NULL, 1, 0, '', '', 1,
        'ri:login-box-line', '', '', '', '', '', 0, 0, 0, 0, 0, 4, 1, 1, '云逸', '云逸', '2025-08-05 17:26:52',
        '2025-08-05 18:03:37');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952671270177181698, '扫码登录', 'scan-qrcode', 'scan-qrcode', 'scan-qrcode', '', 0, '', NULL, 1,
        1952662576219656194, 'scan-qrcode', '', 1, 'ri:qr-scan-2-line', '', '', '', '', '', 0, 0, 0, 0, 0, 1, 1, 1,
        '云逸', '云逸', '2025-08-05 18:01:24', '2025-08-05 18:01:45');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type, module_name,
                            description, need_authentication, parent_id, component, redirect, deleted, icon, extra_icon,
                            enter_transition, leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, create_by, update_by, create_name, update_name,
                            create_time, update_time)
VALUES (1952671616769298434, '公共接口', 'common', 'common', 'common', '', 0, '', NULL, 1, 0, 'common', '', 1,
        'ri:creative-commons-nd-line', '', '', '', '', '', 0, 1, 0, 0, 0, 5, 1, 1, '云逸', '云逸',
        '2025-08-05 18:02:47', '2025-08-05 18:03:37');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint                                                 NOT NULL COMMENT '主键id',
    `code`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '角色代码',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '角色名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色描述',
    `deleted`     tinyint(1)                                             NULL DEFAULT NULL COMMENT '是否已删除',
    `create_by`   bigint                                                 NULL DEFAULT NULL COMMENT '创建人',
    `update_by`   bigint                                                 NULL DEFAULT NULL COMMENT '修改人',
    `create_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '修改人名称',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'RBAC角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO sys_role (id, code, name, description, deleted, create_by, update_by, create_name, update_name, create_time,
                      update_time)
VALUES (1, 'normal', '普通用户', '普通用户', 0, 1, 1, '云逸', '云逸', '2024-11-25 17:37:41', '2024-11-25 17:37:41');
INSERT INTO sys_role (id, code, name, description, deleted, create_by, update_by, create_name, update_name, create_time,
                      update_time)
VALUES (1864508890354565121, 'addRole01', '添加用户01', '测试添加用户-01', 0, 1, 1, '云逸', '云逸',
        '2024-12-05 11:15:35', '2024-12-05 11:19:59');
INSERT INTO sys_role (id, code, name, description, deleted, create_by, update_by, create_name, update_name, create_time,
                      update_time)
VALUES (1864511627943235585, 'addRole02', '添加用户02', '测试添加用户-02', 0, 1, 1, '云逸', '云逸',
        '2024-12-05 11:26:28', '2024-12-05 11:26:28');
INSERT INTO sys_role (id, code, name, description, deleted, create_by, update_by, create_name, update_name, create_time,
                      update_time)
VALUES (1921501763071176706, 'testRole', '测试角色', '测试角色1', 0, 1, 1, '云逸', '云逸', '2025-05-11 17:44:55',
        '2025-05-11 17:49:14');
INSERT INTO sys_role (id, code, name, description, deleted, create_by, update_by, create_name, update_name, create_time,
                      update_time)
VALUES (1948585988513411073, 'admin', '管理员', '管理员角色', 0, 1, 1, '云逸', '云逸', '2025-07-25 11:27:57',
        '2025-07-25 11:27:57');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `id`            bigint                                                NOT NULL COMMENT '角色菜单关联表ID',
    `role_id`       bigint                                                NOT NULL COMMENT '角色ID',
    `permission_id` bigint                                                NOT NULL COMMENT '权限菜单ID',
    `create_by`     bigint                                                NULL DEFAULT NULL COMMENT '创建人',
    `update_by`     bigint                                                NULL DEFAULT NULL COMMENT '修改人',
    `create_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '修改人名称',
    `create_time`   datetime                                              NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                              NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1924347637514170369, 1921501763071176706, 1864241504623833089, 1, 1, '云逸', '云逸', '2025-05-19 14:13:24',
        '2025-05-19 14:13:24');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1924347637514170370, 1921501763071176706, 1919660730064613377, 1, 1, '云逸', '云逸', '2025-05-19 14:13:24',
        '2025-05-19 14:13:24');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1924347637514170371, 1921501763071176706, 1919661224031989762, 1, 1, '云逸', '云逸', '2025-05-19 14:13:24',
        '2025-05-19 14:13:24');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1924347637514170372, 1921501763071176706, 1919658873598554113, 1, 1, '云逸', '云逸', '2025-05-19 14:13:24',
        '2025-05-19 14:13:24');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569589959176194, 1, 1, 1, 1, '云逸', '云逸', '2025-07-25 10:22:48', '2025-07-25 10:22:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569589963370497, 1, 1870104848520814594, 1, 1, '云逸', '云逸', '2025-07-25 10:22:48',
        '2025-07-25 10:22:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569589963370498, 1, 1864241504623833089, 1, 1, '云逸', '云逸', '2025-07-25 10:22:48',
        '2025-07-25 10:22:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569589963370499, 1, 1919661224031989762, 1, 1, '云逸', '云逸', '2025-07-25 10:22:48',
        '2025-07-25 10:22:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569589963370500, 1, 1948569373352734721, 1, 1, '云逸', '云逸', '2025-07-25 10:22:48',
        '2025-07-25 10:22:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569589963370501, 1, 1919660730064613377, 1, 1, '云逸', '云逸', '2025-07-25 10:22:48',
        '2025-07-25 10:22:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569589963370502, 1, 1919658873598554113, 1, 1, '云逸', '云逸', '2025-07-25 10:22:48',
        '2025-07-25 10:22:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569710960652290, 1864508890354565121, 1864241504623833089, 1, 1, '云逸', '云逸', '2025-07-25 10:23:16',
        '2025-07-25 10:23:16');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569710960652291, 1864508890354565121, 1919660995832492033, 1, 1, '云逸', '云逸', '2025-07-25 10:23:16',
        '2025-07-25 10:23:16');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569710960652292, 1864508890354565121, 1870104848520814594, 1, 1, '云逸', '云逸', '2025-07-25 10:23:16',
        '2025-07-25 10:23:16');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569710960652293, 1864508890354565121, 1919661224031989762, 1, 1, '云逸', '云逸', '2025-07-25 10:23:16',
        '2025-07-25 10:23:16');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569710960652294, 1864508890354565121, 1948569373352734721, 1, 1, '云逸', '云逸', '2025-07-25 10:23:16',
        '2025-07-25 10:23:16');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569710960652295, 1864508890354565121, 1919660730064613377, 1, 1, '云逸', '云逸', '2025-07-25 10:23:16',
        '2025-07-25 10:23:16');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1948569710960652296, 1864508890354565121, 1919658873598554113, 1, 1, '云逸', '云逸', '2025-07-25 10:23:16',
        '2025-07-25 10:23:16');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013762, 1948585988513411073, 1952657433640067074, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013763, 1948585988513411073, 1952658080221388801, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013764, 1948585988513411073, 1952307138347184137, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013765, 1948585988513411073, 1952307138347184131, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013766, 1948585988513411073, 1952307138347184154, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013767, 1948585988513411073, 1952307138347184156, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013768, 1948585988513411073, 1952307138347184153, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013769, 1948585988513411073, 1952307138347184155, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013770, 1948585988513411073, 1952307138347184162, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013771, 1948585988513411073, 1919658873598554113, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013772, 1948585988513411073, 1919660730064613377, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013773, 1948585988513411073, 1952307138347184201, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013774, 1948585988513411073, 1952307138347184198, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013775, 1948585988513411073, 1952307138347184184, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013776, 1948585988513411073, 1952307138347184166, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013777, 1948585988513411073, 1952307138347184167, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013778, 1948585988513411073, 1952307138347184208, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013779, 1948585988513411073, 1952307138347184165, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013780, 1948585988513411073, 1952307138347184225, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013781, 1948585988513411073, 1919660995832492033, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013782, 1948585988513411073, 1952307138347184209, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013783, 1948585988513411073, 1952307138347184207, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013784, 1948585988513411073, 1952307138347184206, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013785, 1948585988513411073, 1952307138347184188, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013786, 1948585988513411073, 1952307138347184169, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013787, 1948585988513411073, 1952307138347184168, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013788, 1948585988513411073, 1952307138347184226, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013789, 1948585988513411073, 1919661224031989762, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013790, 1948585988513411073, 1952307138347184213, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013791, 1948585988513411073, 1952307138347184211, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013792, 1948585988513411073, 1952307138347184190, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013793, 1948585988513411073, 1952307138347184210, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013794, 1948585988513411073, 1952307138347184189, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013795, 1948585988513411073, 1952307138347184170, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013796, 1948585988513411073, 1952307138347184212, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013797, 1948585988513411073, 1952307138347184227, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013798, 1948585988513411073, 1864241504623833089, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013799, 1948585988513411073, 1952306933212164097, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013800, 1948585988513411073, 1952307138347184214, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013801, 1948585988513411073, 1952307138347184215, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013802, 1948585988513411073, 1952307138347184217, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013803, 1948585988513411073, 1952307138347184216, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013804, 1948585988513411073, 1952307138347184191, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013805, 1948585988513411073, 1952307138347184192, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013806, 1948585988513411073, 1952307138347184171, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013807, 1948585988513411073, 1952307138347184174, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013808, 1948585988513411073, 1952307138347184172, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013809, 1948585988513411073, 1952307138347184175, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013810, 1948585988513411073, 1952307138347184173, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013811, 1948585988513411073, 1952307138347184176, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013812, 1948585988513411073, 1952647718126247937, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013813, 1948585988513411073, 1952307138347184218, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013814, 1948585988513411073, 1952307138347184220, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013815, 1948585988513411073, 1952307138347184219, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013816, 1948585988513411073, 1952658732645376001, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013817, 1948585988513411073, 1952307138347184224, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013818, 1948585988513411073, 1952307138347184223, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013819, 1948585988513411073, 1952307138347184195, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013820, 1948585988513411073, 1952307138347184221, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013821, 1948585988513411073, 1952307138347184194, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013822, 1948585988513411073, 1952307138347184181, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013823, 1948585988513411073, 1952307138347184182, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013824, 1948585988513411073, 1952307138347184180, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013825, 1948585988513411073, 1952307138347184197, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013826, 1948585988513411073, 1952307138347184196, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013827, 1948585988513411073, 1952307138347184222, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013828, 1948585988513411073, 1952307138347184139, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013829, 1948585988513411073, 1952307138347184142, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013830, 1948585988513411073, 1952307138347184152, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013831, 1948585988513411073, 1952657129544638465, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013832, 1948585988513411073, 1952657633259577345, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013833, 1948585988513411073, 1952307138347184140, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013834, 1948585988513411073, 1952307138347184141, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013835, 1948585988513411073, 1952307138347184134, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013836, 1948585988513411073, 1952307138347184129, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013837, 1948585988513411073, 1952307138347184160, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013838, 1948585988513411073, 1952307138347184130, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013839, 1948585988513411073, 1952657828596703234, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013840, 1948585988513411073, 1952307138347184151, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013841, 1948585988513411073, 1952307138347184161, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013842, 1948585988513411073, 1952307138347184132, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013843, 1948585988513411073, 1952661871538835457, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013844, 1948585988513411073, 1952307138347184138, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1952914472673013845, 1948585988513411073, 1952307138347184133, 1, 1, '云逸', '云逸', '2025-08-06 10:07:48',
        '2025-08-06 10:07:48');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          bigint                                                NOT NULL COMMENT '主键id',
    `role_id`     bigint                                                NULL DEFAULT NULL COMMENT '角色ID',
    `user_id`     bigint                                                NULL DEFAULT NULL COMMENT '用户ID',
    `create_by`   bigint                                                NULL DEFAULT NULL COMMENT '创建人',
    `update_by`   bigint                                                NULL DEFAULT NULL COMMENT '修改人',
    `create_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '修改人名称',
    `create_time` datetime                                              NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                              NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `basic-cloud-platform`.sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                                                  create_time, update_time)
VALUES (1921489309037510658, 1, 1862332106783637506, 1, 1, '云逸', '云逸', '2025-05-11 16:55:26',
        '2025-05-11 16:55:26');
INSERT INTO `basic-cloud-platform`.sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                                                  create_time, update_time)
VALUES (1921489309037510659, 1864508890354565121, 1862332106783637506, 1, 1, '云逸', '云逸', '2025-05-11 16:55:26',
        '2025-05-11 16:55:26');
INSERT INTO `basic-cloud-platform`.sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                                                  create_time, update_time)
VALUES (1923297772209012737, 1, 1920404587435859970, 1, 1, '云逸', '云逸', '2025-05-16 16:41:37',
        '2025-05-16 16:41:37');
INSERT INTO `basic-cloud-platform`.sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                                                  create_time, update_time)
VALUES (1923297772209012738, 1864511627943235585, 1920404587435859970, 1, 1, '云逸', '云逸', '2025-05-16 16:41:37',
        '2025-05-16 16:41:37');
INSERT INTO `basic-cloud-platform`.sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                                                  create_time, update_time)
VALUES (1948586077352964098, 1, 1, 1, 1, '云逸', '云逸', '2025-07-25 11:28:18', '2025-07-25 11:28:18');
INSERT INTO `basic-cloud-platform`.sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                                                  create_time, update_time)
VALUES (1948586077352964099, 1948585988513411073, 1, 1, 1, '云逸', '云逸', '2025-07-25 11:28:18',
        '2025-07-25 11:28:18');

-- ----------------------------
-- Table structure for sys_third_user_bind
-- ----------------------------
DROP TABLE IF EXISTS `sys_third_user_bind`;
CREATE TABLE `sys_third_user_bind`
(
    `id`               bigint                                                 NOT NULL COMMENT '主键',
    `user_id`          bigint                                                 NOT NULL COMMENT '本地系统用户ID',
    `provider`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '第三方平台标识（如 github/gitee/wechat）',
    `provider_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '第三方用户唯一标识（如 openid 或 user_id）',
    `email`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '第三方用户邮箱，用于绑定校验',
    `access_token`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '第三方登录时的 access token（可选存储）',
    `refresh_token`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '第三方 refresh token（可选）',
    `expires_at`       datetime                                               NULL DEFAULT NULL COMMENT 'access_token 过期时间',
    `bind_status`      tinyint(1)                                             NULL DEFAULT 0 COMMENT '绑定状态：0-待确认/1-已绑定',
    `bind_time`        datetime                                               NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    `confirm_token`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用于邮箱绑定确认的 token',
    `token_expires_at` datetime                                               NULL DEFAULT NULL COMMENT '确认token有效期',
    `create_time`      datetime                                               NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime                                               NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id` ASC) USING BTREE,
    INDEX `idx_email` (`email` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '第三方账号绑定表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`
(
    `id`          bigint                                                 NOT NULL COMMENT '主键',
    `type_code`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '字典类型编码',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '类型说明',
    `status`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL DEFAULT 'Y' COMMENT '状态（Y=启用，N=禁用）',
    `create_by`   bigint                                                 NULL     DEFAULT NULL COMMENT '创建人',
    `update_by`   bigint                                                 NULL     DEFAULT NULL COMMENT '修改人',
    `create_time` datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                               NULL     DEFAULT NULL COMMENT '修改时间',
    `create_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '创建人名称',
    `update_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '修改人名称',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_type_code` (`type_code` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '字典类型表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`
(
    `id`          bigint                                                 NOT NULL COMMENT '主键',
    `type_code`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '字典类型编码（外键）',
    `item_code`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '字典项键',
    `item_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典项值',
    `sort_order`  int                                                    NULL     DEFAULT 0 COMMENT '排序值',
    `status`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL DEFAULT 'Y' COMMENT '状态（Y=启用，N=禁用）',
    `i18n_json`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '多语言 JSON 值',
    `create_by`   bigint                                                 NULL     DEFAULT NULL COMMENT '创建人',
    `update_by`   bigint                                                 NULL     DEFAULT NULL COMMENT '修改人',
    `create_time` datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                               NULL     DEFAULT NULL COMMENT '修改时间',
    `create_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '创建人名称',
    `update_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '修改人名称',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_type_code` (`type_code` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '字典项表'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_api_scan_record`;
CREATE TABLE `sys_api_scan_record`
(
    `id`                 bigint    NOT NULL COMMENT '主键ID',
    `scan_time`          timestamp NULL                                        DEFAULT CURRENT_TIMESTAMP COMMENT '扫描时间',
    `total_count`        int                                                   DEFAULT '0' COMMENT '总接口数',
    `new_count`          int                                                   DEFAULT '0' COMMENT '新发现接口数',
    `exist_count`        int                                                   DEFAULT '0' COMMENT '已存在接口数',
    `missing_desc_count` int                                                   DEFAULT '0' COMMENT '缺少注解接口数',
    `scan_result`        text COMMENT '扫描结果摘要',
    `create_by`          bigint                                                DEFAULT NULL COMMENT '创建人',
    `update_by`          bigint                                                DEFAULT NULL COMMENT '修改人',
    `create_time`        datetime                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `create_name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称',
    `update_name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称',
    PRIMARY KEY (`id`),
    KEY `idx_scan_time` (`scan_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='接口扫描记录表';

DROP TABLE IF EXISTS `sys_api_endpoint`;
CREATE TABLE `sys_api_endpoint`
(
    `id`                     bigint       NOT NULL COMMENT '主键ID',
    `scan_batch_id`          bigint       NOT NULL COMMENT '扫描批次ID(扫描记录id)',
    `path`                   varchar(100) NOT NULL COMMENT '请求路径',
    `request_method`         varchar(10)  NOT NULL COMMENT '请求方式',
    `module_name`            varchar(100)                                          DEFAULT NULL COMMENT '所属模块',
    `permission`             varchar(50)                                           DEFAULT NULL COMMENT '权限码',
    `title`                  varchar(100)                                          DEFAULT NULL COMMENT '接口描述(标题)',
    `scan_status`            tinyint      NOT NULL COMMENT '扫描状态：1-新发现 2-已存在 3-缺少注释 4-忽略',
    `existing_permission_id` bigint                                                DEFAULT NULL COMMENT '已存在的接口ID（关联权限表接口id）',
    `imported`               tinyint                                               DEFAULT '0' COMMENT '是否已导入：0-未导入 1-已导入',
    `import_time`            timestamp    NULL                                     DEFAULT NULL COMMENT '导入时间',
    `error_message`          text COMMENT '错误信息',
    `create_by`              bigint                                                DEFAULT NULL COMMENT '创建人',
    `update_by`              bigint                                                DEFAULT NULL COMMENT '修改人',
    `create_time`            datetime                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`            datetime                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `create_name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称',
    `update_name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_batch_path_method` (`scan_batch_id`, `module_name`, `path`, `request_method`),
    KEY `idx_scan_batch_id` (`scan_batch_id`),
    KEY `idx_request_path` (`path`),
    KEY `idx_scan_status` (`scan_status`),
    KEY `idx_module_name` (`module_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='扫描接口详情表';

SET
    FOREIGN_KEY_CHECKS = 1;
