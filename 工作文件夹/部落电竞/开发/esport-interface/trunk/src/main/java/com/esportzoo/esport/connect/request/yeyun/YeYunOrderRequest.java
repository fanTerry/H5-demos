package com.esportzoo.esport.connect.request.yeyun;

import com.esportzoo.esport.connect.request.BaseRequest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tingjun.wang
 * @date 2019/9/25 10:27
 */
public class YeYunOrderRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = -8519428458934684470L;

	/**
	 * 本平台 userId
	 */
	private Long userId;
	/**
	 * 查询开始时间
	 */
	private Date startDate;
	/**
	 * 查询结束时间
	 */
	private Date endDate;
	/**
	 * 本平台订单流水号
	 */
	private String tradeNo;
	/**
	 * 操作积分数量
	 */
	private BigDecimal pointNum;
	/**
	 * 操作钱包数量
	 */
	private BigDecimal walletScore;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public BigDecimal getPointNum() {
		return pointNum;
	}

	public void setPointNum(BigDecimal pointNum) {
		this.pointNum = pointNum;
	}

	public BigDecimal getWalletScore() {
		return walletScore;
	}

	public void setWalletScore(BigDecimal walletScore) {
		this.walletScore = walletScore;
	}
}
