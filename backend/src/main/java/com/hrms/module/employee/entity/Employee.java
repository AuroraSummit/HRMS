package com.hrms.module.employee.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emp_employee")
public class Employee extends BaseEntity {
    private String empNo;
    private String name;
    private Integer gender;
    private LocalDate birthday;
    private String idCard;
    private String phone;
    private String email;
    private String avatar;
    private Long deptId;
    private Long positionId;
    private LocalDate hireDate;
    private String status;
    private Integer employmentType;
    private Integer source;
    private String emergencyContact;
    private String emergencyPhone;
}
