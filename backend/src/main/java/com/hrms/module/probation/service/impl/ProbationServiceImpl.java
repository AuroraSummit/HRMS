package com.hrms.module.probation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.probation.dao.ProbationDao;
import com.hrms.module.probation.entity.Probation;
import com.hrms.module.probation.service.ProbationService;
import org.springframework.stereotype.Service;

@Service
public class ProbationServiceImpl extends ServiceImpl<ProbationDao, Probation> implements ProbationService {
}
