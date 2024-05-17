package com.basic.cloud.oauth2.authorization.server.service.impl;

import com.basic.cloud.oauth2.authorization.server.entity.AuthorizationConsent;
import com.basic.cloud.oauth2.authorization.server.mapper.AuthorizationConsentMapper;
import com.basic.cloud.oauth2.authorization.server.service.IAuthorizationConsentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vains
 * @since 2024-05-17
 */
@Service
public class AuthorizationConsentServiceImpl extends ServiceImpl<AuthorizationConsentMapper, AuthorizationConsent> implements IAuthorizationConsentService {

}
