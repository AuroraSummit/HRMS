package com.hrms.module.transfer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("emp_transfer")
public class Transfer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long empId;
    /** 异动类型(1晋升 2平调 3降职 4转岗 5其他) */
    private Integer transferType;
    private Long fromDeptId;
    private Long toDeptId;
    private Long fromPositionId;
    private Long toPositionId;
    private BigDecimal fromSalary;
    private BigDecimal toSalary;
    private LocalDate transferDate;
    private String reason;
    private String remark;
    /** 状态(0待审批 1已通过 2已驳回) */
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
