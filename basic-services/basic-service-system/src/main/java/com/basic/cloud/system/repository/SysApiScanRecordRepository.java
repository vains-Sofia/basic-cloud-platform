package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysApiScanRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 接口扫描记录表 Repository
 *
 * @author vains
 */
public interface SysApiScanRecordRepository extends JpaRepository<SysApiScanRecord, Long>, JpaSpecificationExecutor<SysApiScanRecord> {

}
