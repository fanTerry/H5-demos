package com.esportzoo.esport.connect.request.payment;

import java.io.Serializable;

import com.esportzoo.esport.connect.request.BaseRequest;

public class UserPayStatusRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = 3972119267223659384L;
	//第三方支付订单ID
	private String payNo;
	
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	
}
