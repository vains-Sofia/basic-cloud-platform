package com.basic.framework.mybatis.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import com.basic.framework.mybatis.plus.util.LambdaMethodUtils;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 审计字段自动填充处理
 *
 * @author vains
 */
public class BasicMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        String username;
        if (authenticatedUser != null) {
            username = authenticatedUser.getUsername();
        } else {
            username = null;
        }
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getCreateBy),
                Long.class, SecurityUtils.getUserId());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateBy),
                Long.class, SecurityUtils.getUserId());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getCreateTime),
                LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateTime),
                LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getCreateName),
                String.class, username);
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateName),
                String.class, username);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        String username;
        if (authenticatedUser != null) {
            username = authenticatedUser.getUsername();
        } else {
            username = null;
        }
        this.strictUpdateFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateBy),
                Long.class, SecurityUtils.getUserId());
        this.strictUpdateFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateTime),
                LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateName),
                String.class, username);
    }


}
