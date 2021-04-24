package com.esportzoo.esport.connect.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import com.esportzoo.esport.constant.ResponseConstant;

/**
 * 接口数据响应公共类
 * @author huanweili
 *
 * @param <T>
 */
@ApiModel(value="CommonResponse", description="通用返回参数模板")
public class CommonResponse<T> implements Serializable{

	private static final long serialVersionUID = 3380370321354738645L;

	@ApiModelProperty(required=true, value="状态码")
	private String code;
	
	@ApiModelProperty(required=true, value="状态描述")
	private String message;
	
	@ApiModelProperty(required=true, value="业务数据")
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static <T> CommonResponse<T> newCommonResponse() {
		return new CommonResponse<T>();
	}
	
	public static <T> CommonResponse<T> newCommonResponse(String code, String message) {
		CommonResponse<T> cr = new CommonResponse<>();
		cr.setCode(code);
		cr.setMessage(message);
		return cr;
	}

	/*========================================================
					获取返回的公共参数处理
	========================================================*/
	public static <T> CommonResponse<T> withSuccessResp(T data){
		CommonResponse<T> cr = new CommonResponse<>();
		cr.setCode(ResponseConstant.RESP_SUCC_CODE);
		cr.setMessage(ResponseConstant.RESP_SUCC_MESG);
		cr.setData(data);
		return cr;
	}
	
	public static <T> CommonResponse<T> withSuccessResp(String msg){
		CommonResponse<T> cr = new CommonResponse<>();
		cr.setCode(ResponseConstant.RESP_SUCC_CODE);
		cr.setMessage(msg);
		return cr;
	}
	
	public static <T> CommonResponse<T> withErrorResp(String msg){
		CommonResponse<T> cr = new CommonResponse<>();
		cr.setCode(ResponseConstant.RESP_ERROR_CODE);
		cr.setMessage(msg);
		return cr;
	}

	public static CommonResponse withErrorResp(){
		CommonResponse cr = new CommonResponse<>();
		cr.setCode(ResponseConstant.RESP_ERROR_CODE);
		cr.setMessage("服务器繁忙！");
		return cr;
	}

	public static <T> CommonResponse<T> withResp(String code, String msg){
		CommonResponse<T> cr = new CommonResponse<>();
		cr.setCode(code);
		cr.setMessage(msg);
		return cr;
	}
	
	public CommonResponse<T> withData(T data){
		this.setData(data);
		return this;
	}
	
}