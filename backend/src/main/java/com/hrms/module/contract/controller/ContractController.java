package com.hrms.module.contract.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.contract.entity.Contract;
import com.hrms.module.contract.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "合同管理")
@RestController
@RequestMapping("/api/v1/hr/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping("/page")
    @Operation(summary = "分页查询合同")
    public PageResult<Contract> page(@RequestParam(defaultValue = "1") long page,
                                     @RequestParam(defaultValue = "10") long pageSize,
                                     @RequestParam(required = false) Long empId,
                                     @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        if (empId != null) wrapper.eq(Contract::getEmpId, empId);
        if (status != null) wrapper.eq(Contract::getStatus, status);
        wrapper.orderByDesc(Contract::getId);
        return PageResult.success(contractService.page(new Page<>(page, pageSize), wrapper));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询合同详情")
    public Result<Contract> getById(@PathVariable Long id) {
        return Result.success(contractService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增合同")
    public Result<Contract> create(@RequestBody Contract contract) {
        contractService.save(contract);
        return Result.success(contract);
    }

    @PutMapping
    @Operation(summary = "修改合同")
    public Result<Contract> update(@RequestBody Contract contract) {
        contractService.updateById(contract);
        return Result.success(contract);
    }

    @PutMapping("/renew/{id}")
    @Operation(summary = "续签合同")
    public Result<Contract> renew(@PathVariable Long id, @RequestBody Contract body) {
        Contract contract = contractService.getById(id);
        if (contract == null) {
            return Result.error("合同不存在");
        }
        if (body.getEndDate() != null) {
            contract.setEndDate(body.getEndDate());
        }
        if (body.getStartDate() != null) {
            contract.setStartDate(body.getStartDate());
        }
        // Reset status to active (1=生效)
        contract.setStatus(1);
        contractService.updateById(contract);
        return Result.success(contract);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除合同")
    public Result<Void> delete(@PathVariable Long id) {
        contractService.removeById(id);
        return Result.success();
    }
}
