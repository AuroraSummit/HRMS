package com.hrms.module.org.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrms.module.org.entity.Department;
import com.hrms.module.org.vo.DeptTreeVO;

import java.util.List;

public interface DepartmentService extends IService<Department> {
    List<DeptTreeVO> findTree();
}
