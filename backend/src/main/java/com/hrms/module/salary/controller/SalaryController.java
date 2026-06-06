package com.hrms.module.salary.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.salary.entity.SalaryConfig;
import com.hrms.module.salary.entity.SalaryRecord;
import com.hrms.module.salary.service.SalaryConfigService;
import com.hrms.module.salary.service.SalaryRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "薪酬管理")
@RestController
@RequestMapping("/api/v1/hr/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryConfigService configService;
    private final SalaryRecordService recordService;

    // ===== 薪酬配置 =====
    @GetMapping("/config")
    @Operation(summary = "查询薪酬配置列表")
    public Result<List<SalaryConfig>> listConfigs() {
        return Result.success(configService.list());
    }

    @GetMapping("/config/page")
    @Operation(summary = "分页查询薪酬配置")
    public PageResult<SalaryConfig> pageConfigs(@RequestParam(defaultValue = "1") long page,
                                                @RequestParam(defaultValue = "10") long pageSize) {
        return PageResult.success(configService.page(new Page<>(page, pageSize)));
    }

    @GetMapping("/config/{id}")
    @Operation(summary = "查询薪酬配置详情")
    public Result<SalaryConfig> getConfigById(@PathVariable Long id) {
        return Result.success(configService.getById(id));
    }

    @PostMapping("/config")
    @Operation(summary = "新增薪酬配置")
    public Result<SalaryConfig> createConfig(@RequestBody SalaryConfig config) {
        configService.save(config);
        return Result.success(config);
    }

    @PutMapping("/config")
    @Operation(summary = "修改薪酬配置")
    public Result<SalaryConfig> updateConfig(@RequestBody SalaryConfig config) {
        configService.updateById(config);
        return Result.success(config);
    }

    @DeleteMapping("/config/{id}")
    @Operation(summary = "删除薪酬配置")
    public Result<Void> deleteConfig(@PathVariable Long id) {
        configService.removeById(id);
        return Result.success();
    }

    // ===== 薪酬记录 =====
    @GetMapping("/page")
    @Operation(summary = "分页查询薪酬记录")
    public PageResult<SalaryRecord> pageRecords(@RequestParam(defaultValue = "1") long page,
                                                @RequestParam(defaultValue = "10") long pageSize,
                                                @RequestParam(required = false) Long empId,
                                                @RequestParam(required = false) String month) {
        LambdaQueryWrapper<SalaryRecord> wrapper = new LambdaQueryWrapper<>();
        if (empId != null) wrapper.eq(SalaryRecord::getEmpId, empId);
        if (month != null && !month.isEmpty()) wrapper.eq(SalaryRecord::getMonth, month);
        wrapper.orderByDesc(SalaryRecord::getId);
        return PageResult.success(recordService.page(new Page<>(page, pageSize), wrapper));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询薪酬记录详情")
    public Result<SalaryRecord> getRecordById(@PathVariable Long id) {
        return Result.success(recordService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增薪酬记录")
    public Result<SalaryRecord> createRecord(@RequestBody SalaryRecord record) {
        recordService.save(record);
        return Result.success(record);
    }

    @PutMapping
    @Operation(summary = "修改薪酬记录")
    public Result<SalaryRecord> updateRecord(@RequestBody SalaryRecord record) {
        recordService.updateById(record);
        return Result.success(record);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除薪酬记录")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        recordService.removeById(id);
        return Result.success();
    }

    // ===== 薪酬记录(旧路径兼容 /record/*) =====
    @GetMapping("/record/page")
    @Operation(summary = "分页查询薪酬记录(旧路径)")
    public PageResult<SalaryRecord> pageRecordsOld(@RequestParam(defaultValue = "1") long page,
                                                   @RequestParam(defaultValue = "10") long pageSize,
                                                   @RequestParam(required = false) Long empId,
                                                   @RequestParam(required = false) String month) {
        return pageRecords(page, pageSize, empId, month);
    }

    @GetMapping("/record/{id}")
    @Operation(summary = "查询薪酬记录详情(旧路径)")
    public Result<SalaryRecord> getRecordByIdOld(@PathVariable Long id) {
        return getRecordById(id);
    }

    @PostMapping("/record")
    @Operation(summary = "新增薪酬记录(旧路径)")
    public Result<SalaryRecord> createRecordOld(@RequestBody SalaryRecord record) {
        return createRecord(record);
    }

    @PutMapping("/record")
    @Operation(summary = "修改薪酬记录(旧路径)")
    public Result<SalaryRecord> updateRecordOld(@RequestBody SalaryRecord record) {
        return updateRecord(record);
    }

    @DeleteMapping("/record/{id}")
    @Operation(summary = "删除薪酬记录(旧路径)")
    public Result<Void> deleteRecordOld(@PathVariable Long id) {
        return deleteRecord(id);
    }
}
