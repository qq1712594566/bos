package com.xr.service;

import com.xr.entity.SysMenu;

import java.util.List;

public interface SysMenuService {

    public List<SysMenu> findRoleMenus(String name);
}
