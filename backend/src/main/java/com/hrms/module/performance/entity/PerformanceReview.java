package com.hrms.module.performance.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("perf_review")
public class PerformanceReview extends BaseEntity {
    private Long planId;
    private Long empId;
    private Double selfScore;
    private String selfComment;
    private Double managerScore;
    private String managerComment;
    private Double hrScore;
    private String hrComment;
    private Double finalScore;
    private Integer result;
}
