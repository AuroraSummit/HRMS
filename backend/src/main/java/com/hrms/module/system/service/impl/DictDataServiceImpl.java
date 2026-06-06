package com.hrms.module.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.system.dao.DictDataDao;
import com.hrms.module.system.entity.DictData;
import com.hrms.module.system.service.DictDataService;
import org.springframework.stereotype.Service;

@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataDao, DictData> implements DictDataService {
}
