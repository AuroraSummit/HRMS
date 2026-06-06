package com.hrms.module.org.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.org.dao.DepartmentDao;
import com.hrms.module.org.dao.PositionDao;
import com.hrms.module.org.entity.Department;
import com.hrms.module.org.entity.Position;
import com.hrms.module.org.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl extends ServiceImpl<PositionDao, Position> implements PositionService {

    private final DepartmentDao departmentDao;

    @Override
    public List<Position> listByDeptId(Long deptId) {
        List<Position> list = list(new LambdaQueryWrapper<Position>()
                .eq(Position::getDeptId, deptId)
                .orderByAsc(Position::getSort));
        populateDeptName(list);
        return list;
    }

    @Override
    public List<Position> list() {
        List<Position> list = super.list();
        populateDeptName(list);
        return list;
    }

    private void populateDeptName(List<Position> positions) {
        if (positions == null || positions.isEmpty()) return;
        for (Position pos : positions) {
            if (pos.getDeptId() != null) {
                Department dept = departmentDao.selectById(pos.getDeptId());
                if (dept != null) {
                    pos.setDeptName(dept.getName());
                }
            }
        }
    }
}
