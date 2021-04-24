package com.esportzoo.esport.controller.ws.constants;

import java.util.List;

import com.esportzoo.esport.constants.BaseType;

public class SceneType extends BaseType {
 
	private static final long serialVersionUID = -8153531076690901527L;


	protected SceneType(Integer index, String description) {
		super(index, description);
	}

	/**  首页 TODO 后续扩展使用*/
	public static SceneType HOME = new SceneType(1, "app首页");
	
	/** 赛事相关直播*/
	public static SceneType MATCHING = new SceneType(2, "赛事相关直播");
	
	/**  房间聊天 */
	public static SceneType ROOM = new SceneType(3, "房间");
	
	public static List<SceneType> getAllList() {
		return getAll(SceneType.class);
	}

	public static SceneType valueOf(Integer index) {
		return valueOf(SceneType.class, index);
	}
}
