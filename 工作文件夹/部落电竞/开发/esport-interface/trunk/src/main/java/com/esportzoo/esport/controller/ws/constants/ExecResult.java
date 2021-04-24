package com.esportzoo.esport.controller.ws.constants;

import java.io.Serializable;

/**
 * 消息执行结果
 * @author huanwei.li
 */
public class ExecResult implements Serializable{

	private static final long serialVersionUID = -7058282317123835143L;

	/** 状态code (成功的状态码：0000；失败的状态码：9999)*/
	private String code;
	
	/** 状态描述   (成功的描述信息：推送成功；失败的描述信息：推送失败)*/
	private String msg;
	
	public ExecResult(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public ExecResult() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ExecResult [code=" + code + ", msg=" + msg + "]";
	}

}