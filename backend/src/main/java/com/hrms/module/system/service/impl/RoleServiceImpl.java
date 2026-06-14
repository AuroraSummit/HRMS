package com.hrms.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.system.dao.RoleDao;
import com.hrms.module.system.dao.UserRoleDao;
import com.hrms.module.system.entity.Role;
import com.hrms.module.system.entity.UserRole;
import com.hrms.module.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    private final UserRoleDao userRoleDao;

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return userRoleDao.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
        ).stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }
}
