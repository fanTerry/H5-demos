package com.esportzoo.esport.vo.user;

import com.esportzoo.esport.connect.request.BaseRequest;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/07/16
 */
public class PhoneRegisterRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -219351792243803530L;
	
	private String phone;
	private String code;
	private String account;
	private String password;
	private String repetPassword;
	private String redirect;


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepetPassword() {
		return repetPassword;
	}

	public void setRepetPassword(String repetPassword) {
		this.repetPassword = repetPassword;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
