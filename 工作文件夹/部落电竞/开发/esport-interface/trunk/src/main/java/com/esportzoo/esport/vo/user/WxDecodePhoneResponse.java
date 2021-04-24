package com.esportzoo.esport.vo.user;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/08/02
 */
public class WxDecodePhoneResponse implements Serializable {

	private static final long serialVersionUID = -6131239905032306452L;
	
	/** 用户绑定的手机号（国外手机号会有区号） */
	private String phoneNumber;
	/** 没有区号的手机号 */
	private String purePhoneNumber;
	/** 区号 */
	private String countryCode;
	/** openid */
	private String openid;
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getPurePhoneNumber() {
		return purePhoneNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public String getOpenid() {
		return openid;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setPurePhoneNumber(String purePhoneNumber) {
		this.purePhoneNumber = purePhoneNumber;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
