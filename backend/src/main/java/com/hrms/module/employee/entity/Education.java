package com.hrms.module.employee.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emp_education")
public class Education extends BaseEntity {
    private Long empId;
    private String school;
    private String major;
    private Integer degree;
    private LocalDate startDate;
    private LocalDate endDate;
}
