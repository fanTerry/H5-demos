package com.esportzoo.esport.connect.request.game;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 欢乐摇一摇参与游戏请求
 * 
 * @author jing.wu
 * @version 创建时间：2020年4月4日 上午10:29:48
 */
@Data
public class ShakePlayRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -942430405531364147L;

	/** 房间编号 */
	private String roomNo;

	/** 游戏档位id */
	private Long gamePlayItemId;

	/** 是否托管true:托管 */
	private Boolean autoPlay = false;

}
