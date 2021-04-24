package com.esportzoo.esport.controller.ws.server.scene;

import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.esport.controller.ws.constants.ExecResult;

import io.netty.channel.socket.SocketChannel;

/**
 * @param key 是指与通道Id（ChannelId）进行关联的key值,默认为userId，后面可自行根据需求进行组合
 * @author huanwei.li
 *
 */
public interface SceneHandler {
	
	/**
	 * 用户进入某场景
	 * @param scenekey
	 */
	public void inMap(String scenekey, String usrId);
	
	public void inMap(String scenekey, String usrId, JSONObject msgObj);
	
	/**
	 * 用户离开某场景
	 * @param scenekey
	 */
	public void outMap(String scenekey, String usrId);
	
	public void outMap(String scenekey, String usrId, JSONObject msgObj);
	
	/**
	 * 获取指定场景下的一组用户
	 * @param scenekey
	 */
	public Set<String> getMap(String scenekey);
	
	/**
	 * 根据usrId找到对应通道
	 * @param usrId
	 */
	public SocketChannel getSocketChannel(String usrId);
	
	public ExecResult executeData(String msgJson);
	
	
}
