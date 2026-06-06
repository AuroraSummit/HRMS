package com.hrms.module.recruitment.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("rec_demand")
public class RecruitmentDemand extends BaseEntity {
    private String title;
    private Long deptId;
    private Long positionId;
    private Integer headcount;
    private Integer currentCount;
    private String description;
    private String requirement;
    private Double salaryMin;
    private Double salaryMax;
    private Integer status;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String positionName;
}
