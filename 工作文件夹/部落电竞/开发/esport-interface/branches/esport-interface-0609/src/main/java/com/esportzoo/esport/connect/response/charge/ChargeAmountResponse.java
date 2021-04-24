package com.esportzoo.esport.connect.response.charge;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 充值金额和赠送金额
 *
 * @author: Haitao.Li
 *
 * @create: 2020-03-12 10:48
 **/
@Data
public class ChargeAmountResponse implements Serializable {

	/** 充值金额 */
	private String chargeMoney;
	/** 赠送星星 */
	private Integer sendStar;


}
