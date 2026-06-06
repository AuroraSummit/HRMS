package com.hrms.module.org.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.org.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentDao extends BaseMapper<Department> {
}
