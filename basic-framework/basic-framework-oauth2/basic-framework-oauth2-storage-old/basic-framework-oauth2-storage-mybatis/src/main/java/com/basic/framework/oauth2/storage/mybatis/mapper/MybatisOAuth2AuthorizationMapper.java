package com.basic.framework.oauth2.storage.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basic.framework.oauth2.storage.core.domain.request.FindAuthorizationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.response.FindAuthorizationPageResponse;
import com.basic.framework.oauth2.storage.mybatis.domain.MybatisOAuth2Authorization;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author vains
 * @since 2024-05-17
 */
public interface MybatisOAuth2AuthorizationMapper extends BaseMapper<MybatisOAuth2Authorization> {

    /**
     * 分页查询认证信息列表
     *
     * @param pageQuery MybatisPlus分页所需对象
     * @param request   分页请求入参
     * @return 认证信息分页列表
     */
    IPage<FindAuthorizationPageResponse> selectConditionPage(@Param("page") Page<FindAuthorizationPageResponse> pageQuery,
                                                             @Param("request") FindAuthorizationPageRequest request);
}
