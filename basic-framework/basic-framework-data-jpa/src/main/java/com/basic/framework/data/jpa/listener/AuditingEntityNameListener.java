package com.basic.framework.data.jpa.listener;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * 监听修改、新增时间，设置用户名
 *
 * @author vains
 */
public class AuditingEntityNameListener {

    @PrePersist
    public void touchForCreate(Object target) {
        if (target instanceof BasicAuditorEntity entity) {
            AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
            if (authenticatedUser != null) {
                entity.setUpdateName(authenticatedUser.getName());
                entity.setCreateName(authenticatedUser.getName());
            }
        }
    }

    @PreUpdate
    public void touchForUpdate(Object target) {
        if (target instanceof BasicAuditorEntity entity) {
            AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
            if (authenticatedUser != null) {
                entity.setUpdateName(authenticatedUser.getName());
            }
        }
    }

}
