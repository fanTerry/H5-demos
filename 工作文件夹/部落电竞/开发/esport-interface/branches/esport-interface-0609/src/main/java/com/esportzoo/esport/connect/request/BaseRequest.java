package com.esportzoo.esport.connect.request;

import com.esportzoo.esport.constants.ClientType;

public class BaseRequest {

	/** 渠道号 */
	private Long agentId = 1000L;
	/** 客户端类型 */
	private Integer clientType = 0;
	/** 客户端版本 */
	private String version;
	/** 用户会话 ID */
	private String sid = "";
	/** 平台参数 */
	private Integer biz = 1;
	/** 分享码 */
	private String shareCode;
	/** 邀请码 */
	private String inviteCode;

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public ClientType getDefaultClientType() {
		return ClientType.WXXCY;
	}

	public Integer getBiz() {
		return biz;
	}

	public void setBiz(Integer biz) {
		this.biz = biz;
	}

	public String getShareCode() {
		return shareCode;
	}

	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

}
