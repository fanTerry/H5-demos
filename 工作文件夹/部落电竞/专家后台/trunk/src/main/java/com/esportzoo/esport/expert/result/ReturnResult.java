package com.esportzoo.esport.expert.result;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.esport.expert.constant.ResponseConstant;

import java.io.Serializable;

/**
 * ajax请求返回值的封装类 
 */
public class ReturnResult<T> implements Serializable{
	
	private static final long serialVersionUID = -5248071824225569382L;

	public static final ReturnResult ERROR_NOT_LOGIN = new ReturnResult(false, "NOT_LOGIN", "用户未登录");
	
	/** 是否成功 */
	private Boolean isSuccess;
	/** 返回状态码  */
	private String code;
	/** 返回提示信息 */
	private String msg;
	/** 返回值 */
	private Object model;
	
	public static <T> ReturnResult getInstance(ModelResult<T> modelResult){
		if(modelResult.isSuccess()){
			return new ReturnResult(true, modelResult.getModel());
		}
		return new ReturnResult(false, modelResult.getErrorCode(), modelResult.getErrorMsg(), modelResult.getModel());
	}
	
	public static <T> ReturnResult getInstance(DataPage<T> dataPage){
		return new ReturnResult(true, dataPage);
	}

	public ReturnResult(Boolean isSuccess, String msg) {
		super();
		this.isSuccess = isSuccess;
		this.msg = msg;
	}
	
	public ReturnResult(Boolean isSuccess, Object model) {
		super();
		this.isSuccess = isSuccess;
		this.model = model;
	}
	
	public ReturnResult(Boolean isSuccess, String code, String msg) {
		super();
		this.isSuccess = isSuccess;
		this.code = code;
		this.msg = msg;
	}

	public ReturnResult(Boolean isSuccess, String code, String msg, Object model) {
		super();
		this.isSuccess = isSuccess;
		this.code = code;
		this.msg = msg;
		this.model = model;
	}
	
	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
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

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}


	/*========================================================
					获取返回的公共参数处理
	========================================================*/
	public static <T> ReturnResult<T> withSuccessResp(T data) {
		ReturnResult<T> cr = new ReturnResult<>(true, ResponseConstant.RESP_SUCC_CODE, ResponseConstant.RESP_SUCC_MESG);
		cr.setModel(data);
		return cr;
	}

	public static <T> ReturnResult<T> withSuccessResp(String msg) {
		ReturnResult<T> cr = new ReturnResult<>(true, ResponseConstant.RESP_SUCC_CODE, msg);
		return cr;
	}

	public static <T> ReturnResult<T> withErrorResp(String msg) {
		ReturnResult<T> cr = new ReturnResult<>(false, ResponseConstant.RESP_ERROR_CODE, msg);
		return cr;
	}

	public static <T> ReturnResult<T> withSuccessResp(String code, String msg) {
		ReturnResult<T> cr = new ReturnResult<>(true, code, msg);
		return cr;
	}

	public static <T> ReturnResult<T> withErroResp(String code, String msg) {
		ReturnResult<T> cr = new ReturnResult<>(false, code, msg);
		return cr;
	}

	public ReturnResult<T> withData(T data) {
		this.setModel(data);
		return this;
	}
}
