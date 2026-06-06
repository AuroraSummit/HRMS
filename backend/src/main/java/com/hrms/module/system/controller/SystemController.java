package com.hrms.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.system.dao.RoleMenuDao;
import com.hrms.module.system.entity.*;
import com.hrms.module.system.service.*;
import com.hrms.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "系统管理")
@RestController
@RequestMapping("/api/v1/hr/system")
@RequiredArgsConstructor
public class SystemController {

    private final MenuService menuService;
    private final DictTypeService dictTypeService;
    private final DictDataService dictDataService;
    private final OperationLogService operationLogService;
    private final RoleService roleService;
    private final RoleMenuDao roleMenuDao;

    // ===== 菜单管理 =====
    @GetMapping("/menu")
    @Operation(summary = "查询菜单列表")
    public Result<List<Menu>> listMenus() {
        return Result.success(menuService.list());
    }

    @GetMapping("/menu/tree")
    @Operation(summary = "查询菜单树")
    public Result<List<Menu>> getMenuTree() {
        List<Menu> all = menuService.list();

        // Filter menus by current user's role
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId != null) {
            List<Long> roleIds = roleService.getRoleIdsByUserId(userId);
            Set<Long> allowedMenuIds = new HashSet<>();
            for (Long roleId : roleIds) {
                allowedMenuIds.addAll(roleMenuDao.selectMenuIdsByRoleId(roleId));
            }
            if (!allowedMenuIds.isEmpty()) {
                // Also include parent menus so the tree builds correctly
                Set<Long> parentIds = all.stream()
                        .filter(m -> allowedMenuIds.contains(m.getId()) && m.getParentId() != null && m.getParentId() > 0)
                        .map(Menu::getParentId)
                        .collect(Collectors.toSet());
                allowedMenuIds.addAll(parentIds);

                final List<Menu> filtered = all.stream()
                        .filter(m -> allowedMenuIds.contains(m.getId()))
                        .collect(Collectors.toList());
                all = filtered;
            }
        }

        final List<Menu> finalAll = all;
        // Build tree: only return top-level, children embedded
        List<Menu> tree = all.stream()
                .filter(m -> m.getParentId() == null || m.getParentId() == 0)
                .peek(parent -> parent.setChildren(
                        finalAll.stream()
                                .filter(child -> child.getParentId() != null && child.getParentId().equals(parent.getId()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return Result.success(tree);
    }

    @GetMapping("/menu/{id}")
    @Operation(summary = "查询菜单详情")
    public Result<Menu> getMenuById(@PathVariable Long id) {
        return Result.success(menuService.getById(id));
    }

    @PostMapping("/menu")
    @Operation(summary = "新增菜单")
    public Result<Menu> createMenu(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success(menu);
    }

    @PutMapping("/menu")
    @Operation(summary = "修改菜单")
    public Result<Menu> updateMenu(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.success(menu);
    }

    @DeleteMapping("/menu/{id}")
    @Operation(summary = "删除菜单")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.success();
    }

    // ===== 字典类型 =====
    @GetMapping("/dict/type")
    @Operation(summary = "查询字典类型列表")
    public Result<List<DictType>> listDictTypes() {
        return Result.success(dictTypeService.list());
    }

    @GetMapping("/dict/type/list")
    @Operation(summary = "查询字典类型列表(带分页)")
    public Result<List<DictType>> listDictTypesWithPage(@RequestParam(defaultValue = "1") long page,
                                                        @RequestParam(defaultValue = "10") long pageSize) {
        Page<DictType> p = dictTypeService.page(new Page<>(page, pageSize));
        return Result.success(p.getRecords());
    }

    @GetMapping("/dict/type/page")
    @Operation(summary = "分页查询字典类型")
    public PageResult<DictType> pageDictTypes(@RequestParam(defaultValue = "1") long page,
                                               @RequestParam(defaultValue = "10") long pageSize) {
        return PageResult.success(dictTypeService.page(new Page<>(page, pageSize)));
    }

    @PostMapping("/dict/type")
    @Operation(summary = "新增字典类型")
    public Result<DictType> createDictType(@RequestBody DictType dictType) {
        dictTypeService.save(dictType);
        return Result.success(dictType);
    }

    @PutMapping("/dict/type")
    @Operation(summary = "修改字典类型")
    public Result<DictType> updateDictType(@RequestBody DictType dictType) {
        dictTypeService.updateById(dictType);
        return Result.success(dictType);
    }

    @DeleteMapping("/dict/type/{id}")
    @Operation(summary = "删除字典类型")
    public Result<Void> deleteDictType(@PathVariable Long id) {
        dictTypeService.removeById(id);
        return Result.success();
    }

    // ===== 字典数据 =====
    @GetMapping("/dict/data")
    @Operation(summary = "查询字典数据列表")
    public Result<List<DictData>> listDictData(@RequestParam(required = false) Long dictTypeId) {
        LambdaQueryWrapper<DictData> wrapper = new LambdaQueryWrapper<>();
        if (dictTypeId != null) wrapper.eq(DictData::getDictTypeId, dictTypeId);
        wrapper.orderByAsc(DictData::getSort);
        return Result.success(dictDataService.list(wrapper));
    }

    @GetMapping("/dict/data/list")
    @Operation(summary = "查询字典数据列表(别名)")
    public Result<List<DictData>> listDictDataAlias(@RequestParam(required = false) Long dictTypeId) {
        return listDictData(dictTypeId);
    }

    @PostMapping("/dict/data")
    @Operation(summary = "新增字典数据")
    public Result<DictData> createDictData(@RequestBody DictData dictData) {
        dictDataService.save(dictData);
        return Result.success(dictData);
    }

    @PutMapping("/dict/data")
    @Operation(summary = "修改字典数据")
    public Result<DictData> updateDictData(@RequestBody DictData dictData) {
        dictDataService.updateById(dictData);
        return Result.success(dictData);
    }

    @DeleteMapping("/dict/data/{id}")
    @Operation(summary = "删除字典数据")
    public Result<Void> deleteDictData(@PathVariable Long id) {
        dictDataService.removeById(id);
        return Result.success();
    }

    // ===== 操作日志 =====
    @GetMapping("/log/page")
    @Operation(summary = "分页查询操作日志")
    public PageResult<OperationLog> pageLogs(@RequestParam(defaultValue = "1") long page,
                                             @RequestParam(defaultValue = "10") long pageSize,
                                             @RequestParam(required = false) String module,
                                             @RequestParam(required = false) String operation) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (module != null) wrapper.eq(OperationLog::getModule, module);
        if (operation != null) wrapper.like(OperationLog::getOperation, operation);
        wrapper.orderByDesc(OperationLog::getId);
        return PageResult.success(operationLogService.page(new Page<>(page, pageSize), wrapper));
    }

    @DeleteMapping("/log/{id}")
    @Operation(summary = "删除操作日志")
    public Result<Void> deleteLog(@PathVariable Long id) {
        operationLogService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/log/clean")
    @Operation(summary = "清空操作日志")
    public Result<Void> cleanLog() {
        operationLogService.remove(new LambdaQueryWrapper<>());
        return Result.success();
    }
}
