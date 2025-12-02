package com.basic.cloud.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.basic.cloud.workflow.api.domain.request.FindDefinitionPageRequest;
import com.basic.cloud.workflow.domain.entity.ProcessDefinition;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Param;

/**
 * 针对表【process_definition(流程定义表)】的数据库操作Mapper
 *
 * @author vains
 * @see com.basic.cloud.workflow.domain.entity.ProcessDefinition
 */
public interface ProcessDefinitionMapper extends BaseMapper<ProcessDefinition> {

    /**
     * 根据流程定义key查询最新版本的流程定义
     *
     * @param processKey 流程定义key
     * @return ProcessDefinition 流程定义
     */
    ProcessDefinition selectLatestDefinition(@NotBlank(message = "流程定义key不能为空") String processKey);

    /**
     * 分页查询最新版本的流程定义列表，每个流程定义都是最新的版本
     *
     * @param page    分页入参
     * @param request 查询入参
     * @return 流程定义分页数据
     */
    IPage<ProcessDefinition> selectLatestDefinitionsPage(IPage<ProcessDefinition> page,
                                                         @Param("request") FindDefinitionPageRequest request);
}




