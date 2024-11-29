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
  ROW_FORMAT = Dynamic;

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
  ROW_FORMAT = Dynamic;

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
  ROW_FORMAT = Dynamic;

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
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_basic_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_basic_user`;
CREATE TABLE `sys_basic_user`
(
    `id`                    bigint                                                 NOT NULL COMMENT '自增id',
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
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`                  bigint                          NOT NULL COMMENT '主键id',
    `name`                varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '权限名',
    `permission`          varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '权限码',
    `path`                varchar(100) COLLATE utf8mb4_bin                       DEFAULT NULL COMMENT '路径',
    `request_method`      varchar(10) COLLATE utf8mb4_bin                        DEFAULT NULL COMMENT 'HTTP请求方式',
    `permission_type`     tinyint                         NOT NULL COMMENT '0:菜单,1:接口,2:其它',
    `description`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
    `need_authentication` tinyint(1)                                             DEFAULT '0' COMMENT '是否需要鉴权',
    `deleted`             tinyint(1)                                             DEFAULT NULL COMMENT '是否已删除',
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

SET FOREIGN_KEY_CHECKS = 1;
