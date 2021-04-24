package com.esportzoo.esport.connect.response.charge;

import com.esportzoo.esport.constants.EsportPayway;

import java.io.Serializable;
import java.util.Map;

/**
 * H5充值响应
 * 
 * @author wujing
 */
public class H5ChargeV2Response implements Serializable {

	private static final long serialVersionUID = 5141053180517758520L;
	/** 成功标识 */
	private boolean successFlag;
	/** 充值需要请求的地址,如H5支付 */
	private String requestUrl;
	/** 充值选择的支付方式 {@link EsportPayway} */
	private int chargeWay;
	/** 充值返回参数 */
	private Map<String, String> requestParams;
	/** 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时 */
	private String prepayId;
	/** 相对于UserThirdOrder的其他业务订单id */
	private Long outOrderId;
	/**
	 * 支付订单主键 user_third_order的payNo
	 */
	private String payNo;

	public H5ChargeV2Response() {

	}

	public boolean isSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(boolean successFlag) {
		this.successFlag = successFlag;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public int getChargeWay() {
		return chargeWay;
	}

	public void setChargeWay(int chargeWay) {
		this.chargeWay = chargeWay;
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public Long getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(Long outOrderId) {
		this.outOrderId = outOrderId;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
}
