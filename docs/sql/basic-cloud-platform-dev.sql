SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_application
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_application`;
CREATE TABLE `oauth2_application`
(
    `id`                            bigint                                                  NOT NULL,
    `client_id`                     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `client_id_issued_at`           timestamp                                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `client_secret`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL,
    `client_secret_expires_at`      timestamp                                               NULL     DEFAULT NULL,
    `client_name`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `client_logo`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL,
    `client_authentication_methods` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `description`                   varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '客户端描述',
    `authorization_grant_types`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `redirect_uris`                 varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL,
    `post_logout_redirect_uris`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL,
    `scopes`                        varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `client_settings`               varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `token_settings`                varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `create_by`                     bigint                                                  NULL     DEFAULT NULL COMMENT '创建人',
    `update_by`                     bigint                                                  NULL     DEFAULT NULL COMMENT '修改人',
    `create_name`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL     DEFAULT NULL COMMENT '创建人名称',
    `update_name`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL     DEFAULT NULL COMMENT '修改人名称',
    `create_time`                   datetime                                                NULL     DEFAULT NULL COMMENT '创建时间',
    `update_time`                   datetime                                                NULL     DEFAULT NULL COMMENT '修改时间',
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
    `id`                            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NOT NULL,
    `registered_client_id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `principal_id`                  bigint                                                  NULL DEFAULT NULL,
    `principal_name`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `authorization_grant_type`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `authorized_scopes`             varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `attributes`                    text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `state`                         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL,
    `authorization_code_value`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `authorization_code_issued_at`  timestamp                                               NULL DEFAULT NULL,
    `authorization_code_expires_at` timestamp                                               NULL DEFAULT NULL,
    `authorization_code_metadata`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `access_token_value`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `access_token_issued_at`        timestamp                                               NULL DEFAULT NULL,
    `access_token_expires_at`       timestamp                                               NULL DEFAULT NULL,
    `access_token_metadata`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `access_token_type`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL,
    `access_token_scopes`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `refresh_token_value`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `refresh_token_issued_at`       timestamp                                               NULL DEFAULT NULL,
    `refresh_token_expires_at`      timestamp                                               NULL DEFAULT NULL,
    `refresh_token_metadata`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `oidc_id_token_value`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `oidc_id_token_issued_at`       timestamp                                               NULL DEFAULT NULL,
    `oidc_id_token_expires_at`      timestamp                                               NULL DEFAULT NULL,
    `oidc_id_token_metadata`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `oidc_id_token_claims`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `user_code_value`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `user_code_issued_at`           timestamp                                               NULL DEFAULT NULL,
    `user_code_expires_at`          timestamp                                               NULL DEFAULT NULL,
    `user_code_metadata`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `device_code_value`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `device_code_issued_at`         timestamp                                               NULL DEFAULT NULL,
    `device_code_expires_at`        timestamp                                               NULL DEFAULT NULL,
    `device_code_metadata`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin          NULL,
    `create_time`                   datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`                   datetime                                                NULL DEFAULT NULL,
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
    `registered_client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `principal_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `authorities`          varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `create_by`            bigint                                                  NULL DEFAULT NULL COMMENT '创建人',
    `update_by`            bigint                                                  NULL DEFAULT NULL COMMENT '修改人',
    `create_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '修改人名称',
    `create_time`          datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`          datetime                                                NULL DEFAULT NULL COMMENT '修改时间',
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
INSERT INTO `sys_basic_user`
VALUES (1, 'admin', '云逸', 'url', '头像', '17683906991@163.com', 1, 9,
        '{bcrypt}$2a$10$2V.a.YAI1UIbyO1OGxjXoehW98xK5kig9L7CidXOorhyuEdSfigAq', '2024-10-01', NULL, NULL, '地址', 0,
        'system', 1, 1, 'system', 'system', '2024-11-22 23:43:47', '2024-11-22 23:43:47');
INSERT INTO `sys_basic_user`
VALUES (1862332106783637506, 'test01', '注册01', NULL, NULL, '17683906001@163.com', 1, NULL,
        '{bcrypt}$2a$10$XH7KjVib/Hwt/k.9qhp.luQEfRgwTEor7Ni1qlqsFpntPTKAU2i5y', NULL, NULL, NULL, NULL, 1, 'system',
        1862332106783637506, 1862332106783637506, '注册01', '注册01', '2024-11-29 11:05:49', '2024-11-29 11:05:49');


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
INSERT INTO `sys_permission`
VALUES (1, '测试接口', '测试接口', 'test:test01', '/test/test01', NULL, 3, NULL, NULL, NULL, 0, '', '', 0, 'ep:bell',
        'ep:bell', '', '', '', '', 0, 1, 0, 0, 0, 99, 1, 1, '云逸', '云逸', '2024-11-25 17:38:48',
        '2025-05-06 15:20:05');
INSERT INTO `sys_permission`
VALUES (1864241504623833089, '修改权限信息', '修改权限信息', 'permission:updatePermission',
        '/permission/updatePermission', 'PUT', 3, NULL, NULL, NULL, 1919661224031989762, '', '', 0, 'ep:bell',
        'ep:bell', '', '', '', '', 0, 1, 0, 0, 0, 99, 1, 1, '云逸', '云逸', '2024-12-04 17:35:50',
        '2025-05-06 16:11:21');
INSERT INTO `sys_permission`
VALUES (1870104848520814594, '查询权限详情', '查询权限详情', 'permission:permissionDetails',
        '/permission/permissionDetails/{id}', '', 3, NULL, NULL, NULL, 1919661224031989762, '', '', 0, 'ep:bell',
        'ep:bell', '', '', '', 'true', 0, 1, 0, 0, 0, 99, 1, 1, '云逸', '云逸', '2024-12-20 21:51:55',
        '2025-05-06 16:11:29');
INSERT INTO `sys_permission`
VALUES (1919658873598554113, 'menus.pureSysManagement', 'System', 'menu:system', '/system', NULL, 0, NULL, NULL, NULL,
        0, '', '', 0, 'ri:settings-3-line', '', '', '', '', '', 0, 1, 0, 0, 1, 3, 1, 1, '云逸', '云逸',
        '2025-05-06 15:41:56', '2025-05-06 15:58:44');
INSERT INTO `sys_permission`
VALUES (1919660730064613377, 'menus.pureUser', 'SystemUser', 'menu:system:user', '/system/user/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/user/index', '', 0, 'ri:admin-line', '', 'pulse', 'flash', '', '', 0,
        1, 0, 0, 1, 1, 1, 1, '云逸', '云逸', '2025-05-06 15:49:18', '2025-05-06 15:58:55');
INSERT INTO `sys_permission`
VALUES (1919660995832492033, 'menus.pureRole', 'SystemRole', 'menu:system:role', '/system/role/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/role/index', '', 0, 'ri:admin-fill', '', 'pulse', 'flash', '', '', 0,
        1, 0, 0, 1, 2, 1, 1, '云逸', '云逸', '2025-05-06 15:50:22', '2025-05-06 15:59:02');
INSERT INTO `sys_permission`
VALUES (1919661224031989762, 'menus.pureSystemMenu', 'SystemMenu', 'menu:system:menu', '/system/menu/index', NULL, 0,
        NULL, NULL, NULL, 1919658873598554113, '/system/menu/index', '', 0, 'ep:menu', '', 'pulse', 'flash', '', '', 0,
        1, 0, 0, 1, 3, 1, 1, '云逸', '云逸', '2025-05-06 15:51:16', '2025-05-06 15:59:10');
INSERT INTO `sys_permission`
VALUES (1919661595269836801, 'menus.pureDept', 'SystemDept', 'menu:system:dept', '/system/dept/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/dept/index', '', 1, 'ri:git-branch-line', '', 'pulse', 'flash', '',
        '', 0, 1, 0, 0, 1, 4, 1, 1, '云逸', '云逸', '2025-05-06 15:52:45', '2025-05-06 15:59:16');

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
INSERT INTO `sys_role`
VALUES (1, 'normal', '普通用户', '普通用户', 0, 1, 1, '云逸', '云逸', '2024-11-25 17:37:41', '2024-11-25 17:37:41');
INSERT INTO `sys_role`
VALUES (1864508890354565121, 'addRole01', '添加用户01', '测试添加用户-01', 1, 1, 1, '云逸', '云逸',
        '2024-12-05 11:15:35', '2024-12-05 11:19:59');
INSERT INTO `sys_role`
VALUES (1864511627943235585, 'addRole02', '添加用户02', '测试添加用户-02', 0, 1, 1, '云逸', '云逸',
        '2024-12-05 11:26:28', '2024-12-05 11:26:28');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `id`            bigint                                                NOT NULL AUTO_INCREMENT COMMENT '角色菜单关联表ID',
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
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission`
VALUES (1, 1, 1, 1, 1, '云逸', '云逸', '2024-11-25 17:39:25', '2024-11-25 17:39:25');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          bigint                                                NOT NULL AUTO_INCREMENT COMMENT '主键id',
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
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (1, 1, 1, 1, 1, '云逸', '云逸', '2024-11-25 17:39:25', '2024-11-25 17:39:25');

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
    `item_key`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '字典项键',
    `item_value`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典项值',
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

SET FOREIGN_KEY_CHECKS = 1;
