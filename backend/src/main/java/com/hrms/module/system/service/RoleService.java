package com.hrms.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrms.module.system.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    /**
     * 获取用户关联的角色ID列表
     */
    List<Long> getRoleIdsByUserId(Long userId);
}
