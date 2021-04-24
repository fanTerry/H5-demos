package com.esportzoo.esport.connect.response.hd;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户续命方式
 * @author jing.wu
 * @version 创建时间：2019年9月19日 上午10:48:25
 */
@Data
public class UserContinueWayResponse implements Serializable {

	private static final long serialVersionUID = -5111810615992581480L;

	/** 续命方式名称 */
	public String wayName;
	/** 当前方式是否可以续命 */
	public boolean canActive;
	/** 当前方式位置 */
	public Integer orderSort;
	/** 当前方式类型 ,用于前端区分 */
	public Integer wayType;

	public UserContinueWayResponse() {

	}

	public UserContinueWayResponse(String wayName, boolean canActive, Integer orderSort, Integer wayType) {
		this.wayName = wayName;
		this.canActive = canActive;
		this.orderSort = orderSort;
		this.wayType = wayType;
	}
}
