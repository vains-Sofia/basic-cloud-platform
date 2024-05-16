package com.basic.cloud.mybatis.plus.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 *
 * @author vains
 */
@Getter
@Setter
public class BasicEntity implements Serializable {

    /**
     * 创建人
     */
    private Serializable createBy;

    /**
     * 修改人
     */
    private Serializable updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
