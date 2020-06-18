package com.xr.mapper;

import com.xr.entity.SysUser;
import com.xr.entity.SysUserExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper {
    /**
     * 根据用户名查询角色集合
     * @param username
     * @return
     */
    public List<String> findUserRoles(String username);
    long countByExample(SysUserExample example);
    int deleteByExample(SysUserExample example);


    //删除
    int deleteByPrimaryKey(Long id);
    //新增
    int insert(SysUser record);
    //修改 找id
    SysUser selectByPrimaryKey(Long id);
    //查到后修改
    int updateByPrimaryKey(SysUser record);


    int insertSelective(SysUser record);

    /*@Select("select id,username,password,salt,email,mobile,status,dept_id deptId,create_by createBy,create_time createTime,last_update_by lastUpdateBy,last_update_time lastUpdateTime from sys_user where username limit #{page},#{limit}")
    List<SysUser> selectpage(@Param("username") String username,@Param("page") Integer page,@Param("limit") Integer limit);*/

    @Select({"<script>",
            "select id,username,password,salt,email,mobile,status,dept_id deptId,create_by createBy,create_time createTime,last_update_by lastUpdateBy,last_update_time lastUpdateTime from sys_user where 1=1",
            "<when test='username!=null'>",
            "and username like '%${username}%'",
            "</when>",
            "limit #{page},#{limit}",
            "</script>"})
    List<SysUser> selectpage(@Param("username") String username,@Param("page") Integer page,@Param("limit") Integer limit);

    List<SysUser> selectByExample(SysUserExample example);


    int updateByExampleSelective(@Param("record") SysUser record, @Param("example") SysUserExample example);
    int updateByExample(@Param("record") SysUser record, @Param("example") SysUserExample example);
    int updateByPrimaryKeySelective(SysUser record);

    Integer addBatchSysUser(@Param("sysuser") List<SysUser> sysUsers);

}
