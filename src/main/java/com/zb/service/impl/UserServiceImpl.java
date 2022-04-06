package com.zb.service.impl;

import com.zb.mapper.UserMapper;
import com.zb.pojo.User;
import com.zb.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl  implements UserService {

    @Resource
    UserMapper userMapper;
    @Override
    public User login(String name, String pwd) {
        return userMapper.login(name,pwd);
    }
}
