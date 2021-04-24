package com.esportzoo.esport.connect.request.payment;

import java.io.Serializable;

import com.esportzoo.esport.connect.request.BaseRequest;

/**
 * 充值方式列表查询参数
 * @author wujing
 * @date 2019年8月15日 下午3:22:21
 */
public class UserPayWayRequest extends BaseRequest implements Serializable {
	
	private static final long serialVersionUID = -5516710593487152924L;
	
	/** 用户Id */
	private Long userId;
	/** 是否需要本地星星支付，true:需要 ，false:不需要。设置为true时，后台admin设置星星支付可用状态才起作用,默认需要 */
	private Boolean needRecPay;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Boolean getNeedRecPay() {
		return needRecPay;
	}
	public void setNeedRecPay(Boolean needRecPay) {
		this.needRecPay = needRecPay;
	}
}
