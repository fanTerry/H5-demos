package com.esportzoo.esport.connect.request;

/**
 * @description: 首页参数
 *
 * @author: Haitao.Li
 *
 * @create: 2019-07-22 10:01
 **/
public class IndexRequest {

	private  Integer clientType;
	private Long agentId;

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
}
