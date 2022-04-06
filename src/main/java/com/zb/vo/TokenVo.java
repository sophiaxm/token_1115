package com.zb.vo;

/**
 * token的视图结构
 */
public class TokenVo {
    private String token ;//token字符串，唯一的标识
    private long genTime;//生成时间
    private long expTime;//有效期时间

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getGenTime() {
        return genTime;
    }

    public void setGenTime(long genTime) {
        this.genTime = genTime;
    }

    public long getExpTime() {
        return expTime;
    }

    public void setExpTime(long expTime) {
        this.expTime = expTime;
    }
}
