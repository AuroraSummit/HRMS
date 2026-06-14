package com.hrms.module.resignation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.resignation.dao.ResignationDao;
import com.hrms.module.resignation.entity.Resignation;
import com.hrms.module.resignation.service.ResignationService;
import org.springframework.stereotype.Service;

@Service
public class ResignationServiceImpl extends ServiceImpl<ResignationDao, Resignation> implements ResignationService {
}
