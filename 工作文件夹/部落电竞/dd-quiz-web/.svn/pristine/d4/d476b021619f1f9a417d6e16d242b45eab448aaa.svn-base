package com.esportzoo.esport.connect.request.hd;

import java.io.Serializable;

import lombok.Data;

import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.EsportPayway;

/**
 * 答题支付订单参数
 * @author jing.wu
 * @version 创建时间：2019年9月20日 上午9:49:49
 */
@Data
public class SubjectPayRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -618421125167949197L;
	/** 答题流水表id */
	private Long hdSubjectLogId;
	/** 用户选择的支付方式 {@link EsportPayway} */
	private Integer choosedPayWay;
	/** 用户是从哪个分享码进入的 */
	private String shareCode;

}
