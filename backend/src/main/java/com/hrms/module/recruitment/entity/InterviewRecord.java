package com.hrms.module.recruitment.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("rec_interview")
public class InterviewRecord extends BaseEntity {
    private Long demandId;
    private String candidateName;
    private String phone;
    private String email;
    private String position;
    private String resumeUrl;
    private LocalDateTime interviewDate;
    private String interviewer;
    private Integer round;
    private Integer result;
    private String comment;
}
