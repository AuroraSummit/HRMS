package com.hrms.module.salary.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.salary.dao.SalaryConfigDao;
import com.hrms.module.salary.entity.SalaryConfig;
import com.hrms.module.salary.service.SalaryConfigService;
import org.springframework.stereotype.Service;

@Service
public class SalaryConfigServiceImpl extends ServiceImpl<SalaryConfigDao, SalaryConfig> implements SalaryConfigService {
}
