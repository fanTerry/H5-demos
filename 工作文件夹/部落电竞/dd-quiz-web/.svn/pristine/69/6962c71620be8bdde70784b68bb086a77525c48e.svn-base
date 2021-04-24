package com.esportzoo.esport.connect.request;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/08/02
 */
public class WxDecodePhoneRequest implements Serializable {

	private static final long serialVersionUID = -1346254452679835743L;
	
	private String iv;
	private String encryptedData;
	//如果前端检查会话未过期，会传sid来,用来取sessionKey
	private String sid;
	
	
	public String getIv() {
		return iv;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public String getSid() {
		return sid;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}

}
