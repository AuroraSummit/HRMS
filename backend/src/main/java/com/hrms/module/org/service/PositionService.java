package com.hrms.module.org.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrms.module.org.entity.Position;

import java.util.List;

public interface PositionService extends IService<Position> {
    List<Position> listByDeptId(Long deptId);
}
