package com.hrms.module.employee.controller;

import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.employee.dto.CreateEmployeeDto;
import com.hrms.module.employee.dto.EmployeeQueryDto;
import com.hrms.module.employee.entity.Employee;
import com.hrms.module.employee.service.EmployeeService;
import com.hrms.module.employee.vo.EmployeeDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "员工管理")
@RestController
@RequestMapping("/api/v1/hr/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/page")
    @Operation(summary = "分页查询员工")
    public PageResult<EmployeeDetailVO> page(EmployeeQueryDto queryDto) {
        return PageResult.success(employeeService.pageQuery(queryDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询员工详情")
    public Result<EmployeeDetailVO> getById(@PathVariable Long id) {
        return Result.success(employeeService.getDetail(id));
    }

    @PostMapping
    @Operation(summary = "新增员工")
    public Result<Employee> create(@Valid @RequestBody CreateEmployeeDto dto) {
        return Result.success(employeeService.create(dto));
    }

    @PutMapping
    @Operation(summary = "修改员工")
    public Result<Employee> update(@RequestBody Employee employee) {
        return Result.success(employeeService.update(employee));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工")
    public Result<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return Result.success();
    }

    @GetMapping("/export")
    @Operation(summary = "导出员工Excel")
    public void export(@Valid EmployeeQueryDto queryDto, HttpServletResponse response) {
        employeeService.export(queryDto, response);
    }
}
