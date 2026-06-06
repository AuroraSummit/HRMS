package com.hrms.module.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.system.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {
    void deleteByUserId(@Param("userId") Long userId);
}
