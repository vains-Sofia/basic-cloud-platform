/*
 Navicat Premium Dump SQL

 Source Server         : home_local
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : cloudflow.top:3306
 Source Schema         : basic-cloud-platform

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 20/04/2025 16:19:32
*/

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
VALUES (1850087647291408385, 'oidc-client', '2024-10-26 16:10:44', '{noop}secret', NULL, '1850229112386650114', NULL,
        '[\"client_secret_basic\"]', NULL, '[\"refresh_token\",\"password\",\"authorization_code\",\"email\"]',
        '[\"http://127.0.0.1:8080/login/oauth2/code/oidc-client\",\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"https://www.baidu.com\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, '2024-10-26 16:10:44', '2024-10-27 01:32:51', NULL, NULL);
INSERT INTO `oauth2_application`
VALUES (1850087647291408386, 'swagger-client', '2024-10-26 16:10:44',
        '{bcrypt}$2a$10$OUB5yvIJuRCxBHPxuF9LF.ynmkjaywREB63gFpL48UiPBGu6lz8Au', NULL, '1850229112386650115', NULL,
        '[\"client_secret_post\"]', NULL, '[\"refresh_token\",\"password\",\"authorization_code\",\"email\"]',
        '[\"http://127.0.0.1:8080/login/oauth2/code/oidc-client\",\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:8080/auth/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"https://www.baidu.com\",\"https://api.cloudflow.top/webjars/swagger-ui/oauth2-redirect.html\",\"https://api.cloudflow.top/swagger-ui/oauth2-redirect.html\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, '2024-10-26 16:10:44', '2024-10-26 17:33:23', NULL, NULL);
INSERT INTO `oauth2_application`
VALUES (1850087647291408387, 'device-messaging-client', '2024-10-26 16:10:44', NULL, NULL, '1850229112386650116', NULL,
        '[\"none\"]', NULL, '[\"refresh_token\",\"urn:ietf:params:oauth:grant-type:device_code\"]', '[]', '[]',
        '[\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, '2024-10-26 16:10:44', '2024-10-27 01:32:51', NULL, NULL);
INSERT INTO `oauth2_application`
VALUES (1850087647291408388, 'messaging-client', '2024-10-26 16:10:44',
        '{bcrypt}$2a$10$YZTLFvdes7jG9wFqrEUoJ.7hnIy5ElXTzwO3QMLqbjANvzIZr2qw2', NULL, '1850229112386650117', NULL,
        '[\"client_secret_basic\"]', NULL,
        '[\"refresh_token\",\"password\",\"authorization_code\",\"email\",\"client_credentials\"]',
        '[\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"http://127.0.0.1:8000/login/oauth2/code/client-oidc\",\"https://www.baidu.com\",\"https://api.cloudflow.top/monitor/login/oauth2/code/messaging-client-oidc\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":null,\"tokenEndpointAuthenticationSigningAlgorithm\":null,\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, NULL, '2024-10-26 16:10:44', '2024-11-06 17:43:50', NULL, NULL);
INSERT INTO `oauth2_application`
VALUES (1850087647291408389, 'private-key-jwt-client', '2024-10-26 16:10:44', '{noop}12345678', NULL,
        '1850229112386650118', NULL, '[\"private_key_jwt\",\"client_secret_basic\"]', NULL,
        '[\"refresh_token\",\"password\",\"authorization_code\",\"email\"]',
        '[\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:8000/login/oauth2/code/private-key-client-oidc\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"https://www.baidu.com\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":\"http://127.0.0.1:8000/jwkSet\",\"tokenEndpointAuthenticationSigningAlgorithm\":\"RS256\",\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, '2024-10-26 16:10:44', '2024-10-27 01:32:51', NULL, NULL);
INSERT INTO `oauth2_application`
VALUES (1850087647291408390, 'pkce-message-client', '2024-10-26 16:10:43', NULL, NULL, 'PKCE流程', NULL, '[\"none\"]',
        NULL, '[\"refresh_token\",\"authorization_code\"]',
        '[\"http://127.0.0.1:5173/PkceRedirect\",\"https://authorization-example.vercel.app/PkceRedirect\",\"https://j1zr8ren8w.51xd.pub/PkceRedirect\",\"https://flow-cloud.love/PkceRedirect\",\"https://admin.cloudflow.top/PkceRedirect\"]',
        '[\"http://127.0.0.1:8080/getCaptcha\"]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":true,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":\"http://127.0.0.1:8000/jwkSet\",\"tokenEndpointAuthenticationSigningAlgorithm\":\"RS256\",\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, '2024-10-26 16:10:43', '2024-10-27 01:32:51', NULL, NULL);
INSERT INTO `oauth2_application`
VALUES (1850087647291408391, 'opaque-client', '2024-10-26 16:10:44', '{noop}123456', NULL, '匿名token', NULL,
        '[\"client_secret_basic\"]', NULL, '[\"refresh_token\",\"client_credentials\",\"authorization_code\"]',
        '[\"https://flow-cloud.love/OAuth2Redirect\",\"https://authorization-example.vercel.app/OAuth2Redirect\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"https://j1zr8ren8w.51xd.pub/OAuth2Redirect\"]',
        '[]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":\"http://127.0.0.1:8000/jwkSet\",\"tokenEndpointAuthenticationSigningAlgorithm\":\"RS256\",\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":7200,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"reference\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":604800,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        123, 123, '2024-10-26 16:10:44', '2024-10-27 01:32:51', NULL, NULL);
INSERT INTO `oauth2_application`
VALUES (1851513255444410369, 'private-key-jwt-client-update', '2024-10-30 06:35:34',
        '{bcrypt}$2a$10$21/A7X6Wff11AXghTCq4febKZgIsI0Q0jIr1lZ8r0iL4WwiclGx22', NULL, 'private-key-jwt测试客户端',
        'logo1', '[\"private_key_jwt\",\"client_secret_basic\"]', NULL,
        '[\"refresh_token\",\"password\",\"authorization_code\",\"email\"]',
        '[\"http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html\",\"http://127.0.0.1:8000/login/oauth2/code/private-key-client-oidc\",\"http://127.0.0.1:5173/OAuth2Redirect\",\"https://www.baidu.com\"]',
        '[\"http://127.0.0.1:8080/\"]', '[\"openid\",\"profile\",\"message.read\",\"message.write\"]',
        '{\"requireProofKey\":false,\"requireAuthorizationConsent\":true,\"jwkSetUrl\":\"http://127.0.0.1:8000/jwkSet\",\"tokenEndpointAuthenticationSigningAlgorithm\":\"RS256\",\"x509CertificateSubjectDN\":null}',
        '{\"authorizationCodeTimeToLive\":300,\"authorizationCodeTimeToLiveUnit\":\"Seconds\",\"accessTokenTimeToLive\":300,\"accessTokenTimeToLiveUnit\":\"Seconds\",\"accessTokenFormat\":\"self-contained\",\"deviceCodeTimeToLive\":300,\"deviceCodeTimeToLiveUnit\":\"Seconds\",\"reuseRefreshTokens\":true,\"refreshTokenTimeToLive\":3600,\"refreshTokenTimeToLiveUnit\":\"Seconds\",\"idTokenSignatureAlgorithm\":\"RS256\",\"x509CertificateBoundAccessTokens\":false}',
        NULL, 123, '2024-10-30 06:35:34', '2024-11-06 14:03:41', NULL, NULL);

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
-- Records of oauth2_authorization_consent
-- ----------------------------
INSERT INTO `oauth2_authorization_consent`
VALUES (1913867302081761282, '1850087647291408388', '云逸',
        '[\"SCOPE_openid\",\"SCOPE_message.read\",\"SCOPE_message.write\"]', 1, 1, '2025-04-20 16:08:17',
        '2025-04-20 16:08:17', '云逸', '云逸');

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
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '修改时间',
    `create_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '修改人名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'oauth2客户端的scope'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_scope
-- ----------------------------
INSERT INTO `oauth2_scope`
VALUES (1856524382825115650, '', 'profile', 'This application will be able to read your profile information.', 1, 123,
        123, '2024-11-13 10:28:00', '2024-11-13 10:28:00', NULL, NULL);
INSERT INTO `oauth2_scope`
VALUES (1856525468709421058, '', 'message.read', 'This application will be able to read your message.', 1, 123, 123,
        '2024-11-13 10:32:19', '2024-11-13 10:32:19', NULL, NULL);
INSERT INTO `oauth2_scope`
VALUES (1856525597751377922, '', 'message.write',
        'This application will be able to add new messages. It will also be able to edit and delete existing messages.',
        1, 123, 123, '2024-11-13 10:32:50', '2024-11-13 10:32:50', NULL, NULL);
INSERT INTO `oauth2_scope`
VALUES (1856525703913406465, '', 'user.read', 'This application will be able to read your user information.', 1, 123,
        123, '2024-11-13 10:33:15', '2024-11-13 10:43:58', NULL, NULL);

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
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_scope_permission
-- ----------------------------
INSERT INTO `oauth2_scope_permission`
VALUES (1870115747740577793, 'message.read', 1870104226282090498, 1, 1, '云逸', '云逸', '2024-12-20 22:35:14',
        '2024-12-20 22:35:14');

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
    `id`                  bigint     NOT NULL COMMENT '主键id',
    `title`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的locales文件夹下对应添加）',
    `name`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限名',
    `permission`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  DEFAULT NULL COMMENT '权限码',
    `path`                varchar(100) COLLATE utf8mb4_bin                       DEFAULT NULL COMMENT '路径',
    `request_method`      varchar(10) COLLATE utf8mb4_bin                        DEFAULT NULL COMMENT 'HTTP请求方式',
    `permission_type`     tinyint(1) NOT NULL COMMENT '0:菜单,1:接口,2:其它',
    `module_name`         varchar(100) COLLATE utf8mb4_bin                       DEFAULT NULL COMMENT '所属模块名字',
    `description`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
    `need_authentication` tinyint(1)                                             DEFAULT '0' COMMENT '是否需要鉴权',
    `parent_id`           bigint                                                 DEFAULT '0' COMMENT '父节点id',
    `component`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组件路径',
    `redirect`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '路由重定向',
    `deleted`             tinyint(1)                                             DEFAULT NULL COMMENT '是否已删除',
    `icon`                varchar(100) COLLATE utf8mb4_bin                       DEFAULT NULL COMMENT '菜单图标',
    `extra_icon`          varchar(100) COLLATE utf8mb4_bin                       DEFAULT NULL COMMENT '右侧图标',
    `enter_transition`    varchar(100) COLLATE utf8mb4_bin                       DEFAULT NULL COMMENT '进场动画（页面加载动画）',
    `leave_transition`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '离场动画（页面加载动画）',
    `frame_src`           varchar(1000) COLLATE utf8mb4_bin                      DEFAULT NULL COMMENT '链接地址（需要内嵌的iframe链接地址）',
    `frame_loading`       varchar(100) COLLATE utf8mb4_bin                       DEFAULT NULL COMMENT '加载动画（内嵌的iframe页面是否开启首次加载动画）',
    `keep_alive`          tinyint(1)                                             DEFAULT NULL COMMENT '缓存页面（是否缓存该路由页面，开启后会保存该页面的整体状态，刷新后会清空状态）',
    `show_link`           tinyint(1)                                             DEFAULT NULL COMMENT '是否显示该菜单',
    `hidden_tag`          tinyint(1)                                             DEFAULT NULL COMMENT '隐藏标签页（当前菜单名称或自定义信息禁止添加到标签页）',
    `fixed_tag`           tinyint(1)                                             DEFAULT NULL COMMENT '固定标签页（当前菜单名称是否固定显示在标签页且不可关闭）',
    `show_parent`         tinyint(1)                                             DEFAULT NULL COMMENT '是否显示父级菜单',
    `rank`                decimal(10, 0)                                         DEFAULT NULL COMMENT '菜单排序',
    `active_path`         varchar(100) COLLATE utf8mb4_bin                       DEFAULT NULL COMMENT '指定激活菜单即可获得高亮，`activePath`为指定激活菜单的`path`',
    `create_by`           bigint                                                 DEFAULT NULL COMMENT '创建人',
    `update_by`           bigint                                                 DEFAULT NULL COMMENT '修改人',
    `create_name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  DEFAULT NULL COMMENT '创建人名称',
    `update_name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  DEFAULT NULL COMMENT '修改人名称',
    `create_time`         datetime                                               DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime                                               DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='RBAC权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1, '测试接口', '测试接口', 'test:test01', '/test/test01', 'GET', 3, NULL, NULL, 0, 1952661871538835457, '', '',
        0, 'ep:bell', 'ep:bell', '', '', '', '', 0, 1, 0, 0, 0, 1, NULL, 1, 1, '云逸', '云逸', '2024-11-25 17:38:48',
        '2025-08-06 13:51:44');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1864241504623833089, '修改权限信息', '修改权限信息', 'permission:updatePermission',
        '/permission/updatePermission', 'PUT', 3, '', NULL, 1, 1919661224031989762, '', '', 0, '', 'ep:bell', '', '',
        '', '', 0, 1, 0, 0, 0, 8, NULL, 1, 1, '云逸', '云逸', '2024-12-04 17:35:50', '2025-08-06 13:49:35');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1870104848520814594, '查询权限详情', '查询权限详情', 'permission:permissionDetails',
        '/permission/permissionDetails/{id}', 'GET', 3, NULL, NULL, 1, 1919661224031989762, '', '', 1, 'ep:bell',
        'ep:bell', '', '', '', 'true', 0, 1, 0, 0, 0, 8, NULL, 1, 1, '云逸', '云逸', '2024-12-20 21:51:55',
        '2025-08-06 13:45:14');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1919658873598554113, 'menus.pureSysManagement', 'System', 'menu:system', '/system', NULL, 0, NULL, NULL, NULL,
        0, '', '', 0, 'ri:settings-3-line', '', '', '', '', '', 0, 1, 0, 0, 1, 1, NULL, 1, 1, '云逸', '云逸',
        '2025-05-06 15:41:56', '2025-08-06 12:54:31');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1919660730064613377, 'menus.pureUser', 'SystemUser', 'menu:system:user', '/system/user/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/user/index', '', 0, 'ri:admin-line', '', '', '', '', '', 0, 1, 0, 0,
        1, 1, NULL, 1, 1, '云逸', '云逸', '2025-05-06 15:49:18', '2025-08-06 13:49:23');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1919660995832492033, 'menus.pureRole', 'SystemRole', 'menu:system:role', '/system/role/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/role/index', '', 0, 'ri:admin-fill', '', '', '', '', '', 0, 1, 0, 0,
        1, 2, NULL, 1, 1, '云逸', '云逸', '2025-05-06 15:50:22', '2025-08-06 13:49:23');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1919661224031989762, 'menus.pureSystemMenu', 'SystemMenu', 'menu:system:menu', '/system/menu/index', NULL, 0,
        NULL, NULL, NULL, 1919658873598554113, '/system/menu/index', '', 0, 'ep:menu', '', '', '', '', '', 0, 1, 0, 0,
        1, 3, NULL, 1, 1, '云逸', '云逸', '2025-05-06 15:51:16', '2025-08-06 13:49:23');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1919661595269836801, 'menus.pureDept', 'SystemDept', 'menu:system:dept', '/system/dept/index', NULL, 0, NULL,
        NULL, NULL, 1919658873598554113, '/system/dept/index', '', 1, 'ri:git-branch-line', '', '', '', '', '', 0, 1, 0,
        0, 1, 4, NULL, 1, 1, '云逸', '云逸', '2025-05-06 15:52:45', '2025-05-06 15:59:16');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952306933212164097, 'menus.pureSystemDict', 'SystemDict', 'system:dict:index', '/system/dict/index', '', 0,
        'system', NULL, 1, 1919658873598554113, '/system/dict/index', '', 0, 'ri:book-2-line', '', '', '', '', '', 1, 1,
        0, 0, 0, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-04 17:53:40', '2025-08-06 13:49:23');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952647718126247937, 'menus.pureApiScan', 'pureApiScan', 'system:api-scan:index', '/system/api-scan/index', '',
        0, 'system', NULL, 1, 1919658873598554113, '/system/api-scan/index', '', 0, 'ri:scan-2-line', '', '', '', '',
        '', 0, 1, 0, 0, 0, 5, NULL, 1, 1, '云逸', '云逸', '2025-08-05 16:27:49', '2025-08-12 13:41:59');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952657129544638465, 'menus.platformManagement', 'platform', 'platform', '/platform', '', 0, 'system', NULL, 1,
        0, '/platform', '', 0, 'ri:planet-line', '', '', '', '', '', 0, 1, 0, 0, 0, 2, NULL, 1, 1, '云逸', '云逸',
        '2025-08-05 17:05:13', '2025-08-06 13:43:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952657433640067074, 'menus.application', 'Application', 'platform:application:index',
        '/platform/application/index', '', 0, 'auth', NULL, 1, 1952657129544638465, '/platform/application/index', '',
        0, 'ri:apps-line', '', '', '', '', '', 0, 1, 0, 0, 0, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-05 17:06:25',
        '2025-08-12 13:41:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952657633259577345, 'menus.scopeManagement', 'PlatformScope', 'platform:scope:index', '/platform/scope/index',
        '', 0, '', NULL, 1, 1952657129544638465, '/platform/scope/index', '', 0, 'ep:connection', '', '', '', '', '', 0,
        1, 0, 0, 0, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-05 17:07:13', '2025-08-12 13:41:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952657828596703234, 'menus.authorization', 'Authorization', ':platform:authorization:index',
        '/platform/authorization/index', '', 0, '', NULL, 1, 1952657129544638465, '/platform/authorization/index', '',
        0, 'ri:login-circle-line', '', '', '', '', '', 0, 1, 0, 0, 0, 3, NULL, 1, 1, '云逸', '云逸',
        '2025-08-05 17:08:00', '2025-08-12 13:41:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952658080221388801, 'menus.applicationDetail', 'ApplicationDetails', 'platform:application:detail',
        '/platform/application/detail', '', 0, '', NULL, 1, 1952657129544638465, '/platform/application/detail', '', 1,
        'ep:help', '', '', '', '', '', 0, 0, 0, 0, 1, 1, '', 1, 1, '云逸', '云逸', '2025-08-05 17:09:00',
        '2025-08-12 13:38:18');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952658732645376001, 'menus.pureApiEndpoints', 'ApiEndpoints', 'system:api-scan:endpoints',
        '/system/api-scan/endpoints', '', 0, 'system', NULL, 1, 1919658873598554113, '/system/api-scan/endpoints', '',
        0, 'fa-solid:cubes', '', '', '', '', '', 0, 0, 0, 0, 0, 6, '/system/api-scan/index', 1, 1, '云逸', '云逸',
        '2025-08-05 17:11:35', '2025-08-12 13:42:41');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952661871538835457, '测试接口', 'test', 'test', '/test', '', 0, '', NULL, 1, 0, '/test', '', 0,
        'ri:test-tube-line', '', '', '', '', '', 0, 0, 0, 0, 0, 3, '', 1, 1, '云逸', '云逸', '2025-08-05 17:24:04',
        '2025-08-12 13:36:17');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128257, '修改scope信息', '修改scope信息', 'scope:update', '/scope/update', 'PUT', 3, 'auth', NULL,
        0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6,
        NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:52:24');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128258, '重置scope对应的权限', '重置scope对应的权限', 'scope:resetScopePermission',
        '/scope/resetScopePermission', 'PUT', 3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:49:02');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128259, '更新客户端信息', '更新客户端信息', 'application:update', '/application/update', 'PUT', 3,
        'auth', NULL, 0, 1955142007110266881, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-12 13:39:51');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128260, '测试Json请求验证', '测试Json请求验证', 'test:validateJson', '/test/validateJson', 'POST',
        3, 'auth', NULL, 0, 1952661871538835457, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:44');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128261, '测试表单验证', '测试表单验证', 'test:validateForm', '/test/validateForm', 'POST', 3,
        'auth', NULL, 0, 1952661871538835457, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:44');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128262, '保存scope信息', '保存scope信息', 'scope:save', '/scope/save', 'POST', 3, 'auth', NULL, 0,
        1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL,
        1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:49:46');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128263, '保存客户端信息', '保存客户端信息', 'application:save', '/application/save', 'POST', 3,
        'auth', NULL, 0, 1952657433640067074, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-12 13:36:43');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128264, '测试手机号验证', '测试手机号验证', 'test:validatePhone', '/test/validatePhone', 'GET', 3,
        'auth', NULL, 0, 1952661871538835457, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:44');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128265, '查询所有的scope', '查询所有的scope', 'scope:findScopeList', '/scope/findScopeList', 'GET',
        3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:47:34');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128266, '查询scope对应的权限', '查询scope对应的权限', 'scope:findPermissionIdsByScope',
        '/scope/findPermissionIdsByScope/*', 'GET', 3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:48:15');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128267, '根据入参分页查询scope信息', '根据入参分页查询scope信息', 'scope:findByPage',
        '/scope/findByPage', 'GET', 3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:47:26');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128268, '根据入参分页查询认证信息', '根据入参分页查询认证信息', 'authorization:findByPage',
        '/authorization/findByPage', 'GET', 3, 'auth', NULL, 0, 1952657828596703234, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:52:32');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128269, '根据id查询认证信息详情', '根据id查询认证信息详情', 'authorization:findById',
        '/authorization/findById/*', 'GET', 3, 'auth', NULL, 0, 1952657828596703234, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:52:32');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128270, '根据入参分页查询客户端信息', '根据入参分页查询客户端信息', 'application:findByPage',
        '/application/findByPage', 'GET', 3, 'auth', NULL, 0, 1952657433640067074, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-12 13:36:43');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128271, '根据id查询客户端信息', '根据id查询客户端信息', 'application:findById',
        '/application/findById/*', 'GET', 3, 'auth', NULL, 0, 1955142007110266881, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-12 13:39:51');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128272, '根据客户端id查询客户端信息', '根据客户端id查询客户端信息', 'application:findByClientId',
        '/application/findByClientId/*', 'GET', 3, 'auth', NULL, 0, 1955142007110266881, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-12 13:39:51');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128273, '分页查询应用卡片列表数据', '分页查询应用卡片列表数据', 'application:cardListPage',
        '/application/cardListPage', 'GET', 3, 'auth', NULL, 0, 1952657433640067074, '', '', 0, '', '', '', '', '', '',
        0, 1, 0, 0, 0, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-12 13:36:43');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128274, '根据id删除scope', '根据id删除scope', 'scope:removeById', '/scope/removeById/*', 'DELETE',
        3, 'auth', NULL, 0, 1952657633259577345, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 7, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:52:24');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128275, '踢出登录', '踢出登录', 'authorization:offline', '/authorization/offline', 'DELETE', 3,
        'auth', NULL, 0, 1952657828596703234, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:52:32');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128276, '根据客户端id删除客户端', '根据客户端id删除客户端', 'application:remove',
        '/application/remove/*', 'DELETE', 3, 'auth', NULL, 0, 1952657433640067074, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-12 13:39:49');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128277, '更新用户角色', '更新用户角色', 'user:updateUserRoles', '/user/updateUserRoles', 'PUT', 3,
        'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 8, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:52:11');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128278, '修改用户信息', '修改用户信息', 'user:updateBasicUser', '/user/updateBasicUser', 'PUT', 3,
        'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 5, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:52:02');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128279, '重置密码', '重置密码', 'user:resetPassword', '/user/resetPassword', 'PUT', 3, 'system',
        NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        7, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:52:11');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128280, '修改角色信息', '修改角色信息', 'role:updateRole', '/role/updateRole', 'PUT', 3, 'system',
        NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        5, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:40');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128281, '更新角色权限', '更新角色权限', 'role:updateRolePermissions',
        '/role/updateRolePermissions', 'PUT', 3, 'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:51:40');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128282, '批量修改权限信息', '批量修改权限信息', 'permission:batchUpdatePermissions',
        '/permission/batchUpdatePermissions', 'PUT', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9, NULL, 1, 1, '云逸', '云逸',
        '2025-08-06 12:54:17', '2025-08-06 13:49:35');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128283, '根据字典类型ID查询字典类型', '根据字典类型ID查询字典类型', 'dict:type', '/dict/type/*',
        'GET', 3, 'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128284, '更新字典类型', '更新字典类型', 'dict:type', '/dict/type/*', 'PUT', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9, NULL,
        1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128285, '删除字典类型', '删除字典类型', 'dict:type', '/dict/type/*', 'DELETE', 3, 'system', NULL,
        0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 11,
        NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128286, '根据字典项ID查询字典项', '根据字典项ID查询字典项', 'dict:item', '/dict/item/*', 'GET', 3,
        'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 5, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128287, '更新字典项', '更新字典项', 'dict:item', '/dict/item/*', 'PUT', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 10, NULL,
        1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128288, '删除字典项', '删除字典项', 'dict:item', '/dict/item/*', 'DELETE', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 12, NULL,
        1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128289, '添加API接口信息', '添加API接口信息', 'api-endpoint', '/api-endpoint', 'POST', 3, 'system',
        NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        5, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:45:06');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128290, '修改API接口信息', '修改API接口信息', 'api-endpoint', '/api-endpoint', 'PUT', 3, 'system',
        NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        9, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128291, '设置接口状态为忽略', '设置接口状态为忽略', 'api-endpoint:ignore', '/api-endpoint/ignore',
        'PUT', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 10, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128292, '用户注册', '用户注册', 'user:userRegister', '/user/userRegister', 'POST', 3, 'system',
        NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, NULL, 1, 1,
        '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:40:19');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128293, '添加一条用户信息', '添加一条用户信息', 'user:insertBasicUser', '/user/insertBasicUser',
        'POST', 3, 'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:41:16');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128294, '添加角色信息', '添加角色信息', 'role:insertRole', '/role/insertRole', 'POST', 3, 'system',
        NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:43:43');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128295, '添加权限信息', '添加权限信息', 'permission:insertPermission',
        '/permission/insertPermission', 'POST', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:44:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128296, '获取没有父节点的权限id列表', '获取没有父节点的权限id列表',
        'permission:findNonParentPermissions', '/permission/findNonParentPermissions', 'POST', 3, 'system', NULL, 0,
        1919661224031989762, '', '', 0, '', '', '', '', '', '', 0, 1, 0, 0, 0, 4, NULL, 1, 1, '云逸', '云逸',
        '2025-08-06 12:54:17', '2025-08-06 13:44:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128297, '创建字典类型', '创建字典类型', 'dict:type', '/dict/type', 'POST', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL,
        1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128298, '创建字典项', '创建字典项', 'dict:item', '/dict/item', 'POST', 3, 'system', NULL, 0,
        1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, NULL,
        1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128299, '扫描系统接口', '扫描系统接口', 'api-endpoint:scan:endpoints',
        '/api-endpoint/scan/endpoints', 'POST', 3, 'system', NULL, 0, 1952647718126247937, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-12 13:41:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128300, '查询API接口信息列表', '查询API接口信息列表', 'api-endpoint:list', '/api-endpoint/list',
        'POST', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:42:51');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128301, '批量导入接口到权限表', '批量导入接口到权限表', 'api-endpoint:import',
        '/api-endpoint/import', 'POST', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:50:40');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128302, '根据扫描批次ID导入接口', '根据扫描批次ID导入接口', 'api-endpoint:import:batch',
        '/api-endpoint/import/batch/*', 'POST', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:45:06');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128303, '查询用户详情', '查询用户详情', 'user:userDetails', '/user/userDetails/*', 'GET', 3,
        'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:37:59');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128304, '获取登录用户信息', '获取登录用户信息', 'user:loginUserinfo', '/user/loginUserinfo', 'GET',
        3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5,
        NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:37:36');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128305, '获取注册时使用的邮箱验证码', '获取注册时使用的邮箱验证码', 'user:getRegisterEmailCode',
        '/user/getRegisterEmailCode/*', 'GET', 3, 'system', NULL, 0, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:37:36');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128306, '分页查询基础用户信息列表', '分页查询基础用户信息列表', 'user:findByPage',
        '/user/findByPage', 'GET', 3, 'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:37:36');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128307, '查询角色详情', '查询角色详情', 'role:roleDetails', '/role/roleDetails/*', 'GET', 3,
        'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:43:43');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128308, '根据条件查询所有角色列表', '根据条件查询所有角色列表', 'role:findRoles',
        '/role/findRoles', 'GET', 3, 'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:43:43');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128309, '根据用户id查询角色id列表', '根据用户id查询角色id列表', 'role:findRoleIdsByUserId',
        '/role/findRoleIdsByUserId/*', 'GET', 3, 'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:40:21');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128310, '分页查询角色信息列表', '分页查询角色信息列表', 'role:findByPage', '/role/findByPage',
        'GET', 3, 'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:43:43');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128311, '查询权限详情', '查询权限详情', 'permission:permissionDetails',
        '/permission/permissionDetails/*', 'GET', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:45:14');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128312, '查询权限信息列表', '查询权限信息列表', 'permission:findPermissions',
        '/permission/findPermissions', 'GET', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:44:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128313, '根据角色id查询权限id列表', '根据角色id查询权限id列表',
        'permission:findPermissionIdsByRoleId', '/permission/findPermissionIdsByRoleId/*', 'GET', 3, 'system', NULL, 0,
        1919661224031989762, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL,
        1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:44:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128314, '分页查询权限信息列表', '分页查询权限信息列表', 'permission:findByPage',
        '/permission/findByPage', 'GET', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:44:34');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128315, '查询所有字典类型', '查询所有字典类型', 'dict:type:page', '/dict/type/page', 'GET', 3,
        'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:44:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128316, '查询所有字典项', '查询所有字典项', 'dict:type:all', '/dict/type/all', 'GET', 3, 'system',
        NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        7, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:09');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128317, '根据字典类型编码查询字典项', '根据字典类型编码查询字典项', 'dict:item:type',
        '/dict/item/type/*', 'GET', 3, 'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:44:07');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128318, '分页查询字典项', '分页查询字典项', 'dict:item:page', '/dict/item/page', 'GET', 3,
        'system', NULL, 0, 1952306933212164097, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:43:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128319, '查询API扫描记录详情', '查询API扫描记录详情', 'api-scan', '/api-scan/*', 'GET', 3,
        'system', NULL, 0, 1952647718126247937, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-12 13:41:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128320, '删除API扫描记录信息', '删除API扫描记录信息', 'api-scan', '/api-scan/*', 'DELETE', 3,
        'system', NULL, 0, 1952647718126247937, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 4, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-12 13:41:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128321, '分页查询API扫描记录列表', '分页查询API扫描记录列表', 'api-scan:page', '/api-scan/page',
        'GET', 3, 'system', NULL, 0, 1952647718126247937, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-12 13:41:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128322, '查询API接口详情', '查询API接口详情', 'api-endpoint', '/api-endpoint/*', 'GET', 3,
        'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 2, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:42:51');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128323, '删除API接口信息', '删除API接口信息', 'api-endpoint', '/api-endpoint/*', 'DELETE', 3,
        'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 6, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:45:06');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128324, '分页查询API接口列表', '分页查询API接口列表', 'api-endpoint:page', '/api-endpoint/page',
        'GET', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 1, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:42:30');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128325, '根据扫描批次ID查询接口列表', '根据扫描批次ID查询接口列表', 'api-endpoint:batch',
        '/api-endpoint/batch/*', 'GET', 3, 'system', NULL, 0, 1952658732645376001, NULL, NULL, 0, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17',
        '2025-08-06 13:42:51');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128326, '删除用户信息', '删除用户信息', 'user:removeById', '/user/removeById/*', 'DELETE', 3,
        'system', NULL, 0, 1919660730064613377, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 6, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:52:02');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128327, '删除角色信息', '删除角色信息', 'role:removeById', '/role/removeById/*', 'DELETE', 3,
        'system', NULL, 0, 1919660995832492033, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, 6, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:51:40');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1952956369548128328, '删除权限信息', '删除权限信息', 'permission:removeById', '/permission/removeById/*',
        'DELETE', 3, 'system', NULL, 0, 1919661224031989762, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 6, NULL, 1, 1, '云逸', '云逸', '2025-08-06 12:54:17', '2025-08-06 13:44:50');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1955142007110266881, 'menus.applicationDetail', 'ApplicationDetails', 'platform:application:detail',
        '/platform/application/detail', '', 0, 'auth', NULL, 1, 1952657129544638465, '/platform/application/detail', '',
        0, 'ep:help', '', '', '', '', '', 0, 0, 0, 0, 1, 4, '/platform/application/index', 1, 1, '云逸', '云逸',
        '2025-08-12 13:39:14', '2025-08-12 13:41:55');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1955143359093829633, 'menus.swaggerUi', 'SwaggerUi', 'SwaggerUi', '/swagger-ui', '', 1, NULL, NULL, 1, 0, '',
        '', 0, 'ri:book-open-line', '', '', '', 'https://api.cloudflow.top/swagger-ui/index.html', 'true', 0, 1, 0, 0,
        0, 4, '', 1, 1, '云逸', '云逸', '2025-08-12 13:44:36', '2025-08-12 13:44:36');
INSERT INTO sys_permission (id, title, name, permission, `path`, request_method, permission_type,
                            module_name, description, need_authentication, parent_id, component,
                            redirect, deleted, icon, extra_icon, enter_transition,
                            leave_transition, frame_src, frame_loading, keep_alive, show_link,
                            hidden_tag, fixed_tag, show_parent, `rank`, active_path, create_by,
                            update_by, create_name, update_name, create_time, update_time)
VALUES (1955143568150523905, 'menus.monitor', 'Monitor', 'monitor', '/monitor', '', 1, '', NULL, 1, 0, '', '', 0,
        'ep:monitor', '', '', '', 'https://api.cloudflow.top/monitor/', 'true', 0, 1, 0, 0, 0, 5, '', 1, 1, '云逸',
        '云逸', '2025-08-12 13:45:26', '2025-08-12 13:46:39');

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
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1921563497926279169, 1, 1, 1, 1, '云逸', '云逸', '2025-05-11 21:50:13', '2025-05-11 21:50:13');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1921563497926279170, 1, 1870104848520814594, 1, 1, '云逸', '云逸', '2025-05-11 21:50:14',
        '2025-05-11 21:50:14');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1921563497926279171, 1, 1864241504623833089, 1, 1, '云逸', '云逸', '2025-05-11 21:50:14',
        '2025-05-11 21:50:14');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1921563497926279172, 1, 1919661224031989762, 1, 1, '云逸', '云逸', '2025-05-11 21:50:14',
        '2025-05-11 21:50:14');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1921563497926279173, 1, 1919658873598554113, 1, 1, '云逸', '云逸', '2025-05-11 21:50:14',
        '2025-05-11 21:50:14');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1922107705540333569, 1922107622090461185, 1, 1, 1, '云逸', '云逸', '2025-05-13 09:52:43',
        '2025-05-13 09:52:43');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1922107705540333570, 1922107622090461185, 1864241504623833089, 1, 1, '云逸', '云逸', '2025-05-13 09:52:43',
        '2025-05-13 09:52:43');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1922107705540333571, 1922107622090461185, 1870104848520814594, 1, 1, '云逸', '云逸', '2025-05-13 09:52:43',
        '2025-05-13 09:52:43');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1922107705540333572, 1922107622090461185, 1919658873598554113, 1, 1, '云逸', '云逸', '2025-05-13 09:52:43',
        '2025-05-13 09:52:43');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1922107705540333573, 1922107622090461185, 1919660730064613377, 1, 1, '云逸', '云逸', '2025-05-13 09:52:43',
        '2025-05-13 09:52:43');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1922107705540333574, 1922107622090461185, 1919660995832492033, 1, 1, '云逸', '云逸', '2025-05-13 09:52:43',
        '2025-05-13 09:52:43');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1922107705540333575, 1922107622090461185, 1919661224031989762, 1, 1, '云逸', '云逸', '2025-05-13 09:52:43',
        '2025-05-13 09:52:43');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840322, 1952971355100065793, 1, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840323, 1952971355100065793, 1952956369548128324, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840324, 1952971355100065793, 1952956369548128306, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840325, 1952971355100065793, 1952956369548128275, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840326, 1952971355100065793, 1952956369548128273, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840327, 1952971355100065793, 1952956369548128267, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840328, 1952971355100065793, 1952956369548128310, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840329, 1952971355100065793, 1952956369548128259, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840330, 1952971355100065793, 1952956369548128314, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840331, 1952971355100065793, 1952956369548128322, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840332, 1952971355100065793, 1952956369548128325, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840333, 1952971355100065793, 1952956369548128300, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840334, 1952971355100065793, 1952956369548128289, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840335, 1952971355100065793, 1952956369548128323, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840336, 1952971355100065793, 1952956369548128302, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840337, 1952971355100065793, 1952956369548128301, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840338, 1952971355100065793, 1952956369548128290, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840339, 1952971355100065793, 1952956369548128291, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840340, 1952971355100065793, 1952956369548128303, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840341, 1952971355100065793, 1952956369548128309, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840342, 1952971355100065793, 1952956369548128293, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840343, 1952971355100065793, 1952956369548128278, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840344, 1952971355100065793, 1952956369548128326, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840345, 1952971355100065793, 1952956369548128279, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840346, 1952971355100065793, 1952956369548128277, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840347, 1952971355100065793, 1952956369548128307, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840348, 1952971355100065793, 1952956369548128308, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840349, 1952971355100065793, 1952956369548128294, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840350, 1952971355100065793, 1952956369548128280, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840351, 1952971355100065793, 1952956369548128327, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840352, 1952971355100065793, 1952956369548128281, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840353, 1952971355100065793, 1952956369548128313, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840354, 1952971355100065793, 1952956369548128312, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840355, 1952971355100065793, 1952956369548128296, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840356, 1952971355100065793, 1952956369548128295, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840357, 1952971355100065793, 1952956369548128328, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840358, 1952971355100065793, 1952956369548128311, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840359, 1952971355100065793, 1864241504623833089, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840360, 1952971355100065793, 1952956369548128282, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840361, 1952971355100065793, 1952956369548128318, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840362, 1952971355100065793, 1952956369548128317, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840363, 1952971355100065793, 1952956369548128315, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840364, 1952971355100065793, 1952956369548128283, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840365, 1952971355100065793, 1952956369548128286, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840366, 1952971355100065793, 1952956369548128297, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840367, 1952971355100065793, 1952956369548128316, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840368, 1952971355100065793, 1952956369548128298, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840369, 1952971355100065793, 1952956369548128284, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840370, 1952971355100065793, 1952956369548128287, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840371, 1952971355100065793, 1952956369548128285, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840372, 1952971355100065793, 1952956369548128288, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840373, 1952971355100065793, 1952956369548128299, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840374, 1952971355100065793, 1952956369548128321, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840375, 1952971355100065793, 1952956369548128319, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840376, 1952971355100065793, 1952956369548128320, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840377, 1952971355100065793, 1952956369548128272, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840378, 1952971355100065793, 1952956369548128270, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840379, 1952971355100065793, 1952956369548128268, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840380, 1952971355100065793, 1952956369548128265, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840381, 1952971355100065793, 1952956369548128261, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840382, 1952971355100065793, 1952956369548128263, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840383, 1952971355100065793, 1952956369548128276, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840384, 1952971355100065793, 1952956369548128266, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840385, 1952971355100065793, 1952956369548128258, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840386, 1952971355100065793, 1952956369548128262, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840387, 1952971355100065793, 1952956369548128257, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840388, 1952971355100065793, 1952956369548128274, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840389, 1952971355100065793, 1952956369548128269, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840390, 1952971355100065793, 1952956369548128271, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840391, 1952971355100065793, 1952956369548128260, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840392, 1952971355100065793, 1952956369548128264, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840393, 1952971355100065793, 1919660730064613377, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840394, 1952971355100065793, 1919660995832492033, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840395, 1952971355100065793, 1919661224031989762, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840396, 1952971355100065793, 1952306933212164097, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840397, 1952971355100065793, 1952647718126247937, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840398, 1952971355100065793, 1952658732645376001, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840399, 1952971355100065793, 1952657433640067074, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840400, 1952971355100065793, 1952657633259577345, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840401, 1952971355100065793, 1952657828596703234, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840402, 1952971355100065793, 1955142007110266881, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840403, 1952971355100065793, 1919658873598554113, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840404, 1952971355100065793, 1952657129544638465, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840405, 1952971355100065793, 1952661871538835457, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840406, 1952971355100065793, 1955143359093829633, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');
INSERT INTO sys_role_permission (id, role_id, permission_id, create_by, update_by, create_name, update_name,
                                 create_time, update_time)
VALUES (1955143593542840407, 1952971355100065793, 1955143568150523905, 1, 1, '云逸', '云逸', '2025-08-12 13:45:32',
        '2025-08-12 13:45:32');

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
INSERT INTO sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                           create_time, update_time)
VALUES (1921489309037510658, 1, 1862332106783637506, 1, 1, '云逸', '云逸', '2025-05-11 16:55:26',
        '2025-05-11 16:55:26');
INSERT INTO sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                           create_time, update_time)
VALUES (1921489309037510659, 1864508890354565121, 1862332106783637506, 1, 1, '云逸', '云逸', '2025-05-11 16:55:26',
        '2025-05-11 16:55:26');
INSERT INTO sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                           create_time, update_time)
VALUES (1923297772209012737, 1, 1920404587435859970, 1, 1, '云逸', '云逸', '2025-05-16 16:41:37',
        '2025-05-16 16:41:37');
INSERT INTO sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                           create_time, update_time)
VALUES (1923297772209012738, 1864511627943235585, 1920404587435859970, 1, 1, '云逸', '云逸', '2025-05-16 16:41:37',
        '2025-05-16 16:41:37');
INSERT INTO sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
                           create_time, update_time)
VALUES (1948586077352964098, 1, 1, 1, 1, '云逸', '云逸', '2025-07-25 11:28:18', '2025-07-25 11:28:18');
INSERT INTO sys_user_role (id, role_id, user_id, create_by, update_by, create_name, update_name,
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
