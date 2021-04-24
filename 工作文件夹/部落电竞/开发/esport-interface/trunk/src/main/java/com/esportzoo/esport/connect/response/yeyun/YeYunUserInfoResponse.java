package com.esportzoo.esport.connect.response.yeyun;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author tingjun.wang
 * @date 2019/9/24 19:39
 */
@Data
public class YeYunUserInfoResponse implements Serializable {
	private static final long serialVersionUID = 1472433425981710501L;
	/**用户ID **/
	private Long myUserId;
	/** 椰云用户id */
	private String userId;
	/** 椰云用户姓名 */
	private String userName;
	/** 账户状态 0正常 1锁定 */
	private Integer status;
	/** 账户积分 */
	private BigDecimal score;
	/** 是否实名认证 0未实名 1已实名 */
	private Integer isRealCert;
	/** 每天可兑换积分 */
	private BigDecimal exchangeScore;
	/**兑换比例*/
	private String exchangeRatio;

}
