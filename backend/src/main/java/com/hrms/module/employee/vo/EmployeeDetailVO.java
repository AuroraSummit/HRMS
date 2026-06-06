package com.hrms.module.employee.vo;

import com.hrms.module.employee.entity.Education;
import com.hrms.module.employee.entity.Employee;
import com.hrms.module.employee.entity.WorkExperience;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeDetailVO extends Employee {
    private String deptName;
    private String positionName;
    private List<Education> educationList;
    private List<WorkExperience> workExperienceList;
}
