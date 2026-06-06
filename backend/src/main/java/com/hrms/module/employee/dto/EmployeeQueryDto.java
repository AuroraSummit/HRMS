package com.hrms.module.employee.dto;

import com.hrms.common.dto.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeQueryDto extends PageQuery {
    private String name;
    private Long deptId;
    private String status;
    private Integer employmentType;
    private String empNo;
}
