package com.xr.mapper;

import com.xr.entity.SysMenu;
import com.xr.entity.SysMenuExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysMenuMapper {
    long countByExample(SysMenuExample example);

    int deleteByExample(SysMenuExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    List<SysMenu> selectByExample(SysMenuExample example);

    SysMenu selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysMenu record, @Param("example") SysMenuExample example);

    int updateByExample(@Param("record") SysMenu record, @Param("example") SysMenuExample example);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);


    @Select("SELECT m.id,m.Name,m.parent_id,m.url,m.perms,m.type,m.icon,m.order_num from sys_role r,sys_role_menu rm,sys_menu m WHERE r.id=rm.role_id AND r.`name` =#{name} and rm.menu_id=m.id")
    public List<SysMenu> findRoleMenus(String name);

}