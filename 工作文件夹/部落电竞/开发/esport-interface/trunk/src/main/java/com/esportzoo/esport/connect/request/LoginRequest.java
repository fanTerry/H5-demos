package com.esportzoo.esport.connect.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 小程序登录请求参数
 * @author: wujing
 * @date:2019年4月22日上午11:25:00
 */
@ApiModel(value = "LoginRequest", description = "小程序登录请求参数")
public class LoginRequest {

	@ApiModelProperty(required = false, value = "临时登录凭证")
	@NotBlank(message = "临时登录凭证不能为空")
	private String code;

	@ApiModelProperty(required = false, value = "用户非敏感信息")
	@NotBlank(message = "用户非敏感信息不能为空")
	private String rawData;

	@ApiModelProperty(required = false, value = "签名")
	@NotBlank(message = "签名不能为空")
	private String signature;

	@ApiModelProperty(required = false, value = "用户敏感信息")
	@NotBlank(message = "用户敏感信息不能为空")
	private String encrypteData;

	@ApiModelProperty(required = false, value = "解密算法的向量")
	@NotBlank(message = "解密算法的向量不能为空")
	private String iv;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getEncrypteData() {
		return encrypteData;
	}

	public void setEncrypteData(String encrypteData) {
		this.encrypteData = encrypteData;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}
}
