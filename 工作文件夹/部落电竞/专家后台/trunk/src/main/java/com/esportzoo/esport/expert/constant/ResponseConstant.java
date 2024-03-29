package com.esportzoo.esport.expert.constant;

/**
 * 返回处理结果状态码和操作错误信息
 * @author huanweili
 *
 */
public class ResponseConstant {
	
	public static final String MOBILE_TYPE = "接口操作";
	/** 返回成功状态码 */
    public static final String RESP_SUCC_CODE = "200", RESP_SUCC_MESG = "处理成功";
    /** 未知和处理失败返回码 */
    public static final String RESP_ERROR_CODE = "9999", RESP_ERROR_MESG = "处理失败";
    /** 返回成功状态码，老接口的失败状态码 */
    public static final String OLD_RESP_ERROR_CODE = "-200";
    /** 参数错误 */
    public static final String RESP_PARAM_ERROR_CODE = "4444", RESP_PARAM_ERROR_MESG = "参数错误";
    /** 系统异常,请稍候重试 */
    public static final String SYSTEM_ERROR_CODE = "5555", SYSTEM_ERROR_MESG = "系统异常,请稍候重试";
    /** 用户未登录 */
    public static final Integer USR_NOT_LOGIN_CODE = 401;
    /** 用户未登录 */
    public static final String  USR_NOT_LOGIN_MESG = "用户未登录";
    /** 签名错误 */
    public static final String NOT_SIGN_MESG = "签名错误";
    /** 请求参数为空 */
	public static final String PARAM_NOT_NULL = "请求参数为空";
	/** 参数错误 */
	public static final String PARAM_IS_ERROR = "请求参数错误"; 
	/** oatuch 获取accessToken请求参数异常*/
	public static final String OAUTH_PARAM_IS_ERROR_CODE = "10001", OAUTH_PARAM_IS_ERROR_MESG = "获取accessToken请求参数不能为空";
}