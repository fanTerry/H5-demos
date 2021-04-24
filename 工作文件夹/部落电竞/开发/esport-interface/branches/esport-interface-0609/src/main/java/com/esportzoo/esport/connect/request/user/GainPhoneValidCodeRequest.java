package com.esportzoo.esport.connect.request.user;

import java.io.Serializable;

import com.esportzoo.esport.connect.request.BaseRequest;

/**
 * @author tingting.shen
 * @date 2019/07/15
 */
public class GainPhoneValidCodeRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -6644349215387797392L;
	
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
