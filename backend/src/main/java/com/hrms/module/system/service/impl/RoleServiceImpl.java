package com.hrms.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.common.dto.PageQuery;
import com.hrms.common.exception.BusinessException;
import com.hrms.module.system.dao.RoleDao;
import com.hrms.module.system.dao.RoleMenuDao;
import com.hrms.module.system.dao.UserRoleDao;
import com.hrms.module.system.dto.RoleDto;
import com.hrms.module.system.entity.Role;
import com.hrms.module.system.entity.RoleMenu;
import com.hrms.module.system.entity.UserRole;
import com.hrms.module.system.service.RoleService;
import com.hrms.module.system.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    private final RoleDao roleDao;
    private final RoleMenuDao roleMenuDao;
    private final UserRoleDao userRoleDao;

    @Override
    public Page<RoleVO> pageQuery(PageQuery pageQuery) {
        Page<Role> page = roleDao.selectPage(pageQuery.toPage(), null);
        Page<RoleVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public RoleVO getDetail(Long id) {
        Role role = roleDao.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return toVO(role);
    }

    @Override
    @Transactional
    public void create(RoleDto dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        role.setDescription(dto.getDescription());
        role.setStatus(dto.getStatus());
        role.setSort(dto.getSort());
        roleDao.insert(role);

        if (dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            assignMenus(role.getId(), dto.getMenuIds());
        }
    }

    @Override
    @Transactional
    public void update(RoleDto dto) {
        Role role = roleDao.selectById(dto.getId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        role.setDescription(dto.getDescription());
        role.setStatus(dto.getStatus());
        role.setSort(dto.getSort());
        roleDao.updateById(role);

        if (dto.getMenuIds() != null) {
            assignMenus(role.getId(), dto.getMenuIds());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        roleDao.deleteById(id);
        roleMenuDao.deleteByRoleId(id);
        userRoleDao.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, id));
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMenuDao.deleteByRoleId(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            List<RoleMenu> list = new ArrayList<>();
            for (Long menuId : menuIds) {
                RoleMenu rm = new RoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                list.add(rm);
            }
            roleMenuDao.insertBatch(list);
        }
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return userRoleDao.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
        ).stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    private RoleVO toVO(Role role) {
        RoleVO vo = new RoleVO();
        vo.setId(role.getId());
        vo.setName(role.getName());
        vo.setCode(role.getCode());
        vo.setDescription(role.getDescription());
        vo.setStatus(role.getStatus());
        vo.setSort(role.getSort());
        vo.setCreateTime(role.getCreateTime());
        vo.setUpdateTime(role.getUpdateTime());
        List<Long> menuIds = roleMenuDao.selectMenuIdsByRoleId(role.getId());
        vo.setMenuIds(menuIds);
        return vo;
    }
}
