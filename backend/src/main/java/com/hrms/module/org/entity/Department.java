package com.hrms.module.org.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("org_department")
public class Department extends BaseEntity {
    private String name;
    private Long parentId;
    private String path;
    private Integer sort;
    private String leader;
    private String phone;
    private String email;
    private Integer status;
}
