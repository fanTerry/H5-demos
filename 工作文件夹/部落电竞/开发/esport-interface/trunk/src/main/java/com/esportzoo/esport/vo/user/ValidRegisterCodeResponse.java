package com.esportzoo.esport.vo.user;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/07/16
 */
public class ValidRegisterCodeResponse implements Serializable {

	private static final long serialVersionUID = 4869946512761877642L;
	
	private String phone;
	private String code;
	private boolean validPass;
	
	public String getPhone() {
		return phone;
	}
	public String getCode() {
		return code;
	}
	public boolean isValidPass() {
		return validPass;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setValidPass(boolean validPass) {
		this.validPass = validPass;
	}
	
}
