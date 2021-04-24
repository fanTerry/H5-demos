package com.esportzoo.esport.connect.response.charge;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/05/18
 */
public class ChargeResponse implements Serializable{

	private static final long serialVersionUID = -2722686348306815099L;

	private Integer chargeWay;
	private String timeStamp;
	private String nonceStr;
	private String prepayId;
	private String paySign;
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public Integer getChargeWay() {
		return chargeWay;
	}

	public void setChargeWay(Integer chargeWay) {
		this.chargeWay = chargeWay;
	}
}
