package com.hrms.module.system.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_operation_log")
public class OperationLog extends BaseEntity {
    private String module;
    private String operation;
    private String requestUrl;
    private String requestMethod;
    private String requestParam;
    private String responseData;
    private String ip;
    private Long duration;
    private String operator;
}
