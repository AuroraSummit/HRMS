package com.hrms.module.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.contract.dao.ContractDao;
import com.hrms.module.contract.entity.Contract;
import com.hrms.module.contract.service.ContractService;
import org.springframework.stereotype.Service;

@Service
public class ContractServiceImpl extends ServiceImpl<ContractDao, Contract> implements ContractService {
}
