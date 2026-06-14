package com.hrms.module.employee.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hrms.module.employee.entity.Education;
import com.hrms.module.employee.entity.Employee;
import com.hrms.module.employee.entity.WorkExperience;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateEmployeeDto extends Employee {
    @TableField(exist = false)
    private List<Education> educationList;

    @TableField(exist = false)
    private List<WorkExperience> workExperienceList;
}
