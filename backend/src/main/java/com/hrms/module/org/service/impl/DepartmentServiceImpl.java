package com.hrms.module.org.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.org.dao.DepartmentDao;
import com.hrms.module.org.entity.Department;
import com.hrms.module.org.service.DepartmentService;
import com.hrms.module.org.vo.DeptTreeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentDao, Department> implements DepartmentService {

    @Override
    public List<DeptTreeVO> findTree() {
        List<Department> allDepts = list();
        List<Department> roots = allDepts.stream()
                .filter(d -> d.getParentId() == null || d.getParentId() == 0)
                .sorted((a, b) -> {
                    if (a.getSort() == null) return 1;
                    if (b.getSort() == null) return -1;
                    return a.getSort().compareTo(b.getSort());
                })
                .collect(Collectors.toList());

        List<DeptTreeVO> tree = new ArrayList<>();
        for (Department root : roots) {
            tree.add(buildTree(root, allDepts));
        }
        return tree;
    }

    private DeptTreeVO buildTree(Department dept, List<Department> allDepts) {
        DeptTreeVO vo = new DeptTreeVO();
        vo.setId(dept.getId());
        vo.setLabel(dept.getName());

        List<Department> children = allDepts.stream()
                .filter(d -> dept.getId().equals(d.getParentId()))
                .sorted((a, b) -> {
                    if (a.getSort() == null) return 1;
                    if (b.getSort() == null) return -1;
                    return a.getSort().compareTo(b.getSort());
                })
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
            List<DeptTreeVO> childVOs = new ArrayList<>();
            for (Department child : children) {
                childVOs.add(buildTree(child, allDepts));
            }
            vo.setChildren(childVOs);
        }

        return vo;
    }
}
