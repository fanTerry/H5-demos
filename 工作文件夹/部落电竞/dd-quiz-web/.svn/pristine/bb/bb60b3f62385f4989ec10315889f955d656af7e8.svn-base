package com.esportzoo.esport.connect.response.payment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付方式列表信息对象
 */
public class UserPayWayResponse implements Serializable {

	private static final long serialVersionUID = -1504269299881191766L;

	/** 支付编号 默认：0 @see EsportPayway#index */
	private Integer payIndex;

	/** 支付方式名称 @see EsportPayway#description */
	private String payName;

	/** 支付图标 */
	private String payIcon;

	/** 前端余额显示状态：0:不显示，1显示 默认：0 @see ShowBalanceFlag */
	private Integer showBalance;

	/** 余额 */
	private BigDecimal balance;

	/** 货币单位 */
	private String currencyUnit;

	/** 是否为第三方支付，0：否 1:是 默认：1 @see ThirdPaymentFlag */
	private Integer thirdPayment;

	public Integer getPayIndex() {
		return payIndex;
	}

	public void setPayIndex(Integer payIndex) {
		this.payIndex = payIndex;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayIcon() {
		return payIcon;
	}

	public void setPayIcon(String payIcon) {
		this.payIcon = payIcon;
	}

	public Integer getShowBalance() {
		return showBalance;
	}

	public void setShowBalance(Integer showBalance) {
		this.showBalance = showBalance;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	public Integer getThirdPayment() {
		return thirdPayment;
	}

	public void setThirdPayment(Integer thirdPayment) {
		this.thirdPayment = thirdPayment;
	}

	@Override
	public String toString() {
		return "UserChargeWayDTO [payIndex=" + payIndex + ", payName="
				+ payName + ", payIcon=" + payIcon + ", showBalance="
				+ showBalance + ", balance=" + balance + ", currencyUnit="
				+ currencyUnit + ", thirdPayment=" + thirdPayment + "]";
	}

}