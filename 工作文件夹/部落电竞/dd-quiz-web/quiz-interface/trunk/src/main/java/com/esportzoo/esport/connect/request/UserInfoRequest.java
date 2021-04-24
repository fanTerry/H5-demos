package com.esportzoo.esport.connect.request;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2019-07-22 19:11
 **/
public class UserInfoRequest extends BaseRequest {
	/** 用户昵称 **/
	private String nickName;
	/** 身份证号 **/
	private String certNo;
	/** 用户状态 @UserConsumerStatus,0 正常,1 关闭 **/
	private Integer status;
	/** 电子邮件 **/
	private String email;
	/** 联系电话 **/
	private String phone;
	private String icon;
	/** 简介 **/
	private String intro;
	/** 真实姓名 **/
	private String trueName;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
}
