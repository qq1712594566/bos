package com.xr.mapper;

import com.xr.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//@Mapper
public interface UsersMapper {

    @Select("select id,name,password from users where name=#{name} and password=#{password}")
    public Users findUserByNameAndPassword(Users loginUser);
}
