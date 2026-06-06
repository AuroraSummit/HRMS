package com.hrms.module.training.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.training.entity.TrainingPlan;
import com.hrms.module.training.entity.TrainingRecord;
import com.hrms.module.training.service.TrainingPlanService;
import com.hrms.module.training.service.TrainingRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "培训管理")
@RestController
@RequestMapping("/api/v1/hr/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingPlanService planService;
    private final TrainingRecordService recordService;

    // ===== 培训计划（前端使用 /training/page 等路径） =====
    @GetMapping("/page")
    @Operation(summary = "分页查询培训计划")
    public PageResult<TrainingPlan> pagePlans(@RequestParam(defaultValue = "1") long page,
                                              @RequestParam(defaultValue = "10") long pageSize) {
        return PageResult.success(planService.page(new Page<>(page, pageSize),
                new LambdaQueryWrapper<TrainingPlan>().orderByDesc(TrainingPlan::getId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询培训计划详情")
    public Result<TrainingPlan> getPlanById(@PathVariable Long id) {
        return Result.success(planService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增培训计划")
    public Result<TrainingPlan> createPlan(@RequestBody TrainingPlan plan) {
        planService.save(plan);
        return Result.success(plan);
    }

    @PutMapping
    @Operation(summary = "修改培训计划")
    public Result<TrainingPlan> updatePlan(@RequestBody TrainingPlan plan) {
        planService.updateById(plan);
        return Result.success(plan);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除培训计划")
    public Result<Void> deletePlan(@PathVariable Long id) {
        planService.removeById(id);
        return Result.success();
    }

    // ===== 旧路径兼容 /plan/* =====
    @GetMapping("/plan")
    @Operation(summary = "查询培训计划列表(旧路径)")
    public Result<List<TrainingPlan>> listPlansOld() {
        return Result.success(planService.list());
    }

    @GetMapping("/plan/page")
    @Operation(summary = "分页查询培训计划(旧路径)")
    public PageResult<TrainingPlan> pagePlansOld(@RequestParam(defaultValue = "1") long page,
                                                  @RequestParam(defaultValue = "10") long pageSize) {
        return pagePlans(page, pageSize);
    }

    @GetMapping("/plan/{id}")
    @Operation(summary = "查询培训计划详情(旧路径)")
    public Result<TrainingPlan> getPlanByIdOld(@PathVariable Long id) {
        return getPlanById(id);
    }

    @PostMapping("/plan")
    @Operation(summary = "新增培训计划(旧路径)")
    public Result<TrainingPlan> createPlanOld(@RequestBody TrainingPlan plan) {
        return createPlan(plan);
    }

    @PutMapping("/plan")
    @Operation(summary = "修改培训计划(旧路径)")
    public Result<TrainingPlan> updatePlanOld(@RequestBody TrainingPlan plan) {
        return updatePlan(plan);
    }

    @DeleteMapping("/plan/{id}")
    @Operation(summary = "删除培训计划(旧路径)")
    public Result<Void> deletePlanOld(@PathVariable Long id) {
        return deletePlan(id);
    }

    // ===== 培训记录 =====
    @GetMapping("/record/page")
    @Operation(summary = "分页查询培训记录")
    public PageResult<TrainingRecord> pageRecords(@RequestParam(defaultValue = "1") long page,
                                                  @RequestParam(defaultValue = "10") long pageSize,
                                                  @RequestParam(required = false) Long planId,
                                                  @RequestParam(required = false) Long empId) {
        LambdaQueryWrapper<TrainingRecord> wrapper = new LambdaQueryWrapper<>();
        if (planId != null) wrapper.eq(TrainingRecord::getPlanId, planId);
        if (empId != null) wrapper.eq(TrainingRecord::getEmpId, empId);
        wrapper.orderByDesc(TrainingRecord::getId);
        return PageResult.success(recordService.page(new Page<>(page, pageSize), wrapper));
    }

    @PostMapping("/record")
    @Operation(summary = "新增培训记录")
    public Result<TrainingRecord> createRecord(@RequestBody TrainingRecord record) {
        recordService.save(record);
        return Result.success(record);
    }

    @PutMapping("/record")
    @Operation(summary = "修改培训记录")
    public Result<TrainingRecord> updateRecord(@RequestBody TrainingRecord record) {
        recordService.updateById(record);
        return Result.success(record);
    }

    @DeleteMapping("/record/{id}")
    @Operation(summary = "删除培训记录")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        recordService.removeById(id);
        return Result.success();
    }
}
