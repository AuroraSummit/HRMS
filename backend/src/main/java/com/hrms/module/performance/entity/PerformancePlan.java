package com.hrms.module.performance.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("perf_plan")
public class PerformancePlan extends BaseEntity {
    private Long templateId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
}
