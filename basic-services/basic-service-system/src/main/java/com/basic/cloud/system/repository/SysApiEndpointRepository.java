package com.basic.cloud.system.repository;

import com.basic.cloud.system.api.enums.ScanStatusEnum;
import com.basic.cloud.system.domain.SysApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 扫描接口详情表 Repository
 *
 * @author vains
 */
public interface SysApiEndpointRepository extends JpaRepository<SysApiEndpoint, Long>, JpaSpecificationExecutor<SysApiEndpoint> {

    /**
     * 根据扫描批次ID查询接口列表
     *
     * @param scanBatchId 扫描批次ID
     * @return 接口列表
     */
    List<SysApiEndpoint> findByScanBatchId(Long scanBatchId);

    /**
     * 根据路径和请求方法查询接口
     *
     * @param path          接口路径
     * @param requestMethod 请求方法
     * @return 接口详情
     */
    SysApiEndpoint findByPathAndRequestMethod(String path, String requestMethod);

    /**
     * 根据扫描状态查询接口列表
     *
     * @param scanStatus 扫描状态
     * @return 接口列表
     */
    List<SysApiEndpoint> findByScanStatus(ScanStatusEnum scanStatus);

    /**
     * 判断同一个批次下 path + method 是否重复（用于联合唯一校验逻辑）
     *
     * @param scanBatchId   扫描批次ID
     * @param path          接口路径
     * @param requestMethod 请求方法
     * @return 是否存在
     */
    boolean existsByScanBatchIdAndPathAndRequestMethod(Long scanBatchId, String path, String requestMethod);
}
