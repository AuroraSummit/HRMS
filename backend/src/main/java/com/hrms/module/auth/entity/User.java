package com.hrms.module.auth.entity;

import com.hrms.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("auth_user")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private String avatar;
    private Integer status;
    private Integer type;

    /** 关联员工ID */
    private Long empId;
}
