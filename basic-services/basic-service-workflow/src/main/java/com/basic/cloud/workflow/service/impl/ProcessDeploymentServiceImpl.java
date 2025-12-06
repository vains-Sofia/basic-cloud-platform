package com.basic.cloud.workflow.service.impl;

import com.basic.cloud.workflow.api.domain.request.FindDeploymentPageRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessDeployResponse;
import com.basic.cloud.workflow.service.ProcessDeploymentService;
import com.basic.cloud.workflow.util.PaginationUtils;
import com.basic.cloud.workflow.util.QueryBuilder;
import com.basic.framework.core.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 流程部署 Service 实现
 *
 * @author vains
 */
@Service
@RequiredArgsConstructor
public class ProcessDeploymentServiceImpl implements ProcessDeploymentService {

    private final RepositoryService repositoryService;

    @Override
    public PageResult<PageProcessDeployResponse> findDeploymentPage(FindDeploymentPageRequest request) {
        DeploymentQuery serviceDeploymentQuery = repositoryService.createDeploymentQuery();
        // 组装查询参数
        DeploymentQuery deploymentQuery = new QueryBuilder<>(serviceDeploymentQuery)
                .like(serviceDeploymentQuery::deploymentNameLike, request.getName())
                .like(serviceDeploymentQuery::deploymentCategoryLike, request.getCategory())
                .like(serviceDeploymentQuery::deploymentKeyLike, request.getProcessDefinitionKey())
                .build();

        // 根据部署时间倒序排序
        deploymentQuery.orderByDeploymentTime().desc();

        // 计算适用于框架内部分页的页码和每页行数
        PaginationUtils.PageParam param = PaginationUtils.calc(Math.toIntExact(request.getCurrent()), Math.toIntExact(request.getSize()));
        // 查询
        List<Deployment> deployments = deploymentQuery.listPage(param.firstResult(), param.maxResults());

        // 安全的转为响应bean列表
        List<PageProcessDeployResponse> responseList = Optional.ofNullable(deployments)
                .map(deploymentList -> {
                    List<PageProcessDeployResponse> deploymentResponsesList = new ArrayList<>();
                    for (Deployment deployment : deploymentList) {
                        PageProcessDeployResponse response = new PageProcessDeployResponse();
                        BeanUtils.copyProperties(deployment, response);
                        response.setProcessKey(deployment.getKey());
                        deploymentResponsesList.add(response);
                    }
                    return deploymentResponsesList;
                })
                .orElse(List.of());

        return PageResult.of(request.getCurrent(), request.getSize(), deploymentQuery.count(), responseList);
    }

    @Override
    public void undeploy(String deploymentId, Boolean cascade) {
        repositoryService.deleteDeployment(deploymentId, (cascade == null || cascade));
    }
}
