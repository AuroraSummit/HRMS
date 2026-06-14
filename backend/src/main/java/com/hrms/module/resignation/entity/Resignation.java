package com.hrms.module.resignation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("emp_resignation")
public class Resignation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long empId;
    private LocalDate applyDate;
    private LocalDate resignDate;
    private LocalDate actualResignDate;
    /** 离职类型(1主动离职 2被动离职 3退休 4其他) */
    private Integer resignType;
    private String reason;
    private String handoverTo;
    /** 交接状态(0未交接 1交接中 2已完成) */
    private Integer handoverStatus;
    /** 资产归还(0未归还 1已归还) */
    private Integer assetReturn;
    /** 薪资结算(0未结算 1已结算) */
    private Integer settlement;
    /** 状态(0待审批 1已通过 2已驳回 3已撤回) */
    private Integer status;
    private String approveComment;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
