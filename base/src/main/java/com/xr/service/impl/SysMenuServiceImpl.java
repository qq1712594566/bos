package com.xr.service.impl;


import com.xr.entity.SysMenu;
import com.xr.mapper.SysMenuMapper;
import com.xr.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findRoleMenus(String name) {
        return sysMenuMapper.findRoleMenus(name);
    }
}
