package com.esportzoo.esport.connect.response.charge;

import com.esportzoo.esport.vo.charge.IapChargeVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tingting.shen
 * @date 2019/05/21
 */
@Data
public class ChargePageResponse implements Serializable {

	private static final long serialVersionUID = -3576840973250550766L;

	/** 可用的推荐币 */
	private String ableRecScore;
	/** 可用的充值金额列表 */
	private String[] chargeAmountList;
	/** 苹果支付充值列表 */
	private List<IapChargeVo> iapChargeList;

	/** 返回的充值列表 */
	private List<ChargeAmountResponse> chargeAmountResponseList;


}
