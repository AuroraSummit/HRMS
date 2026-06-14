package com.hrms.module.transfer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.transfer.entity.Transfer;
import com.hrms.module.transfer.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "员工异动")
@RestController
@RequestMapping("/api/v1/hr/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @GetMapping("/page")
    @Operation(summary = "分页查询异动记录")
    public PageResult<Transfer> page(@RequestParam(defaultValue = "1") long page,
                                     @RequestParam(defaultValue = "10") long pageSize,
                                     @RequestParam(required = false) Long empId) {
        LambdaQueryWrapper<Transfer> wrapper = new LambdaQueryWrapper<>();
        if (empId != null) wrapper.eq(Transfer::getEmpId, empId);
        wrapper.orderByDesc(Transfer::getId);
        Page<Transfer> p = transferService.page(new Page<>(page, pageSize), wrapper);
        return PageResult.success(p);
    }

    @PostMapping
    @Operation(summary = "新增异动记录")
    public Result<Transfer> create(@RequestBody Transfer transfer) {
        transfer.setStatus(0);
        transferService.save(transfer);
        return Result.success(transfer);
    }

    @PutMapping
    @Operation(summary = "修改异动记录")
    public Result<Transfer> update(@RequestBody Transfer transfer) {
        transferService.updateById(transfer);
        return Result.success(transfer);
    }

    @PutMapping("/approve/{id}")
    @Operation(summary = "审批异动")
    public Result<Void> approve(@PathVariable Long id, @RequestParam Integer status) {
        Transfer transfer = transferService.getById(id);
        if (transfer != null) {
            transfer.setStatus(status);
            transferService.updateById(transfer);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除异动记录")
    public Result<Void> delete(@PathVariable Long id) {
        transferService.removeById(id);
        return Result.success();
    }
}
