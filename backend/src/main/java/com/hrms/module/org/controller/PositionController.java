package com.hrms.module.org.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.org.entity.Position;
import com.hrms.module.org.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "岗位管理")
@RestController
@RequestMapping("/api/v1/hr/org/position")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/page")
    @Operation(summary = "分页查询岗位")
    public PageResult<Position> page(@RequestParam(defaultValue = "1") long page,
                                     @RequestParam(defaultValue = "10") long pageSize) {
        return PageResult.success(positionService.page(new Page<>(page, pageSize)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询岗位")
    public Result<Position> getById(@PathVariable Long id) {
        return Result.success(positionService.getById(id));
    }

    @GetMapping
    @Operation(summary = "查询岗位列表")
    public Result<List<Position>> list(@RequestParam(required = false) Long deptId) {
        if (deptId != null) {
            return Result.success(positionService.listByDeptId(deptId));
        }
        return Result.success(positionService.list());
    }

    @GetMapping("/list")
    @Operation(summary = "查询岗位列表(别名)")
    public Result<List<Position>> listAlias(@RequestParam(required = false) Long deptId) {
        return list(deptId);
    }

    @PostMapping
    @Operation(summary = "新增岗位")
    public Result<Position> create(@RequestBody Position position) {
        positionService.save(position);
        return Result.success(position);
    }

    @PutMapping
    @Operation(summary = "修改岗位")
    public Result<Position> update(@RequestBody Position position) {
        positionService.updateById(position);
        return Result.success(position);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除岗位")
    public Result<Void> delete(@PathVariable Long id) {
        positionService.removeById(id);
        return Result.success();
    }
}
