package com.basic.cloud.oauth2.authorization.server.service.impl;

import com.basic.cloud.oauth2.authorization.server.entity.Client;
import com.basic.cloud.oauth2.authorization.server.mapper.ClientMapper;
import com.basic.cloud.oauth2.authorization.server.service.IClientService;
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
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {

}
