package com.basic.example.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.example.mybatis.plus.enums.GenderEnum;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author vains
 * @since 2024-05-15
 */
@Getter
@Setter
@TableName("example_user")
public class ExampleUser extends BasicEntity {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 姓名
     */
    @NotNull
    @TableField("name")
    private String name;

    /**
     * 性别
     */
    @TableField("gender")
    private GenderEnum gender;

    /**
     * 是否开启
     */
    @TableField("enabled")
    private Boolean enabled;
}
