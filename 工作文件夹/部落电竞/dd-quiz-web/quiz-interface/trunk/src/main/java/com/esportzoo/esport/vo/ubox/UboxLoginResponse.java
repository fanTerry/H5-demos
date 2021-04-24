package com.esportzoo.esport.vo.ubox;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/06/10
 */
public class UboxLoginResponse implements Serializable {

	private static final long serialVersionUID = 5039890691376154772L;
	
	private String code;
	private String session_key;
	private String openid;
	private String unionid;
	private String ubox_session;
	private UboxUserInfo userinfo;
	
	
	public String getCode() {
		return code;
	}
	public String getSession_key() {
		return session_key;
	}
	public String getOpenid() {
		return openid;
	}
	public String getUnionid() {
		return unionid;
	}
	public String getUbox_session() {
		return ubox_session;
	}
	public UboxUserInfo getUserinfo() {
		return userinfo;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public void setUbox_session(String ubox_session) {
		this.ubox_session = ubox_session;
	}
	public void setUserinfo(UboxUserInfo userinfo) {
		this.userinfo = userinfo;
	}
	
}
