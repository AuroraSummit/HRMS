package com.hrms.module.recruitment.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("rec_offer")
public class OfferRecord extends BaseEntity {
    private Long demandId;
    private String candidateName;
    private Double offerSalary;
    private LocalDate offerDate;
    private LocalDate expiryDate;
    private Integer status;
}
