package com.esportzoo.esport.connect.response.expert;

import java.io.Serializable;

/**
 * @description: 支付结果返回
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-16 17:55
 **/
public class RecOrderResponse implements Serializable {


	private static final long serialVersionUID = -6856167067317991160L;

	private Integer payResult ;
	
	public RecOrderResponse() {}
	public RecOrderResponse(Integer payResult) {
		this.payResult = payResult;
	}

	public Integer getPayResult() {
		return payResult;
	}

	public void setPayResult(Integer payResult) {
		this.payResult = payResult;
	}
}
