package com.zb.service;

import com.zb.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {
    //登录
    public User login( String name,  String pwd);
}
