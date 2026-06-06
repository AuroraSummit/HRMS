package com.hrms.module.salary.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sal_config")
public class SalaryConfig extends BaseEntity {
    private String name;
    private Double basicSalary;
    private Double housingFundRate;
    private Double pensionRate;
    private Double medicalRate;
    private Double unemploymentRate;
    private Double injuryRate;
    private Double maternityRate;
    private Double taxExemption;
}
