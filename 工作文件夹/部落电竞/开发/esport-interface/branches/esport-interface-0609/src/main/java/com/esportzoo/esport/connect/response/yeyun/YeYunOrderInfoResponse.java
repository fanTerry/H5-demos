package com.esportzoo.esport.connect.response.yeyun;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tingjun.wang
 * @date 2019/9/25 9:49
 */
public class YeYunOrderInfoResponse implements Serializable {
	private static final long serialVersionUID = 8889741608826859213L;

	private String id;
	/**
	 * 订单类型：充值记录(商户转入)
	 */
	private String type;
	/**
	 * 描述：[26920987016]充值记录(商户转入)(来源(ac97b47b-19a8-4d7e-a4c8-2a55d479f75d))
	 */
	private String desc;
	/**
	 *  剩余积分值
	 */
	private BigDecimal total;
	/**
	 *  本次交易值
	 */
	private BigDecimal value;
	/**
	 * 订单关联的椰云用户id
	 */
	private String userId;
	/**
	 * 操作的时间
	 */
	private Date date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
