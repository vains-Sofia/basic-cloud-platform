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
    `client_secret`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户端秘钥',
    `client_secret_expires_at`      timestamp NULL DEFAULT NULL COMMENT '客户端秘钥过期时间',
    `client_name`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '客户端名称',
    `client_logo`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户端logo',
    `client_authentication_methods` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端支持的认证方式',
    `authorization_grant_types`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端支持的授权模式',
    `redirect_uris`                 varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '回调地址',
    `post_logout_redirect_uris`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'Open Connection登出后跳转地址',
    `scopes`                        varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端拥有的scope',
    `client_settings`               varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端设置',
    `token_settings`                varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端生成token设置',
    `create_by`                     bigint NULL DEFAULT NULL COMMENT '创建人',
    `update_by`                     bigint NULL DEFAULT NULL COMMENT '修改人',
    `create_time`                   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`                   datetime NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth2_authorization
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization`
(
    `id`                            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '数据id',
    `registered_client_id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'oauth2授权登录时使用的客户端id',
    `principal_name`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'oauth2授权登录时授权用户名',
    `authorization_grant_type`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'oauth2授权登录时使用的授权模式',
    `authorized_scopes`             varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'oauth2授权登录时申请的权限',
    `attributes`                    text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'oauth2授权登录属性',
    `state`                         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'oauth2授权登录时的state',
    `authorization_code_value`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '授权码的值',
    `authorization_code_issued_at`  timestamp NULL DEFAULT NULL COMMENT '授权码签发时间',
    `authorization_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '授权码过期时间',
    `authorization_code_metadata`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '授权码元数据',
    `access_token_value`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'access token的值',
    `access_token_issued_at`        timestamp NULL DEFAULT NULL COMMENT 'access token签发时间',
    `access_token_expires_at`       timestamp NULL DEFAULT NULL COMMENT 'access token过期时间',
    `access_token_metadata`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'access token元数据',
    `access_token_type`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'access token类型(一般是bearer)',
    `access_token_scopes`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'access token中包含的scope',
    `refresh_token_value`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '认证后签发的 refresh token',
    `refresh_token_issued_at`       timestamp NULL DEFAULT NULL COMMENT 'refresh token 签发时间',
    `refresh_token_expires_at`      timestamp NULL DEFAULT NULL COMMENT 'refresh token 过期时间',
    `refresh_token_metadata`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'refresh token 元数据',
    `oidc_id_token_value`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '认证后签发的 oidc id token',
    `oidc_id_token_issued_at`       timestamp NULL DEFAULT NULL COMMENT 'oidc id token 签发时间',
    `oidc_id_token_expires_at`      timestamp NULL DEFAULT NULL COMMENT 'oidc id token 过期时间',
    `oidc_id_token_metadata`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'oidc id token 元数据',
    `oidc_id_token_claims`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'oidc id token 声明(Claims)信息(一般情况下是用户数据)',
    `user_code_value`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '设备码模式(Device Flow)中的 user code',
    `user_code_issued_at`           timestamp NULL DEFAULT NULL COMMENT 'user code 签发时间',
    `user_code_expires_at`          timestamp NULL DEFAULT NULL COMMENT 'user code 过期时间',
    `user_code_metadata`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'user code 元数据',
    `device_code_value`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '设备码模式(Device Flow)中的 device code',
    `device_code_issued_at`         timestamp NULL DEFAULT NULL COMMENT 'device code 签发时间',
    `device_code_expires_at`        timestamp NULL DEFAULT NULL COMMENT 'device code 过期时间',
    `device_code_metadata`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'device code 元数据',
    `create_by`                     bigint NULL DEFAULT NULL COMMENT '创建人',
    `update_by`                     bigint NULL DEFAULT NULL COMMENT '修改人',
    `create_time`                   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`                   datetime NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

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
    `create_by`            bigint NULL DEFAULT NULL COMMENT '创建人',
    `update_by`            bigint NULL DEFAULT NULL COMMENT '修改人',
    `create_time`          datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`          datetime NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
