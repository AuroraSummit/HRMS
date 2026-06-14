package com.hrms.module.attendance.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.attendance.entity.LeaveApplication;
import com.hrms.module.attendance.service.LeaveApplicationService;
import com.hrms.module.employee.dao.EmployeeDao;
import com.hrms.module.employee.entity.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "请假管理")
@RestController
@RequestMapping("/api/v1/hr/attendance/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveApplicationService leaveService;
    private final EmployeeDao employeeDao;

    @GetMapping("/page")
    @Operation(summary = "分页查询请假记录")
    public PageResult<LeaveApplication> page(@RequestParam(defaultValue = "1") long page,
                                             @RequestParam(defaultValue = "10") long pageSize,
                                             @RequestParam(required = false) Long empId,
                                             @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<LeaveApplication> wrapper = new LambdaQueryWrapper<>();
        if (empId != null) wrapper.eq(LeaveApplication::getEmpId, empId);
        if (status != null) wrapper.eq(LeaveApplication::getStatus, status);
        wrapper.orderByDesc(LeaveApplication::getId);
        Page<LeaveApplication> result = leaveService.page(new Page<>(page, pageSize), wrapper);
        // 填充员工姓名
        for (LeaveApplication leave : result.getRecords()) {
            if (leave.getEmpId() != null) {
                Employee emp = employeeDao.selectById(leave.getEmpId());
                if (emp != null) leave.setEmployeeName(emp.getName());
            }
        }
        return PageResult.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询请假详情")
    public Result<LeaveApplication> getById(@PathVariable Long id) {
        return Result.success(leaveService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增请假申请")
    public Result<LeaveApplication> create(@RequestBody LeaveApplication leave) {
        leaveService.save(leave);
        return Result.success(leave);
    }

    @PutMapping
    @Operation(summary = "修改请假申请")
    public Result<LeaveApplication> update(@RequestBody LeaveApplication leave) {
        leaveService.updateById(leave);
        return Result.success(leave);
    }

    @PutMapping("/approve/{id}")
    @Operation(summary = "审批请假申请")
    public Result<Void> approve(@PathVariable Long id, @RequestBody(required = false) LeaveApplication body) {
        LeaveApplication leave = leaveService.getById(id);
        if (leave == null) {
            return Result.error("请假记录不存在");
        }
        // Default to approved (1), or use provided status
        Integer newStatus = (body != null && body.getStatus() != null) ? body.getStatus() : 1;
        leave.setStatus(newStatus);
        leaveService.updateById(leave);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除请假申请")
    public Result<Void> delete(@PathVariable Long id) {
        leaveService.removeById(id);
        return Result.success();
    }
}
