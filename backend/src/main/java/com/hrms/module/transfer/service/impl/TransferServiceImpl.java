package com.hrms.module.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.transfer.dao.TransferDao;
import com.hrms.module.transfer.entity.Transfer;
import com.hrms.module.transfer.service.TransferService;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl extends ServiceImpl<TransferDao, Transfer> implements TransferService {
}
