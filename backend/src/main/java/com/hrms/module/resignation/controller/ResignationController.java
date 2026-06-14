package com.hrms.module.resignation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.resignation.entity.Resignation;
import com.hrms.module.resignation.service.ResignationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "离职管理")
@RestController
@RequestMapping("/api/v1/hr/resignation")
@RequiredArgsConstructor
public class ResignationController {

    private final ResignationService resignationService;

    @GetMapping("/page")
    @Operation(summary = "分页查询离职申请")
    public PageResult<Resignation> page(@RequestParam(defaultValue = "1") long page,
                                        @RequestParam(defaultValue = "10") long pageSize,
                                        @RequestParam(required = false) Long empId) {
        LambdaQueryWrapper<Resignation> wrapper = new LambdaQueryWrapper<>();
        if (empId != null) wrapper.eq(Resignation::getEmpId, empId);
        wrapper.orderByDesc(Resignation::getId);
        Page<Resignation> p = resignationService.page(new Page<>(page, pageSize), wrapper);
        return PageResult.success(p);
    }

    @PostMapping
    @Operation(summary = "提交离职申请")
    public Result<Resignation> create(@RequestBody Resignation resignation) {
        resignation.setStatus(0);
        resignationService.save(resignation);
        return Result.success(resignation);
    }

    @PutMapping
    @Operation(summary = "修改离职申请")
    public Result<Resignation> update(@RequestBody Resignation resignation) {
        resignationService.updateById(resignation);
        return Result.success(resignation);
    }

    @PutMapping("/approve/{id}")
    @Operation(summary = "审批离职")
    public Result<Void> approve(@PathVariable Long id, @RequestParam Integer status,
                                @RequestParam(required = false) String comment) {
        Resignation r = resignationService.getById(id);
        if (r != null) {
            r.setStatus(status);
            if (comment != null) r.setApproveComment(comment);
            resignationService.updateById(r);
        }
        return Result.success();
    }

    @PutMapping("/handover/{id}")
    @Operation(summary = "更新交接状态")
    public Result<Void> handover(@PathVariable Long id,
                                 @RequestParam String handoverTo,
                                 @RequestParam Integer handoverStatus) {
        Resignation r = resignationService.getById(id);
        if (r != null) {
            r.setHandoverTo(handoverTo);
            r.setHandoverStatus(handoverStatus);
            resignationService.updateById(r);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除离职申请")
    public Result<Void> delete(@PathVariable Long id) {
        resignationService.removeById(id);
        return Result.success();
    }
}
