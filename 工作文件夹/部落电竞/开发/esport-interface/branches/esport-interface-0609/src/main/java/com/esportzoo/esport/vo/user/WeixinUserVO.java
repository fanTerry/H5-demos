package com.esportzoo.esport.vo.user;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-03-07 19:26
 * @Description
 **/
public class WeixinUserVO implements Serializable {

    private static final long serialVersionUID = 6051420534399959885L;

    public String access_token;
    /** 微信用户唯一表示符 */
    public String unionid;
    public String openid;
    public String errcode;
    public String errormsg;
    public String refresh_token;
    /** 微信用户昵称*/
    public String nickname;
    /** 过期时间*/
    private int expires_in;
    /**头像*/
    private String headimgurl;
    /**是否关注*/
    private String subscribe;

    public WeixinUserVO() {
    }

    /**
     * true 表示有错误
     * @return
     */
    public boolean haveError(){
        return StringUtils.isNotBlank(errcode);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

}