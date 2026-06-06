package com.hrms.module.recruitment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.org.dao.DepartmentDao;
import com.hrms.module.org.dao.PositionDao;
import com.hrms.module.org.entity.Department;
import com.hrms.module.org.entity.Position;
import com.hrms.module.recruitment.entity.RecruitmentDemand;
import com.hrms.module.recruitment.service.RecruitmentDemandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "招聘需求管理")
@RestController
@RequestMapping("/api/v1/hr/recruitment/demand")
@RequiredArgsConstructor
public class DemandController {

    private final RecruitmentDemandService demandService;
    private final DepartmentDao departmentDao;
    private final PositionDao positionDao;

    @GetMapping("/page")
    @Operation(summary = "分页查询招聘需求")
    public PageResult<RecruitmentDemand> page(@RequestParam(defaultValue = "1") long page,
                                              @RequestParam(defaultValue = "10") long pageSize) {
        Page<RecruitmentDemand> p = demandService.page(new Page<>(page, pageSize),
                new LambdaQueryWrapper<RecruitmentDemand>().orderByDesc(RecruitmentDemand::getId));
        // 填充部门名称和岗位名称
        for (RecruitmentDemand demand : p.getRecords()) {
            if (demand.getDeptId() != null) {
                Department dept = departmentDao.selectById(demand.getDeptId());
                if (dept != null) demand.setDeptName(dept.getName());
            }
            if (demand.getPositionId() != null) {
                Position pos = positionDao.selectById(demand.getPositionId());
                if (pos != null) demand.setPositionName(pos.getName());
            }
        }
        return PageResult.success(p);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询招聘需求详情")
    public Result<RecruitmentDemand> getById(@PathVariable Long id) {
        return Result.success(demandService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增招聘需求")
    public Result<RecruitmentDemand> create(@RequestBody RecruitmentDemand demand) {
        demandService.save(demand);
        return Result.success(demand);
    }

    @PutMapping
    @Operation(summary = "修改招聘需求")
    public Result<RecruitmentDemand> update(@RequestBody RecruitmentDemand demand) {
        demandService.updateById(demand);
        return Result.success(demand);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除招聘需求")
    public Result<Void> delete(@PathVariable Long id) {
        demandService.removeById(id);
        return Result.success();
    }
}
