package com.hrms.module.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.system.dao.MenuDao;
import com.hrms.module.system.entity.Menu;
import com.hrms.module.system.service.MenuService;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {
}
