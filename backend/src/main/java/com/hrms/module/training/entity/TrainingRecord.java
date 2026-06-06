package com.hrms.module.training.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("train_record")
public class TrainingRecord extends BaseEntity {
    private Long planId;
    private Long empId;
    private Integer attendance;
    private Double score;
    private String evaluation;
}
