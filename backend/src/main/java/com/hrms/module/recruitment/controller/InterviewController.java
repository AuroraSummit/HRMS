package com.hrms.module.recruitment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.recruitment.entity.InterviewRecord;
import com.hrms.module.recruitment.service.InterviewRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "面试记录管理")
@RestController
@RequestMapping("/api/v1/hr/recruitment/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewRecordService interviewService;

    @GetMapping("/page")
    @Operation(summary = "分页查询面试记录")
    public PageResult<InterviewRecord> page(@RequestParam(defaultValue = "1") long page,
                                            @RequestParam(defaultValue = "10") long pageSize) {
        Page<InterviewRecord> p = interviewService.page(new Page<>(page, pageSize),
                new LambdaQueryWrapper<InterviewRecord>().orderByDesc(InterviewRecord::getId));
        return PageResult.success(p);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询面试记录详情")
    public Result<InterviewRecord> getById(@PathVariable Long id) {
        return Result.success(interviewService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增面试记录")
    public Result<InterviewRecord> create(@RequestBody InterviewRecord record) {
        interviewService.save(record);
        return Result.success(record);
    }

    @PutMapping
    @Operation(summary = "修改面试记录")
    public Result<InterviewRecord> update(@RequestBody InterviewRecord record) {
        interviewService.updateById(record);
        return Result.success(record);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除面试记录")
    public Result<Void> delete(@PathVariable Long id) {
        interviewService.removeById(id);
        return Result.success();
    }
}
