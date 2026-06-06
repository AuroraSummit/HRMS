package com.hrms.module.attendance.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.attendance.entity.Attendance;
import com.hrms.module.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "考勤管理")
@RestController
@RequestMapping("/api/v1/hr/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/page")
    @Operation(summary = "分页查询考勤记录")
    public PageResult<Attendance> page(@RequestParam(defaultValue = "1") long page,
                                       @RequestParam(defaultValue = "10") long pageSize,
                                       @RequestParam(required = false) Long empId,
                                       @RequestParam(required = false) LocalDate startDate,
                                       @RequestParam(required = false) LocalDate endDate) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        if (empId != null) wrapper.eq(Attendance::getEmpId, empId);
        if (startDate != null) wrapper.ge(Attendance::getAttendanceDate, startDate);
        if (endDate != null) wrapper.le(Attendance::getAttendanceDate, endDate);
        wrapper.orderByDesc(Attendance::getAttendanceDate);
        return PageResult.success(attendanceService.page(new Page<>(page, pageSize), wrapper));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询考勤记录详情")
    public Result<Attendance> getById(@PathVariable Long id) {
        return Result.success(attendanceService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增考勤记录")
    public Result<Attendance> create(@RequestBody Attendance attendance) {
        attendanceService.save(attendance);
        return Result.success(attendance);
    }

    @PutMapping
    @Operation(summary = "修改考勤记录")
    public Result<Attendance> update(@RequestBody Attendance attendance) {
        attendanceService.updateById(attendance);
        return Result.success(attendance);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除考勤记录")
    public Result<Void> delete(@PathVariable Long id) {
        attendanceService.removeById(id);
        return Result.success();
    }
}
