package com.xr.service;

import com.xr.entity.SysRole;

import java.util.List;

public interface SysRoleService {

    public List<SysRole> findUserByRoless(String username);
}
