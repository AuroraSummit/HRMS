package com.hrms.module.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.system.dao.DictTypeDao;
import com.hrms.module.system.entity.DictType;
import com.hrms.module.system.service.DictTypeService;
import org.springframework.stereotype.Service;

@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeDao, DictType> implements DictTypeService {
}
