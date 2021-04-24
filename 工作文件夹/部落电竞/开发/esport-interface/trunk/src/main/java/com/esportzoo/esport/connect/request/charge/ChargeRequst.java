package com.esportzoo.esport.connect.request.charge;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/05/18
 */
@Data
public class ChargeRequst extends BaseRequest implements Serializable {
	
	private static final long serialVersionUID = -6138304073665998121L;
	
	private String chargeAmount;

	private Integer chargeWay;




	
	
}
