package com.esportzoo.esport.connect.response.hd;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class HdSignResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 礼品ID
	 */
	private Integer hdGiftId;
	/**
	 * 礼品名称
	 */
	private String hdGiftName;
	/**
	 * 第几天
	 */
	private Integer signFlagBit;
	/**
	 * 礼品数量
	 */
	private BigDecimal hdGiftCount;
	/**
	 * 是否已领取 0已领取 1未领取 2不能领取
	 */
	private Integer receiveStatus;
	/**
	 *第七天随机金豆标识
	 */
	private String sevenFlag;



}
