package com.esportzoo.esport.connect.response.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayOrderStatusResponse implements Serializable {

	private static final long serialVersionUID = -990627429929422149L;

	private Long chargeId;

	/** 充值商端对应的流水号(可据此流水号去对方查询相关信息) */
	private String chargePayNo;

	/** 请求的充值金额 */
	private Integer amount;

	/** 充值商返回的充值金额 */
	private Integer successAmount;

	/** 状态 @ThirdPayStatus*/
	public Integer status;

	/** 响应时间 */
	private Date responseTime;

	/** 响应参数 */
	private String responseInfo;
	
	  /** 充值流水编号 */
    private String chargeNo;
    
    /**用户钱包余额*/
	private BigDecimal walletRec;
	public Long getChargeId() {
		return chargeId;
	}

	public void setChargeId(Long chargeId) {
		this.chargeId = chargeId;
	}

	public String getChargePayNo() {
		return chargePayNo;
	}

	public void setChargePayNo(String chargePayNo) {
		this.chargePayNo = chargePayNo;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getSuccessAmount() {
		return successAmount;
	}

	public void setSuccessAmount(Integer successAmount) {
		this.successAmount = successAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo;
	}

	public String getChargeNo() {
		return chargeNo;
	}

	public void setChargeNo(String chargeNo) {
		this.chargeNo = chargeNo;
	}

	public BigDecimal getWalletRec() {
		return walletRec;
	}

	public void setWalletRec(BigDecimal walletRec) {
		this.walletRec = walletRec;
	}
    
}
