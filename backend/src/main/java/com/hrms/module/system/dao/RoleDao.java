package com.hrms.module.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDao extends BaseMapper<Role> {
    List<Role> selectByUserId(@Param("userId") Long userId);
}
