package com.hrms.module.employee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.module.employee.dto.EmployeeQueryDto;
import com.hrms.module.employee.entity.Employee;
import com.hrms.module.employee.vo.EmployeeDetailVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeDao extends BaseMapper<Employee> {
    Page<EmployeeDetailVO> selectEmployeeWithDeptAndPosition(Page<EmployeeDetailVO> page, EmployeeQueryDto queryDto);
}
