package com.hrms.module.attendance.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("att_leave")
public class LeaveApplication extends BaseEntity {
    private Long empId;
    private Integer leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double duration;
    private String reason;
    private Integer status;

    @TableField(exist = false)
    private String employeeName;
}
