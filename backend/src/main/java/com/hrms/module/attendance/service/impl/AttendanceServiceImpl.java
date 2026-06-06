package com.hrms.module.attendance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.attendance.dao.AttendanceDao;
import com.hrms.module.attendance.entity.Attendance;
import com.hrms.module.attendance.service.AttendanceService;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceDao, Attendance> implements AttendanceService {
}
