package com.basic.cloud.oauth2.authorization.server.service.impl;

import com.basic.cloud.oauth2.authorization.server.entity.Authorization;
import com.basic.cloud.oauth2.authorization.server.mapper.AuthorizationMapper;
import com.basic.cloud.oauth2.authorization.server.service.IAuthorizationService;
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
public class AuthorizationServiceImpl extends ServiceImpl<AuthorizationMapper, Authorization> implements IAuthorizationService {

}
