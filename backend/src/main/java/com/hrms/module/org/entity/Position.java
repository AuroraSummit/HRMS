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
    private String description;
    private Integer status;

    @TableField(exist = false)
    private String deptName;
}
