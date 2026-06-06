package com.hrms.module.recruitment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.recruitment.entity.OfferRecord;
import com.hrms.module.recruitment.service.OfferRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Offer管理")
@RestController
@RequestMapping("/api/v1/hr/recruitment/offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferRecordService offerService;

    @GetMapping
    @Operation(summary = "查询Offer列表")
    public Result<List<OfferRecord>> list() {
        return Result.success(offerService.list());
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询Offer")
    public PageResult<OfferRecord> page(@RequestParam(defaultValue = "1") long page,
                                        @RequestParam(defaultValue = "10") long pageSize) {
        return PageResult.success(offerService.page(new Page<>(page, pageSize),
                new LambdaQueryWrapper<OfferRecord>().orderByDesc(OfferRecord::getId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询Offer详情")
    public Result<OfferRecord> getById(@PathVariable Long id) {
        return Result.success(offerService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增Offer")
    public Result<OfferRecord> create(@RequestBody OfferRecord offer) {
        offerService.save(offer);
        return Result.success(offer);
    }

    @PutMapping
    @Operation(summary = "修改Offer")
    public Result<OfferRecord> update(@RequestBody OfferRecord offer) {
        offerService.updateById(offer);
        return Result.success(offer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Offer")
    public Result<Void> delete(@PathVariable Long id) {
        offerService.removeById(id);
        return Result.success();
    }
}
