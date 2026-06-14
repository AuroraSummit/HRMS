package com.hrms.module.notice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrms.common.result.PageResult;
import com.hrms.common.result.Result;
import com.hrms.module.notice.entity.Notice;
import com.hrms.module.notice.service.NoticeService;
import com.hrms.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "通知公告")
@RestController
@RequestMapping("/api/v1/hr/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/page")
    @Operation(summary = "分页查询通知公告")
    public PageResult<Notice> page(@RequestParam(defaultValue = "1") long page,
                                   @RequestParam(defaultValue = "10") long pageSize,
                                   @RequestParam(required = false) Integer noticeType) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        if (noticeType != null) wrapper.eq(Notice::getNoticeType, noticeType);
        wrapper.eq(Notice::getStatus, 1);
        wrapper.orderByDesc(Notice::getPriority).orderByDesc(Notice::getId);
        Page<Notice> p = noticeService.page(new Page<>(page, pageSize), wrapper);
        return PageResult.success(p);
    }

    @GetMapping("/list")
    @Operation(summary = "查询最新通知(员工门户)")
    public Result<List<Notice>> listLatest() {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, 1)
               .and(w -> w.eq(Notice::getTargetRole, "all").or().eq(Notice::getTargetRole, "employee"))
               .ge(Notice::getExpireDate, LocalDate.now())
               .orderByDesc(Notice::getPriority)
               .orderByDesc(Notice::getId)
               .last("LIMIT 10");
        return Result.success(noticeService.list(wrapper));
    }

    @GetMapping("/admin/list")
    @Operation(summary = "查询所有通知(管理端)")
    public Result<List<Notice>> adminList() {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Notice::getId);
        return Result.success(noticeService.list(wrapper));
    }

    @PostMapping
    @Operation(summary = "发布通知公告")
    public Result<Notice> create(@RequestBody Notice notice) {
        notice.setViewCount(0);
        noticeService.save(notice);
        return Result.success(notice);
    }

    @PutMapping
    @Operation(summary = "修改通知公告")
    public Result<Notice> update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success(notice);
    }

    @PutMapping("/publish/{id}")
    @Operation(summary = "发布/撤回通知")
    public Result<Void> publish(@PathVariable Long id, @RequestParam Integer status) {
        Notice n = noticeService.getById(id);
        if (n != null) {
            n.setStatus(status);
            if (status == 1 && n.getPublishDate() == null) {
                n.setPublishDate(LocalDate.now());
            }
            noticeService.updateById(n);
        }
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查看通知详情")
    public Result<Notice> detail(@PathVariable Long id) {
        Notice notice = noticeService.getById(id);
        if (notice != null) {
            notice.setViewCount((notice.getViewCount() == null ? 0 : notice.getViewCount()) + 1);
            noticeService.updateById(notice);
        }
        return Result.success(notice);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除通知公告")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success();
    }
}
