package com.hrms.module.notice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("sys_notice")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    /** 类型(1公司公告 2制度通知 3活动通知 4系统通知) */
    private Integer noticeType;
    /** 优先级(0普通 1重要 2紧急) */
    private Integer priority;
    private String publisher;
    private LocalDate publishDate;
    private LocalDate expireDate;
    /** 目标角色(all/admin/employee) */
    private String targetRole;
    /** 状态(0草稿 1已发布 2已撤回) */
    private Integer status;
    private Integer viewCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
