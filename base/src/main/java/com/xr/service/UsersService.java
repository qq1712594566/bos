package com.xr.service;

import com.xr.entity.Users;
import com.xr.mapper.UsersMapper;

public interface UsersService {
    /**
     * 用户登录
     * @param users
     * @return
     */
   public Users login(Users users);
}
