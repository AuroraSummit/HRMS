package com.hrms.module.org.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.org.entity.Department;
import com.hrms.module.org.service.DepartmentService;
import com.hrms.module.org.vo.DeptTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门管理")
@RestController
@RequestMapping("/api/v1/hr/org/dept")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/tree")
    @Operation(summary = "获取部门树")
    public Result<List<DeptTreeVO>> tree() {
        return Result.success(departmentService.findTree());
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询部门")
    public PageResult<Department> page(@RequestParam(defaultValue = "1") long page,
                                       @RequestParam(defaultValue = "10") long pageSize) {
        return PageResult.success(departmentService.page(new Page<>(page, pageSize)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询部门")
    public Result<Department> getById(@PathVariable Long id) {
        return Result.success(departmentService.getById(id));
    }

    @GetMapping
    @Operation(summary = "查询所有部门")
    public Result<List<Department>> list() {
        return Result.success(departmentService.list());
    }

    @PostMapping
    @Operation(summary = "新增部门")
    public Result<Department> create(@RequestBody Department department) {
        departmentService.save(department);
        return Result.success(department);
    }

    @PutMapping
    @Operation(summary = "修改部门")
    public Result<Department> update(@RequestBody Department department) {
        departmentService.updateById(department);
        return Result.success(department);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门")
    public Result<Void> delete(@PathVariable Long id) {
        departmentService.removeById(id);
        return Result.success();
    }
}
