package com.xr.controller;

import com.xr.entity.Users;
import com.xr.service.UsersService;
import com.xr.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("user1")
public class UsersController {

    @Autowired
    private UsersService usersService;

    /*@RequestMapping("login")
    public ResponseEntity login(@RequestBody Users users, HttpSession session){
        Users loginUser = usersService.login(users);
        System.out.println(users);
        Map<String,Object> map = new HashMap<>();
        if(loginUser!=null){
            map.put("msg","登录成功");
            map.put("loginUser",loginUser);
            map.put("token",session.getId());
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }else{
            map.put("msg","登录失败，用户名或密码错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }*/

    @RequestMapping("login")
    /**
     * 用户登录
     */
    public ResponseResult login(@RequestBody Users users, HttpSession session){
        Users loginUser = usersService.login(users);
        System.out.println(users);
        ResponseResult result = new ResponseResult();
        if(loginUser!=null){
            session.setAttribute("loginUser",loginUser);
            // 登录成功，返回前端的消息
            result.getData().put("message","登录成功");
            // 前台唯一标识
            result.getData().put("token",loginUser.getName());
        }else{
            //登录失败，返回前端的消息
            result.getData().put("message","登录失败，用户名或密码错误");
        }
        return result;
    }
}
