package com.xr.service.impl;

import com.xr.entity.SysUser;
import com.xr.entity.SysUserExample;
import com.xr.mapper.SysUserMapper;
import com.xr.service.SysUserService;
import com.xr.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser login(SysUser sysUser) {
        // 构建自定义查询条件

        SysUserExample example = new SysUserExample();

        //添加用户和修改用户时，盐和密码都需要获得，并存入数据库，密码的加密次数必须与shiro的配置类里的加密次数一致，不然密码校验不能通过
        String md5Password = CommonUtil.getMD5Password(sysUser.getPassword(), sysUser.getSalt(), 2);

        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(sysUser.getUsername());
        criteria.andPasswordEqualTo(md5Password);

        List<SysUser> list =  sysUserMapper.selectByExample(example);
        if(list!=null && list.size()==1){
            return list.get(0);
        }
        return  null;
    }

    @Override
    public SysUser findUserByUserName(String username) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<SysUser> list =  sysUserMapper.selectByExample(example);

        if(list!=null && list.size()==1){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<String> findUserRoles(String username) {
        List<String> roles = sysUserMapper.findUserRoles(username);
        return roles;
    }


    @Override
    public int deleteByPrimaryKey(Long id) {
       return sysUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SysUser record) {
        return sysUserMapper.insert(record);
    }

    @Override
    public SysUser selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public int updateByPrimaryKey(SysUser record) {
        return sysUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<SysUser> list(SysUser sysUser) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        if(sysUser!=null){
            if(sysUser.getUsername()!=null){
                criteria.andUsernameLike("%"+sysUser.getUsername()+"%");
            }
            // 还可以添加其他属性的条件
        }
        List<SysUser> list =  sysUserMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<SysUser> selectpage(String username,Integer page, Integer limit) {

        return sysUserMapper.selectpage(username,page, limit);
    }

    public Integer addBatchSysUser(List<SysUser> s) {
        for (SysUser ss : s) {
            ss.setCreateTime(new Date());
        }
        return sysUserMapper.addBatchSysUser(s);
    }

}