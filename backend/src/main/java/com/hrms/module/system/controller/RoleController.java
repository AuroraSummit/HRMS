package com.hrms.module.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.dto.PageQuery;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.system.dto.RoleDto;
import com.hrms.module.system.service.RoleService;
import com.hrms.module.system.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/v1/hr/system/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/page")
    @Operation(summary = "分页查询角色列表")
    public PageResult<RoleVO> pageQuery(PageQuery pageQuery) {
        Page<RoleVO> page = roleService.pageQuery(pageQuery);
        return PageResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询角色详情")
    public Result<RoleVO> getDetail(@PathVariable Long id) {
        return Result.success(roleService.getDetail(id));
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public Result<Void> create(@RequestBody RoleDto dto) {
        roleService.create(dto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改角色")
    public Result<Void> update(@RequestBody RoleDto dto) {
        roleService.update(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/menus")
    @Operation(summary = "分配菜单权限")
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        roleService.assignMenus(id, body.get("menuIds"));
        return Result.success();
    }
}
