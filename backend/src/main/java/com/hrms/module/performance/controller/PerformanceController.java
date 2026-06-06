package com.hrms.module.performance.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.performance.entity.PerformanceTemplate;
import com.hrms.module.performance.entity.PerformancePlan;
import com.hrms.module.performance.entity.PerformanceReview;
import com.hrms.module.performance.service.PerformanceTemplateService;
import com.hrms.module.performance.service.PerformancePlanService;
import com.hrms.module.performance.service.PerformanceReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "绩效管理")
@RestController
@RequestMapping("/api/v1/hr/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceTemplateService templateService;
    private final PerformancePlanService planService;
    private final PerformanceReviewService reviewService;

    // ===== 模板 =====
    @GetMapping("/template")
    @Operation(summary = "查询绩效模板列表")
    public Result<List<PerformanceTemplate>> listTemplates() {
        return Result.success(templateService.list());
    }

    @GetMapping("/template/page")
    @Operation(summary = "分页查询绩效模板")
    public PageResult<PerformanceTemplate> pageTemplates(@RequestParam(defaultValue = "1") long page,
                                                         @RequestParam(defaultValue = "10") long pageSize) {
        return PageResult.success(templateService.page(new Page<>(page, pageSize)));
    }

    @PostMapping("/template")
    @Operation(summary = "新增绩效模板")
    public Result<PerformanceTemplate> createTemplate(@RequestBody PerformanceTemplate template) {
        templateService.save(template);
        return Result.success(template);
    }

    @PutMapping("/template")
    @Operation(summary = "修改绩效模板")
    public Result<PerformanceTemplate> updateTemplate(@RequestBody PerformanceTemplate template) {
        templateService.updateById(template);
        return Result.success(template);
    }

    @DeleteMapping("/template/{id}")
    @Operation(summary = "删除绩效模板")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        templateService.removeById(id);
        return Result.success();
    }

    // ===== 计划 =====
    @GetMapping("/plan")
    @Operation(summary = "查询绩效计划列表")
    public Result<List<PerformancePlan>> listPlans() {
        return Result.success(planService.list());
    }

    @GetMapping("/plan/page")
    @Operation(summary = "分页查询绩效计划")
    public PageResult<PerformancePlan> pagePlans(@RequestParam(defaultValue = "1") long page,
                                                  @RequestParam(defaultValue = "10") long pageSize) {
        return PageResult.success(planService.page(new Page<>(page, pageSize),
                new LambdaQueryWrapper<PerformancePlan>().orderByDesc(PerformancePlan::getId)));
    }

    @PostMapping("/plan")
    @Operation(summary = "新增绩效计划")
    public Result<PerformancePlan> createPlan(@RequestBody PerformancePlan plan) {
        planService.save(plan);
        return Result.success(plan);
    }

    @PutMapping("/plan")
    @Operation(summary = "修改绩效计划")
    public Result<PerformancePlan> updatePlan(@RequestBody PerformancePlan plan) {
        planService.updateById(plan);
        return Result.success(plan);
    }

    @DeleteMapping("/plan/{id}")
    @Operation(summary = "删除绩效计划")
    public Result<Void> deletePlan(@PathVariable Long id) {
        planService.removeById(id);
        return Result.success();
    }

    // ===== 考评（前端使用 /performance/page 等路径） =====
    @GetMapping("/page")
    @Operation(summary = "分页查询考评记录")
    public PageResult<PerformanceReview> pageReviews(@RequestParam(defaultValue = "1") long page,
                                                     @RequestParam(defaultValue = "10") long pageSize,
                                                     @RequestParam(required = false) Long planId,
                                                     @RequestParam(required = false) Long empId) {
        LambdaQueryWrapper<PerformanceReview> wrapper = new LambdaQueryWrapper<>();
        if (planId != null) wrapper.eq(PerformanceReview::getPlanId, planId);
        if (empId != null) wrapper.eq(PerformanceReview::getEmpId, empId);
        wrapper.orderByDesc(PerformanceReview::getId);
        return PageResult.success(reviewService.page(new Page<>(page, pageSize), wrapper));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询考评记录详情")
    public Result<PerformanceReview> getReviewById(@PathVariable Long id) {
        return Result.success(reviewService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增考评记录")
    public Result<PerformanceReview> createReview(@RequestBody PerformanceReview review) {
        reviewService.save(review);
        return Result.success(review);
    }

    @PutMapping
    @Operation(summary = "修改考评记录")
    public Result<PerformanceReview> updateReview(@RequestBody PerformanceReview review) {
        reviewService.updateById(review);
        return Result.success(review);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除考评记录")
    public Result<Void> deleteReview(@PathVariable Long id) {
        reviewService.removeById(id);
        return Result.success();
    }

    // ===== 考评(旧路径兼容 /review/*) =====
    @GetMapping("/review/page")
    @Operation(summary = "分页查询考评记录(旧路径)")
    public PageResult<PerformanceReview> pageReviewsOld(@RequestParam(defaultValue = "1") long page,
                                                        @RequestParam(defaultValue = "10") long pageSize,
                                                        @RequestParam(required = false) Long planId,
                                                        @RequestParam(required = false) Long empId) {
        return pageReviews(page, pageSize, planId, empId);
    }

    @PostMapping("/review")
    @Operation(summary = "新增考评记录(旧路径)")
    public Result<PerformanceReview> createReviewOld(@RequestBody PerformanceReview review) {
        return createReview(review);
    }

    @PutMapping("/review")
    @Operation(summary = "修改考评记录(旧路径)")
    public Result<PerformanceReview> updateReviewOld(@RequestBody PerformanceReview review) {
        return updateReview(review);
    }

    @DeleteMapping("/review/{id}")
    @Operation(summary = "删除考评记录(旧路径)")
    public Result<Void> deleteReviewOld(@PathVariable Long id) {
        return deleteReview(id);
    }
}
