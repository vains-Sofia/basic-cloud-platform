package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysThirdUserBind;
import com.basic.cloud.system.enums.BindStatusEnum;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 三方登录用户绑定 Repository
 *
 * @author vains
 */
public interface SysThirdUserBindRepository extends JpaRepository<SysThirdUserBind, Long>, JpaSpecificationExecutor<SysThirdUserBind> {

    /**
     * 根据第三方平台和用户ID查询绑定信息
     *
     * @param provider       第三方平台
     * @param providerUserId 第三方用户ID
     * @return 可选的绑定信息
     */
    Optional<SysThirdUserBind> findByProviderAndProviderUserId(OAuth2AccountPlatformEnum provider, String providerUserId);

    /**
     * 根据确认绑定token查询绑定信息
     *
     * @param confirmToken 确认绑定token
     * @return 绑定信息列表
     */
    Optional<SysThirdUserBind> findByConfirmToken(String confirmToken);

    /**
     * 检查是否存在指定平台、用户ID和绑定状态的绑定信息
     *
     * @param provider       第三方平台
     * @param providerUserId 第三方用户ID
     * @param bindStatus     绑定状态
     * @return 是否存在
     */
    boolean existsByProviderAndProviderUserIdAndBindStatus(OAuth2AccountPlatformEnum provider, String providerUserId, BindStatusEnum bindStatus);
}
