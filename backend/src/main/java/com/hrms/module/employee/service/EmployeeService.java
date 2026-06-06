package com.hrms.module.employee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.module.employee.dto.CreateEmployeeDto;
import com.hrms.module.employee.dto.EmployeeQueryDto;
import com.hrms.module.employee.entity.Employee;
import com.hrms.module.employee.vo.EmployeeDetailVO;

import jakarta.servlet.http.HttpServletResponse;

public interface EmployeeService extends IService<Employee> {
    Page<EmployeeDetailVO> pageQuery(EmployeeQueryDto queryDto);
    EmployeeDetailVO getDetail(Long id);
    Employee create(CreateEmployeeDto dto);
    Employee update(Employee employee);
    void delete(Long id);
    void export(EmployeeQueryDto queryDto, HttpServletResponse response);
}
