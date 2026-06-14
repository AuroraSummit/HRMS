package com.hrms.module.probation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.probation.entity.Probation;
import com.hrms.module.probation.service.ProbationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "转正管理")
@RestController
@RequestMapping("/api/v1/hr/probation")
@RequiredArgsConstructor
public class ProbationController {

    private final ProbationService probationService;

    @GetMapping("/page")
    @Operation(summary = "分页查询转正记录")
    public PageResult<Probation> page(@RequestParam(defaultValue = "1") long page,
                                      @RequestParam(defaultValue = "10") long pageSize,
                                      @RequestParam(required = false) Long empId) {
        LambdaQueryWrapper<Probation> wrapper = new LambdaQueryWrapper<>();
        if (empId != null) wrapper.eq(Probation::getEmpId, empId);
        wrapper.orderByDesc(Probation::getId);
        Page<Probation> p = probationService.page(new Page<>(page, pageSize), wrapper);
        return PageResult.success(p);
    }

    @PostMapping
    @Operation(summary = "新增转正记录")
    public Result<Probation> create(@RequestBody Probation probation) {
        probation.setStatus(0);
        probationService.save(probation);
        return Result.success(probation);
    }

    @PutMapping
    @Operation(summary = "修改转正记录")
    public Result<Probation> update(@RequestBody Probation probation) {
        probationService.updateById(probation);
        return Result.success(probation);
    }

    @PutMapping("/evaluate/{id}")
    @Operation(summary = "提交转正评估")
    public Result<Void> evaluate(@PathVariable Long id, @RequestBody Probation data) {
        Probation p = probationService.getById(id);
        if (p != null) {
            if (data.getSelfEvaluation() != null) p.setSelfEvaluation(data.getSelfEvaluation());
            if (data.getMentorEvaluation() != null) p.setMentorEvaluation(data.getMentorEvaluation());
            if (data.getManagerEvaluation() != null) p.setManagerEvaluation(data.getManagerEvaluation());
            if (data.getScore() != null) p.setScore(data.getScore());
            if (data.getResult() != null) p.setResult(data.getResult());
            p.setStatus(1);
            probationService.updateById(p);
        }
        return Result.success();
    }

    @PutMapping("/approve/{id}")
    @Operation(summary = "审批转正")
    public Result<Void> approve(@PathVariable Long id, @RequestParam Integer status) {
        Probation p = probationService.getById(id);
        if (p != null) {
            p.setStatus(status);
            probationService.updateById(p);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除转正记录")
    public Result<Void> delete(@PathVariable Long id) {
        probationService.removeById(id);
        return Result.success();
    }
}
