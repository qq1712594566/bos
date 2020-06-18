package com.xr.service.impl;

import com.xr.entity.SysRole;
import com.xr.mapper.SysRoleMapper;
import com.xr.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> findUserByRoless(String username) {
        return sysRoleMapper.findUserByRoless(username);
    }
}
