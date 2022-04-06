package com.zb.service;

import com.zb.pojo.User;

/**
 * token业务
 */
public interface TokenService {

    //生成token
    public String genToken(String userAgent, User user);
    //保存到redis
    public void saveRedis(String token,User user);

    //从redis获取对象
    public User getUserByRedis(String token);


    //从redis中删除token
    public boolean delete(String token);

    //置换
    public String reload(String token,String UserAgent) throws  Exception;

}
