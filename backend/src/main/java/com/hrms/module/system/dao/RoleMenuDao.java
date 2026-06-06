package com.hrms.module.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.system.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuDao extends BaseMapper<RoleMenu> {
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    void deleteByRoleId(@Param("roleId") Long roleId);

    void insertBatch(@Param("list") List<RoleMenu> list);
}
