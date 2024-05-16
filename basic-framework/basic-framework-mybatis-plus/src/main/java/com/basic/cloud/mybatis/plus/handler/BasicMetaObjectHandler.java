package com.basic.cloud.mybatis.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.basic.cloud.mybatis.plus.domain.BasicEntity;
import com.basic.cloud.mybatis.plus.util.LambdaMethodUtils;
import com.basic.cloud.oauth2.authorization.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审计字段自动填充处理
 *
 * @author vains
 */
public class BasicMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getCreateBy),
                Serializable.class, SecurityUtils.getUserId());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateBy),
                Serializable.class, SecurityUtils.getUserId());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getCreateTime),
                LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateTime),
                LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateBy),
                Serializable.class, SecurityUtils.getUserId());
        this.strictInsertFill(metaObject, LambdaMethodUtils.extractMethodToProperty(BasicEntity::getUpdateTime),
                LocalDateTime.class, LocalDateTime.now());
    }


}
