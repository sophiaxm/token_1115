package com.zb.controller;

import com.zb.pojo.User;
import com.zb.service.TokenService;
import com.zb.service.UserService;
import com.zb.util.Dto;
import com.zb.util.DtoUtil;
import com.zb.vo.TokenVo;
import jdk.nashorn.internal.parser.Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    @Resource
    UserService userService;

    @Resource
    TokenService tokenService;

    @RequestMapping("/login")
    @ResponseBody
    public Dto login(String name, String pwd, HttpServletRequest request){
        //调用登录业务
        User user = userService.login(name, pwd);
        if(null!=user) {
            //获取用户代理字符串
            String userAgent = request.getHeader("User-Agent");
            System.out.println(userAgent);
            //调用业务，获取token字符串
            String token = tokenService.genToken(userAgent, user);
            //保存到redis
            tokenService.saveRedis(token,user);
            //封装tokenvo
            TokenVo tokenVo=new TokenVo();
            tokenVo.setToken(token);
            tokenVo.setGenTime(System.currentTimeMillis());
            tokenVo.setExpTime(System.currentTimeMillis()+2*60*60*1000);//毫秒
            return DtoUtil.returnSuccess("登录成功",tokenVo);
        }else{
            return DtoUtil.returnFail("登录失败","10001");
        }
    }


    //根据token获取登录对象
    @RequestMapping("/getUser")
    @ResponseBody
    public Dto getUser(HttpServletRequest request){
        //从请求头中获取令牌token
        String token = request.getHeader("token");
        //调用业务
        User user = tokenService.getUserByRedis(token);
        if(null!=user){
            return DtoUtil.returnSuccess("获取成功",user);
        }else{
            return DtoUtil.returnFail("获取失败，没有登录","10002");
        }
    }



    //置换请求
    @RequestMapping("/reload")
    @ResponseBody
    public Dto reload(HttpServletRequest request){
        //从请求头中获取用户代理字符串
        String userAgent=request.getHeader("User-Agent");
        //从请求头中获取token
        String token = request.getHeader("token");
        try {
            //调用置换业务
            String newToken = tokenService.reload(token, userAgent);
            //封装tokenvo
            TokenVo tokenVo = new TokenVo();
            tokenVo.setToken(newToken);
            tokenVo.setGenTime(System.currentTimeMillis());
            tokenVo.setExpTime(System.currentTimeMillis() + 2 * 60 * 60 * 1000);//毫秒
            return DtoUtil.returnSuccess("置换成功", tokenVo);
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("置换失败",e.getMessage(),"10003");
        }
    }
}
