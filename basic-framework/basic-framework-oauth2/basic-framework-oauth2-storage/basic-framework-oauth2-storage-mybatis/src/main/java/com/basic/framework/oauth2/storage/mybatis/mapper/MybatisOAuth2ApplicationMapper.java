package com.basic.framework.oauth2.storage.mybatis.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basic.framework.oauth2.storage.core.domain.request.FindApplicationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.response.BasicApplicationResponse;
import com.basic.framework.oauth2.storage.mybatis.domain.MybatisOAuth2Application;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vains
 * @since 2024-05-17
 */
public interface MybatisOAuth2ApplicationMapper extends BaseMapper<MybatisOAuth2Application> {

    /**
     * 分页查询客户端信息
     * @param page 分页对象，MP自动解析
     * @param request 其它入参
     * @return 分页的客户端信息
     */
    IPage<BasicApplicationResponse> selectConditionPage(@Param("page") Page<BasicApplicationResponse> page, @Param("request") FindApplicationPageRequest request);
}
