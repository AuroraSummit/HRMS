package com.hrms.module.contract.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ctr_contract")
public class Contract extends BaseEntity {
    private Long empId;
    private Integer contractType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate signDate;
    private String fileUrl;
    private Integer status;
    private String remark;
}
