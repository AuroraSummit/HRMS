package com.hrms.module.performance.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("perf_template")
public class PerformanceTemplate extends BaseEntity {
    private String name;
    private String description;
    private Integer status;
}
