package com.esportzoo.esport.controller.ws.constants;

import java.util.List;

import com.esportzoo.esport.constants.BaseType;

public class NettyExecType extends BaseType {

	private static final long serialVersionUID = 6708546291377913881L;

	protected NettyExecType(Integer index, String description) {
		super(index, description);
	}
	
	/**  用户进入房间，将用户与房间进行关联 */
	public static NettyExecType USR_REGIST_ROOM= new NettyExecType(10, "用户与房间进行关联");
	
	/**  用户与通道进行关联 */
	public static NettyExecType INIT_USR_CHANNEL= new NettyExecType(12, "用户与通道进行关联");
	
	/**  用户聊天 */
	public static NettyExecType USR_CHAT= new NettyExecType(13, "用户聊天");
	
	/**  保持连接心跳 */
	public static NettyExecType HEART_BEAT = new NettyExecType(15, "连接心跳");
	
	/**  用户赛事数据 */
	public static NettyExecType USR_MATCHING= new NettyExecType(16, "用户赛事数据");
	
	/**  用户赛事事件数据 */
	public static NettyExecType USR_EVENTING= new NettyExecType(17, "用户赛事事件数据");
	
	public static List<NettyExecType> getAllList() {
		return getAll(NettyExecType.class);
	}

	public static NettyExecType valueOf(Integer index) {
		return valueOf(NettyExecType.class, index);
	}
}
