package com.zb.service.impl;

import com.alibaba.fastjson.JSON;
import com.zb.pojo.User;
import com.zb.service.TokenService;
import com.zb.util.MD5;
import com.zb.util.RedisUtil;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    //redisUtil对象
    @Resource
    RedisUtil redisUtil;


    String tokenPrefix="token:";
    @Override
    public String genToken(String userAgent, User user) {
        StringBuffer sb=new StringBuffer(tokenPrefix);
        //获取用户代理对象
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        if(agent.getOperatingSystem().isMobileDevice()){
            sb.append("MOBILE-");
        }else{
            sb.append("PC-");
        }
        //加密
        sb.append(MD5.getMd5(user.getName(),32)+"-");
        sb.append(user.getId()+"-");
        //时间
        sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"-");
        sb.append(MD5.getMd5(userAgent,6));

        return sb.toString();
    }

    @Override
    public void saveRedis(String token, User user) {
        if(token.startsWith(tokenPrefix+"PC-")){
            redisUtil.set(token, JSON.toJSONString(user),2*60*60);//秒
        }else {
            redisUtil.set(token, JSON.toJSONString(user));
        }
    }

    @Override
    public User getUserByRedis(String token) {
        if(redisUtil.hasKey(token)){
            //从token获取
            String jsonStr = redisUtil.get(token).toString();
            //从jsonStr转成User对象
            User user = JSON.parseObject(jsonStr, User.class);
            return user;
        }
        return null;
    }

    @Override
    public boolean delete(String token) {
        return false;
    }

    /**
     * 置换
     * @param token 原来的token字符串
     * @param userAgent 用户代理
     * @return
     */
    @Override
    public String reload(String token, String userAgent) throws  Exception {
        // 根据原有的token获取用户对象
        if(!redisUtil.hasKey(token)){
            throw  new Exception("令牌错误");
        }
        //token生成的时间
        String time=token.split("-")[3];
        System.out.println(time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
        Date data = sdf.parse(time);
        //去用时间=现在时间-生成时间
       long diff= System.currentTimeMillis()-data.getTime();//毫秒
        //剩余时间
        long shengTime=2*60*60*1000-diff;//2 小时
       long reloadTime=5*60*1000;//5 分钟
        System.out.println("剩余的时间:"+shengTime/1000/60+"分钟");
        if(shengTime>reloadTime){
            throw  new Exception("剩余时间超过5分钟，不能置换");
        }
        //从token获取
        String jsonStr = redisUtil.get(token).toString();
        //从jsonStr转成User对象
        User user = JSON.parseObject(jsonStr, User.class);
        // 调用genToken生成新的token
        String newToken = this.genToken(userAgent, user);
        //调用saveRedis保存到redis
        this.saveRedis(newToken,user);
        //删除
       // this.delete(token);
        return newToken;
    }
}
