package com.xr.controller;


import com.xr.entity.RespBean;
import com.xr.entity.SysUser;
import com.xr.service.SysUserService;
import com.xr.util.PoiUtils;
import com.xr.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("user")
@Api("用户管理相关接口")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("info")
    @ApiOperation(value = "获得登录用户信息",notes = "获得登录用户信息")
    public ResponseResult info(String token){
        /*SysUser users = sysUserService.findUserByUserName(token);
        List<String> roles =  sysUserService.findUserRoles(token);
        ResponseResult result = new ResponseResult();
        result.getData().put("roles",roles);
        result.getData().put("introduction",users.getIntroduction());
        result.getData().put("avatar",users.getAvatar());
        result.getData().put("name",users.getUsername());
        return result;*/

        Subject subject=SecurityUtils.getSubject();
        Session session=subject.getSession();
        ResponseResult result=new ResponseResult();
        if(token.equals(subject.getSession().getId().toString())){
            // 从shiro的session里获得保存的用户信息
            SysUser loginUser = (SysUser) session.getAttribute("USER_SESSION");
            // 获得角色字符串集合
            List<String> roles = (List<String>) session.getAttribute("roles");
            if(loginUser!=null){
                // 根据用户获得角色字符串数组
                roles = sysUserService.findUserRoles(loginUser.getUsername());
                // 将信息存入到session
                result.getData().put("roles",roles);
                result.getData().put("introduction",loginUser.getIntroduction());
                result.getData().put("avatar",loginUser.getAvatar());
                result.getData().put("name",loginUser.getUsername());
                return result;
            }
        }
        return null;


    }


    @RequestMapping("login")
    @ApiOperation(value = "用户登录",notes = "用户登录")
    public ResponseResult login(SysUser sysUser){

       /* SysUser loginUser = sysUserService.login(sysUser);
        ResponseResult result = new ResponseResult();
        if(loginUser!=null){
            result.getData().put("token",loginUser.getUsername());
        }else{
            result.getData().put("message","登录失败，用户名或密码错误");
        }
        return result;*/

        ResponseResult result = new ResponseResult();
        UsernamePasswordToken token = new UsernamePasswordToken(sysUser.getUsername(), sysUser.getPassword());
        // 获得登录的主题
        Subject subject = SecurityUtils.getSubject();
        // 调用登录方法
        subject.login(token);
        // 返回给前台的toke，唯一标识用户
        result.getData().put("token",subject.getSession().getId());
        return result;


    }

    private List<SysUser> list2;

    @RequestMapping("list")
    @RequiresPermissions("user:list")
    @ApiOperation(value = "获得用户列表",notes = "获得用户列表")
    public ResponseResult query(SysUser sysUser, Integer page,Integer limit){

        String username= sysUser.getUsername();
        List<SysUser> list1 =sysUserService.selectpage(username,(page-1)*limit,limit);
        List<SysUser> list = sysUserService.list(sysUser);
        list2=list;
        ResponseResult result = new ResponseResult();
        result.getData().put("items",list1);
        result.getData().put("total",list.size());
        return result;

    }

   @RequestMapping("add")
   @RequiresPermissions("user:add")
   @ApiOperation(value = "添加用户",notes = "添加用户")
    public ResponseResult add(SysUser sysUser){

       //生成盐（部分，需要存入数据库中）
       String salt=new SecureRandomNumberGenerator().nextBytes().toHex();
       //将原始密码加盐（上面生成的盐），并且用md5算法加密两次，将最后结果存入数据库中
       String password = new Md5Hash(sysUser.getPassword(),salt,2).toString();
       sysUser.setSalt(salt);
       sysUser.setPassword(password);

       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       java.util.Date time=null;
       try {
           time= sdf.parse(sdf.format(new Date()));
       } catch (ParseException e) {

           e.printStackTrace();
       }
       //如果CreateTime为空，就给他赋值为当前时间
        sysUser.setCreateTime(time);
        sysUserService.insert(sysUser);
        ResponseResult result =new ResponseResult();
        result.getData().put("message","添加成功");

        return result;
   }

    @RequestMapping("update")
    @RequiresPermissions("user:update")
    @ApiOperation(value = "修改用户",notes = "修改用户")
    public ResponseResult update(SysUser sysUser){

        //生成盐（部分，需要存入数据库中）
        String salt=new SecureRandomNumberGenerator().nextBytes().toHex();
        //将原始密码加盐（上面生成的盐），并且用md5算法加密两次，将最后结果存入数据库中
        String password = new Md5Hash(sysUser.getPassword(),salt,2).toString();
        sysUser.setSalt(salt);
        sysUser.setPassword(password);

        sysUserService.updateByPrimaryKey(sysUser);
        ResponseResult result =new ResponseResult();
        result.getData().put("message","修改成功");

        return result;
    }

    @RequestMapping("delete")
    @RequiresPermissions("user:delete")
    @ApiOperation(value = "删除用户",notes = "删除用户")
    public ResponseResult delete(Long id){


        sysUserService.deleteByPrimaryKey(id);
        ResponseResult result =new ResponseResult();
        result.getData().put("message","删除成功");

        return result;
    }


    @RequestMapping("logout")
    @ApiOperation(value = "注销用户",notes = "注销用户")
    public ResponseResult logout(){
        ResponseResult result = new ResponseResult();
        Subject subject = SecurityUtils.getSubject();

        // 只需调用shiro的logout方法就可以了
        subject.logout();
        return result;
    }



    @RequestMapping("export")
    //ResponseEntity里面装了所有响应的数据
    public ResponseEntity<byte[]> exportExcel() throws IOException {
        return PoiUtils.exportJobLevelExcel(list2);
    }


    @RequestMapping("import")
    public RespBean importData(MultipartFile file, HttpServletRequest req) throws IOException {
        System.out.println("进入import方法");
        System.out.println(file);


        List<SysUser> sysUsers=PoiUtils.parseFile2List(file);

        for (SysUser sysUserss : sysUsers) {
            System.out.println(sysUserss);
        }

        //根据上传的职称级别信息文件，批量添加数据
        if (sysUserService.addBatchSysUser(sysUsers)==1){
            return RespBean.ok("批量导入成功！");
        }else {
            return RespBean.error("批量导入失败！");
        }
    }


}
