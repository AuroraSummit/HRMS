package com.hrms.module.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hrms.common.dto.PageQuery;
import com.hrms.module.system.dto.RoleDto;
import com.hrms.module.system.entity.Role;
import com.hrms.module.system.vo.RoleVO;

import java.util.List;

public interface RoleService extends IService<Role> {
    Page<RoleVO> pageQuery(PageQuery pageQuery);

    RoleVO getDetail(Long id);

    void create(RoleDto dto);

    void update(RoleDto dto);

    void delete(Long id);

    void assignMenus(Long roleId, List<Long> menuIds);

    List<Long> getRoleIdsByUserId(Long userId);
}
