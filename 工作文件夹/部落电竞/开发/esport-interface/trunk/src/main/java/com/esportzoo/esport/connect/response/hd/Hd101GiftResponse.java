package com.esportzoo.esport.connect.response.hd;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description: 礼品展示
 *
 * @author: Haitao.Li
 *
 * @create: 2019-09-18 14:14
 **/

@Data
public class Hd101GiftResponse implements Serializable {

	private static final long serialVersionUID = -5426324256166482910L;

	/**
	 * 礼品ID
	 */
	private Integer hdGiftId;

	/**
	 * 礼品名称
	 */
	private String hdGiftName;

	/**
	 * 礼品剩余数量
	 */
	private Integer giftRemainder;

	/**
	 * 中奖列表名单播报
	 */
	private List<Map> giftWinnerList ;

	private String playTime;
	/**
	 * 中奖需要答对的题目
	 */
	private String needAsweredNum;

	/** 礼品描述*/
	private String giftDesc;
}
