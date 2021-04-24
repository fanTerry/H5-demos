package com.esportzoo.esport.connect.response.payment;

import java.io.Serializable;

public class ChargeMoneyConfigVo implements Serializable {

	private static final long serialVersionUID = -6771253382238002253L;
	private String money;
	private Integer starNum;
	private Integer sendStar;
	
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public Integer getStarNum() {
		return starNum;
	}
	public void setStarNum(Integer starNum) {
		this.starNum = starNum;
	}
	public Integer getSendStar() {
		return sendStar;
	}
	public void setSendStar(Integer sendStar) {
		this.sendStar = sendStar;
	}
	
}
