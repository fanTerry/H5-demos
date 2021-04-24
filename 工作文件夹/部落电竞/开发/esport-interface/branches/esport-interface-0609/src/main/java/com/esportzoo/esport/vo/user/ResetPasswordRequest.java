package com.esportzoo.esport.vo.user;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/07/17
 */
public class ResetPasswordRequest implements Serializable {

	private static final long serialVersionUID = -7512004611872278298L;
	
	private String phone;
	private String code;
	private String password;
	private String repetPassword;
	
	public String getPhone() {
		return phone;
	}
	public String getCode() {
		return code;
	}
	public String getPassword() {
		return password;
	}
	public String getRepetPassword() {
		return repetPassword;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRepetPassword(String repetPassword) {
		this.repetPassword = repetPassword;
	}

}
