package com.esportzoo.esport.controller.ws.constants;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class CommonMsg  implements Serializable{
	private static final long serialVersionUID = 7013172240184808455L;
	
	/** 场景类型  @SceneType*/
	private Integer sceneType;
	
	/** 执行指令类型  @CmdType*/
	private Integer cmdType;
	
	/** 消息执行类型  @NettyExecType*/
	private Integer execType;

	/** 游戏数据类型 */
	private String matchType;
	
	/** 业务消息体 （json格式）*/
	private String playload;

	/** 签名(走dubbo调用可不填) */
    private String sign;
    
	public Integer getSceneType() {
		return sceneType;
	}

	public void setSceneType(Integer sceneType) {
		this.sceneType = sceneType;
	}

	public Integer getCmdType() {
		return cmdType;
	}

	public void setCmdType(Integer cmdType) {
		this.cmdType = cmdType;
	}

	public Integer getExecType() {
		return execType;
	}

	public void setExecType(Integer execType) {
		this.execType = execType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	

	public String getPlayload() {
		return playload;
	}

	public void setPlayload(String playload) {
		this.playload = playload;
	} 

	public void setupPlayload(String columnName, String value) {
		JSONObject jsonO = JSONObject.parseObject(playload);
		jsonO.put(columnName, value);
		playload = jsonO.toJSONString();
	}

	public void setupPlayload(String columnName, Object value) {
		JSONObject jsonO = JSONObject.parseObject(playload);
		jsonO.put(columnName, value);
		playload = jsonO.toJSONString();
	}

	public void removePlayload(String columnName) {
		JSONObject jsonO = JSONObject.parseObject(playload);
		jsonO.remove(columnName);
		playload = jsonO.toJSONString();
	}

	public String getPlayload(String columnName) {
		JSONObject jsonO = JSONObject.parseObject(playload);
		return jsonO.getString(columnName);
	}

	@SuppressWarnings("unchecked")
	public <T> T getPlayload(String columnName, Class<T> clz) {
		JSONObject jsonO = JSONObject.parseObject(playload);
		return (T) jsonO.get(columnName);
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	@Override
	public String toString() {
		return "CommonMsg{" +
				"sceneType=" + sceneType +
				", cmdType=" + cmdType +
				", execType=" + execType +
				", matchType='" + matchType + '\'' +
				", playload='" + playload + '\'' +
				", sign='" + sign + '\'' +
				'}';
	}
}
