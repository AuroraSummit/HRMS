package com.hrms.module.probation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("emp_probation")
public class Probation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long empId;
    private LocalDate probationStart;
    private LocalDate probationEnd;
    private LocalDate actualEnd;
    private String selfEvaluation;
    private String mentorName;
    private String mentorEvaluation;
    private String managerEvaluation;
    private BigDecimal score;
    /** 转正结果(1通过 2延期 3不通过) */
    private Integer result;
    /** 状态(0待评估 1已提交 2已通过 3已驳回) */
    private Integer status;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
