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
SET FOREIGN_KEY_CHECKS = 0;

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
    `create_by`                     bigint                                                  NULL DEFAULT NULL COMMENT '创建人',
    `update_by`                     bigint                                                  NULL DEFAULT NULL COMMENT '修改人',
    `create_time`                   datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`                   datetime                                                NULL DEFAULT NULL COMMENT '修改时间',
    `create_name`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '创建人名称',
    `update_name`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT NULL COMMENT '修改人名称',
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
    `scope`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'scope 名称',
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
VALUES (1856524382825115650, 'profile', 'This application will be able to read your profile information.', 1, 123, 123,
        '2024-11-13 10:28:00', '2024-11-13 10:28:00', NULL, NULL);
INSERT INTO `oauth2_scope`
VALUES (1856525468709421058, 'message.read', 'This application will be able to read your message.', 1, 123, 123,
        '2024-11-13 10:32:19', '2024-11-13 10:32:19', NULL, NULL);
INSERT INTO `oauth2_scope`
VALUES (1856525597751377922, 'message.write',
        'This application will be able to add new messages. It will also be able to edit and delete existing messages.',
        1, 123, 123, '2024-11-13 10:32:50', '2024-11-13 10:32:50', NULL, NULL);
INSERT INTO `oauth2_scope`
VALUES (1856525703913406465, 'user.read', 'This application will be able to read your user information.', 1, 123, 123,
        '2024-11-13 10:33:15', '2024-11-13 10:43:58', NULL, NULL);

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
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, 'normal', '普通用户', '普通用户', 0, 1, 1, '云逸', '云逸', '2024-11-25 17:37:41', '2024-11-25 17:37:41');

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
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (1, 1, 1, 1, 1, '云逸', '云逸', '2024-11-25 17:39:25', '2024-11-25 17:39:25');

SET FOREIGN_KEY_CHECKS = 1;
