package com.zb.util;


import com.sun.net.httpserver.Authenticator;

public class DtoUtil {
    private static final String sucess="success";
    private static final String fail="fail";

    public static Dto returnSuccess(String msg,Object data){
        Dto dto=new Dto();
        dto.setData(data);
        dto.setMsg(msg);
        dto.setSuccess(sucess);
        return dto;
    }
    public static Dto returnSuccess(String msg,String success,Object data){
        Dto dto=new Dto();
        dto.setData(data);
        dto.setMsg(msg);
        dto.setSuccess(success);
        return dto;
    }
    public static Dto returnFail(String msg,String errCode){
        Dto dto=new Dto();
        dto.setMsg(msg);
        dto.setSuccess("fail");
        dto.setErrorCode(errCode);
        return dto;
    }
    public static Dto returnFail(String msg,Object obj,String errCode){
        Dto dto=new Dto();
        dto.setMsg(msg);
        dto.setSuccess("fail");
        dto.setData(obj);
        dto.setErrorCode(errCode);
        return dto;
    }
}
