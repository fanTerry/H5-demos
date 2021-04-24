package com.esportzoo.esport.connect.response.charge;

import java.io.Serializable;
import java.util.Map;

import com.esportzoo.esport.constants.EsportPayway;

/**
 * H5充值响应
 * 
 * @author wujing
 */
public class H5ChargeResponse implements Serializable {

	private static final long serialVersionUID = 2239470079588072618L;
	/** 成功标识 */
	private boolean successFlag;
	/** 充值需要请求的地址,如H5支付 */
	private String requestUrl;
	/** 充值选择的支付方式 {@link EsportPayway} */
	private int chargeWay;
	/** 充值返回参数 */
	private Map<String, String> respParams;

	public H5ChargeResponse() {

	}

	public H5ChargeResponse(boolean successFlag, String requestUrl) {
		this.successFlag = successFlag;
		this.requestUrl = requestUrl;
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

	public Map<String, String> getRespParams() {
		return respParams;
	}

	public void setRespParams(Map<String, String> respParams) {
		this.respParams = respParams;
	}

	public int getChargeWay() {
		return chargeWay;
	}

	public void setChargeWay(int chargeWay) {
		this.chargeWay = chargeWay;
	}
}
