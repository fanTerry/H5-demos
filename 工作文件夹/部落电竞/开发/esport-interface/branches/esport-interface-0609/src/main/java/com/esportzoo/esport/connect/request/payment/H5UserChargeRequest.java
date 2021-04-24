package com.esportzoo.esport.connect.request.payment;

import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.EsportPayway;

import java.io.Serializable;

/**
 * 用户充值请求参数
 * @author wujing
 */
public class H5UserChargeRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -2048682481636748314L;

	private String chargeAmount;

	/** {@link EsportPayway} */
	private Integer chargeWay;

	//
	private Integer chargeType ;
	/** 回调地址 */
	private String redirectUrl;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public Integer getChargeWay() {
		return chargeWay;
	}

	public void setChargeWay(Integer chargeWay) {
		this.chargeWay = chargeWay;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}
}
