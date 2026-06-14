package com.hrms.module.org.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("org_position")
public class Position extends BaseEntity {
    private String name;
    private String code;
    private Long deptId;
    private Integer sort;
    private Integer status;

    /** 岗位描述（数据库无此列，仅供业务使用） */
    @TableField(exist = false)
    private String description;

    @TableField(exist = false)
    private String deptName;
}
