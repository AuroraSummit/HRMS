package com.hrms.module.attendance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.attendance.entity.LeaveApplication;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeaveApplicationDao extends BaseMapper<LeaveApplication> {
}
