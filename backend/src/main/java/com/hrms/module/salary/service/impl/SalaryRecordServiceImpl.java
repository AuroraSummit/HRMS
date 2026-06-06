package com.hrms.module.salary.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.salary.dao.SalaryRecordDao;
import com.hrms.module.salary.entity.SalaryRecord;
import com.hrms.module.salary.service.SalaryRecordService;
import org.springframework.stereotype.Service;

@Service
public class SalaryRecordServiceImpl extends ServiceImpl<SalaryRecordDao, SalaryRecord> implements SalaryRecordService {
}
