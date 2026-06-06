package com.hrms.module.attendance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.attendance.dao.LeaveApplicationDao;
import com.hrms.module.attendance.entity.LeaveApplication;
import com.hrms.module.attendance.service.LeaveApplicationService;
import org.springframework.stereotype.Service;

@Service
public class LeaveApplicationServiceImpl extends ServiceImpl<LeaveApplicationDao, LeaveApplication> implements LeaveApplicationService {
}
