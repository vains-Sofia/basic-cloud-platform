package com.basic.framework.oauth2.storage.core.service;

import com.basic.framework.oauth2.storage.core.domain.BasicApplication;

/**
 * oauth2客户端信息服务接口
 *
 * @author vains
 */
public interface BasicApplicationService {

    /**
     * 保存或更新客户端
     *
     * @param basicApplication 客户端信息
     */
    void save(BasicApplication basicApplication);

    /**
     * 根据数据id查询客户端信息
     *
     * @param id 数据id
     * @return 客户端信息，不存在返回null
     */
    BasicApplication findById(String id);

    /**
     * 根据客户端id查询客户端信息
     *
     * @param clientId 客户端id
     * @return 客户端信息，不存在返回null
     */
    BasicApplication findByClientId(String clientId);

}
