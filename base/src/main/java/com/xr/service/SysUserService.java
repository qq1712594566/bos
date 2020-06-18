package com.xr.service;

import com.xr.entity.SysUser;
import java.util.List;

public interface SysUserService {
    /**
     * 用户登录
     * @param sysUser
     * @return
     */
    public SysUser login(SysUser sysUser);

    /**
     * 根据用户名查询用户信息
     * @return
     */
    public SysUser findUserByUserName(String username);

    /**
     * 根据用户名获得角色的集合
     * @return
     */
    public List<String> findUserRoles(String username);

    //查询的方法
    List<SysUser> list(SysUser sysUser);


    //删除
    int deleteByPrimaryKey(Long id);

    //新增
    int insert(SysUser record);

    //修改 找id
    SysUser selectByPrimaryKey(Long id);

    //查到后修改
    int updateByPrimaryKey(SysUser record);

    List<SysUser> selectpage(String username,Integer page,Integer limit);

    public Integer addBatchSysUser(List<SysUser> s);

}
