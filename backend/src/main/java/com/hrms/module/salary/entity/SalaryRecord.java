package com.hrms.module.salary.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sal_record")
public class SalaryRecord extends BaseEntity {
    private Long empId;
    private String month;
    private Double basicSalary;
    private Double allowance;
    private Double overtimePay;
    private Double bonus;
    private Double deduction;
    private Double housingFund;
    private Double pension;
    private Double medical;
    private Double unemployment;
    private Double tax;
    private Double actualSalary;
    private Integer status;
}
