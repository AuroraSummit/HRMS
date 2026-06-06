package com.hrms.module.performance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.performance.dao.PerformancePlanDao;
import com.hrms.module.performance.entity.PerformancePlan;
import com.hrms.module.performance.service.PerformancePlanService;
import org.springframework.stereotype.Service;

@Service
public class PerformancePlanServiceImpl extends ServiceImpl<PerformancePlanDao, PerformancePlan> implements PerformancePlanService {
}
