package com.esportzoo.esport.connect.response.payment;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

import com.esportzoo.esport.constants.EsportPayway;

/**
 * 支付订单响应
 * 
 * @author wujing
 */
@Data
public class PayOrderResponse implements Serializable {

	private static final long serialVersionUID = -8095526932262877025L;
	/** 兑换订单选择的支付方式 {@link EsportPayway} */
	private int chargeWay;
	/** 请求参数 */
	private Map<String, String> requestParams;
	/** 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时 */
	private String prepayId;
	/** 相对于UserThirdOrder的其他业务订单id，如兑换订单（shopOrder）、文章推荐订单（recOrder） */
	private Long outOrderId;
	/** 成功标识 */
	private boolean successFlag;
	/** 兑换订单需要请求的地址,如H5支付 */
	private String requestUrl;
	/** 支付订单主键 user_third_order的id */
	private Long thirdId;

}
