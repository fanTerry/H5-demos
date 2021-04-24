package com.esportzoo.esport.controller.wxaccount;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.SignKey;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.constants.wx.AppIdAndSecret;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.controller.ws.client.LoginWebSocketServerHandler;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.WxAccountUserInfo;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.manager.LoginManager;
import com.esportzoo.esport.manager.UserWalletManager;
import com.esportzoo.esport.manager.wxaccount.WxAccountManager;
import com.esportzoo.esport.service.consumer.UserConsumerService;
import com.esportzoo.esport.util.AccessTokenUtil;
import com.esportzoo.esport.util.SHA1Util;
import com.esportzoo.esport.util.WeChatUtil;
import com.esportzoo.esport.util.WxpayUtil;
import com.esportzoo.esport.vo.user.WeixinUserVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName WxLoginController
 * @Description
 * @Author jiajing.he
 * @Date 2019/8/13 14:31
 * @Version 1.0
 **/
@RequestMapping("wxlogin")
@Controller
public class WxLoginController extends BaseController {

	@Autowired
	private WxAccountManager wxAccountManager;
	@Autowired
	private UserConsumerService userConsumerService;
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private AccessTokenUtil accessTokenUtil;
	@Autowired
	private LoginWebSocketServerHandler loginWebSocketServerHandler;
	@Autowired
    UserWalletManager userWalletManager;
	private final static String CODE_KEY = "wxaccount_code_key_";
	private Logger logger = LoggerFactory.getLogger(WxLoginController.class);
	public static final String logPrefix = "竞猜PC端微信用户退出_";
	public static final String logCheckLogin = "竞猜PC端微信用户检查登录_";

	@Autowired
	@Qualifier("cachedManager")
	private CachedManager cachedManager;
	@Value("${environ}")
	private String environ;

	/**
	 * 微信公众号验证url的GET请求
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "check",method = RequestMethod.GET)
	public void binCheck(HttpServletRequest request, HttpServletResponse response){
		PrintWriter writer = null;
		try {
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String echostr = request.getParameter("echostr");

			String[] array = new String[] { "9zAtxXIWmTqktAUY6nWIOqTlFcfBaehT", timestamp, nonce };
			StringBuffer sb = new StringBuffer();
			//1.将token、timestamp、nonce三个参数进行字典序排序
			Arrays.sort(array);
			//2.将三个参数字符串拼接成一个字符串进行sha1加密
			for (int i = 0; i < 3; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			String signa = SHA1Util.sha1Encrypted(str);
			if (signa != null && signa.equals(signature)){
				//响应微信
				writer = response.getWriter();
				writer.print(echostr);
				writer.flush();
			}
		}catch (Exception e){
			logger.info("微信公众号验证url异常",e);
		}finally {
			if (writer!=null){
				writer.close();
			}
		}
	}

	@RequestMapping(value = "check",method = RequestMethod.POST)
	public void check(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取微信回调事件信息
		String returnStr = null;
		PrintWriter writer = null;
		try {
			Map<String, String> callbackMap = wxAccountManager.xmlToMap(request);
			logger.info("接收的微信消息callbackMap：{}",JSON.toJSONString(callbackMap));
			if (callbackMap == null){
				logger.info("接收的微信消息callbackMap为空");
				return;
			}

			if ("text".equals(callbackMap.get("MsgType"))){
				//普通消息，暂未处理，直接返回空字符串
				returnStr = "";
			}

			if (callbackMap.get("FromUserName").toString() != null && ("event").equals(callbackMap.get("MsgType"))) {
				// 通过openid获取用户信息
				WxAccountUserInfo wechatUserInfoMap = wxAccountManager.getScanUserInfoByOpenId(
						accessTokenUtil.getAccessToken(AppIdAndSecret.wxService.getIndex()),
						callbackMap.get("FromUserName"));
				logger.info("获取wechatUserInfoMap返回结果 【wechatUserInfoMap:{}】", wechatUserInfoMap);
				ModelResult<UserConsumer> userModelResult = userConsumerService
						.findByUnionId(wechatUserInfoMap.getUnionid());

				String sid = UUID.randomUUID().toString();
				String eventKey = "";
				UserConsumer userConsumer = null;

				if (("subscribe").equals(callbackMap.get("Event"))) {//未订阅事件
					eventKey = callbackMap.get("EventKey");
					eventKey = eventKey.substring(eventKey.lastIndexOf("_")+1);
					if (!userModelResult.isSuccess() || null == userModelResult.getModel()) {
						logger.info("该用户第一次进行扫码关注【openId:{}】", callbackMap.get("FromUserName"));
						userConsumer = wxAccountManager.synchronizeUserInfo(wechatUserInfoMap,
								new BaseRequest(), request);
					} else {//曾经关注过橘子电竞微信公众号后取消再次进行关注
						logger.info("该用户曾经关注过公众号【openId:{}】", callbackMap.get("FromUserName"));
						userConsumer = userModelResult.getModel();
					}
				}

				if (("SCAN").equals(callbackMap.get("Event"))) {//已订阅
					if (userModelResult.isSuccess() || null != userModelResult.getModel()) {
						eventKey = callbackMap.get("EventKey");
						logger.info("该用户已扫码关注过公众号了【openId:{}】", callbackMap.get("FromUserName"));
						userConsumer = userModelResult.getModel();
					}
				}
				if (null != userConsumer ||	StringUtils.isNotBlank(eventKey)) {
					//登录
					quizWebSockeLogin(request, response, eventKey, sid, userConsumer);
				}
				returnStr = "";
			}
			logger.info("finally 返回给微信的内容：{}",returnStr);
			writer = response.getWriter();
			writer.print(returnStr);
			writer.flush();
		} catch (Exception e) {
			logger.info("扫码关注或登录微信回调失败 【{}】", e.getMessage(), e);
		} finally {
			if (writer!=null){
				writer.close();
			}
		}
	}


	public void quizWebSockeLogin(HttpServletRequest request, HttpServletResponse response, String eventKey, String sid, UserConsumer userConsumer) {
		logger.info("【userConsumer:{}】", JSON.toJSONString(userConsumer));
		//缓存登录信息
		cachedManager.cachedMemberSession(userConsumer, sid);
		//更新最后登录时间
		userConsumerService.updateUserConsumerLastLoginTime(userConsumer);
		logger.info("登陆接口,更新用户最新登陆时间，userId={},ninkName={}", userConsumer.getId(),
				userConsumer.getNickName());
		userConsumer.setsId(sid);
		userConsumer.setWalletRec(userWalletManager.getUserWalletRec(userConsumer.getId()).getAbleRecScore());
		//推送扫码登录结果
		loginWebSocketServerHandler.pushSingleBysceneStr(eventKey,
				JSONObject.toJSONString(userConsumer));
	}

	/* 让用户跳转到微信服务器进行授权 */
	@RequestMapping("toAuth")
	public ModelAndView toAuth(@RequestParam(required = true) String backUrl, BaseRequest baseRequest,
			HttpServletRequest request) {//
		if (StringUtils.isBlank(backUrl) || null == baseRequest.getAgentId()) {
			logger.info("跳转授权 重要参数为空！");
			// TODO 如果有错误页 重定向到错误页
			return null;
		}
		try {
			backUrl = URLDecoder.decode(backUrl, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("回调地址解码异常");
		}
		logger.info("跳转授权 收到参数【回调处理后重定向地址{}】 【其它参数{}】", backUrl, JSON.toJSONString(baseRequest));
		String stats = "{}";
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.parseObject(stats);
			jsonObject.put("agentId", baseRequest.getAgentId());
			jsonObject.put("version", baseRequest.getVersion());
			logger.info("接收到重定向地址【{}】", backUrl);
			if (!backUrl.contains("agentId=") || !backUrl.contains("clientType=")) {
				int farstIndex = backUrl.indexOf("?");
				if (-1 == farstIndex) {
					backUrl = backUrl + "?" + "agentId=" + 10006 + "&clientType=" + ClientType.WXGZH.getIndex();
				} else {
					String substring = backUrl.substring(0, backUrl.indexOf("?") + 1);
					backUrl = substring + "agentId=" + 10006 + "&clientType=" + ClientType.WXGZH.getIndex();
				}
				logger.info("重定向地址没有必要参数  后台自动设置 【{}】", backUrl);
			}
		} catch (Exception e) {
			logger.info("解析数据错误 【{}】", e.getMessage(), e);
		}
		String s = null;
		try {
			/* 获取到域名 */
			logger.info("携带参数【{}】", URLEncoder.encode(jsonObject.toJSONString(), "utf-8"));
			s = wxAccountManager.toAuth(URLEncoder.encode(jsonObject.toJSONString(), "utf-8"), backUrl);
		} catch (UnsupportedEncodingException e) {
			logger.info("重定向,参数编码错误！");
		}
		return new ModelAndView(new RedirectView(s));
	}

	@RequestMapping("baseAuth")
	@CrossOrigin
	public ModelAndView baseOauth(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true) String backUrl) {
		try {
			String code = request.getParameter("code");// 拿到的code的值
			String state = request.getParameter("state");
			try {
				state = URLDecoder.decode(state, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("回调收到参数解码异常");
			}
			logger.info("state 【{}】", state);
			logger.info("微信授权回调拿到参数 【code:{}】 【重定向地址：{}】", code, backUrl);
			redisClient.set(CODE_KEY + code, code, 10);
			WeixinUserVO userOpenIdForCode = null;
			if (code.equals(redisClient.get(CODE_KEY + code))) {
				synchronized (WxLoginController.class) {
					if (code.equals(redisClient.get(CODE_KEY + code))) {
						redisClient.del(CODE_KEY + code);
						userOpenIdForCode = wxAccountManager.getUserOpenIdForCode(code);
					}
				}
			}
			/* 用户openid 和令牌 */
			String openid = userOpenIdForCode.getOpenid();
			String access_token = userOpenIdForCode.getAccess_token();
			if (StringUtils.isBlank(openid) || StringUtils.isBlank(access_token)) {
				logger.info("获取用户信息的必要参数为空 【openid:{}】 【accessToken:{}】", openid, access_token);
			} else {
				// 获取微信用户信息
				WxAccountUserInfo userInfo = wxAccountManager.getUserInfoByOpenId(access_token, openid);
				BaseRequest baseRequest = JSONObject.parseObject(state, BaseRequest.class);
				UserConsumer userConsumer = wxAccountManager.synchronizeUserInfo(userInfo, baseRequest, request);
				if (null != userConsumer) {
					loginManager.loginAndUpdateLastLoginTime(request, response, userConsumer, MemberConstants.WX_ACCOUNT_LOGIN_COOKIE_SID);
				}
				/*
				 * return CommonResponse.withSuccessResp(UsrInfoResponse.convertByUserConsumer(
				 * userConsumer,sid));
				 */
				return new ModelAndView(new RedirectView(backUrl));
			}
		} catch (Exception e) {
			logger.info("授权回调出现异常 【{}】", e.getMessage(), e);
		}
		// TODO 如果有错误页 重定向到错误页
		return null;
	}

	/**
	 * js验证时所需的签名
	 * 
	 * @param request
	 * @param reqUrl
	 * @return
	 */
	@RequestMapping("jsCheck")
	@ResponseBody
	public Map jsCheck(HttpServletRequest request, @RequestParam(value = "reqUrl") String reqUrl) {
		Map<String, Object> wxConfig = null;
		try {
			wxConfig = wxAccountManager.getWxConfig(request, reqUrl);
		} catch (Exception e) {
			logger.info("获取微信js配置失败 【{}】", e.getMessage(), e);
		}
		return wxConfig;
	}

	@RequestMapping("getJsConfig")
	@ResponseBody
	public Map getJsConfig(HttpServletRequest request) {
		Map<String, Object> wxConfig = null;
		try {
			wxConfig = wxAccountManager.getWxConfig(request);
		} catch (Exception e) {
			logger.info("获取微信js配置失败 【{}】", e.getMessage(), e);
		}
		return wxConfig;
	}

	@RequestMapping("getAccessToken")
	@ResponseBody
	public CommonResponse<String> getAccessToken(Integer client, String timeStamp, String sign) {
		logger.info("-getAccessToken- 参数sign:{},client:{},timeStampStr:{}", sign, client, timeStamp);
		if (!"production".equals(environ)) {
			return CommonResponse.withErrorResp("Illegal environ");
		}
		if (client == null || StringUtils.isBlank(timeStamp) || StringUtils.isBlank(sign)) {
			return CommonResponse.withErrorResp("Invalid parameter");
		}
		Integer time = Integer.valueOf(WxpayUtil.getTimeStampStr()) - Integer.valueOf(timeStamp);
		if (time < 0 || time > 60 * 5) {
			return CommonResponse.withErrorResp("timeStampStr timeout");
		}
		Map<String, String> params = new HashMap<>();
		params.put("client", String.valueOf(client));
		params.put("timeStamp", timeStamp);
		String md5Sign = WxpayUtil.createMd5Sign(params, SignKey.signKey);
		if (!sign.equals(md5Sign)) {
			logger.info("-getAccessToken- 签名校验不通过");
			return CommonResponse.withErrorResp("Invalid sign");
		}
		AppIdAndSecret appIdAndSecret = AppIdAndSecret.valueOf(client);
		if (appIdAndSecret == null) {
			return CommonResponse.withErrorResp("Illegal client");
		}
		String accessToken = accessTokenUtil.getAccessToken(appIdAndSecret.getDescription(),
				appIdAndSecret.getOutDescription());
		if (StringUtils.isBlank(accessToken)) {
			return CommonResponse.withErrorResp("get accessToken error");
		}
		return CommonResponse.withSuccessResp(accessToken);
	}
	
	
	/**
	 * 生成带参数的二维码，扫描关注微信公众号，自动登录网站
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("scanLogin")
	@ResponseBody
	public Map wechatScanLogin(HttpServletRequest request){
		Map<String, Object> scanInfo = new HashMap<String, Object>();
		try {
			String sessionId=request.getSession().getId();
			String reqKey="";
			if (sessionId.lastIndexOf(".")!=-1){
				 reqKey=sessionId.substring(0, sessionId.lastIndexOf("."));
			}else {
				reqKey = sessionId;
			}

			String value = redisClient.get(reqKey);
			logger.info("value={}",value);
			if (StringUtils.isNotBlank(value)) {
				String sceneStr=value.substring(value.lastIndexOf("_")+1);
				String qrcodeUrl=value.substring(0, value.lastIndexOf("_"));
				scanInfo.put("qrcodeUrl", qrcodeUrl);
				scanInfo.put("sceneStr", sceneStr);
				logger.info("获取带参数的二维码返回结果 【scanInfo:{}】", JSONObject.toJSONString(scanInfo));
				return scanInfo;
			}
			String access_token = accessTokenUtil.getAccessToken(AppIdAndSecret.wxService.getIndex());
			String url = WeChatUtil.WEIXIN_SCAN_TOKEN_URL + access_token;
			String sceneStr = "JZDJ" + new Date().getTime();
			String params = wxAccountManager.sacnReqParams(sceneStr);
			Map<String, Object> resultMap = wxAccountManager.httpClientPost(url, params);
			String qrcodeUrl = null;
			if (resultMap.get("ticket") != null) {
				qrcodeUrl = WeChatUtil.WEIXIN_SCAN_QRCODEURL + resultMap.get("ticket");
				logger.info("获取qrcodeUrl返回结果 【qrcodeUrl:{}】", qrcodeUrl);
			}
			scanInfo.put("qrcodeUrl", qrcodeUrl);
			scanInfo.put("sceneStr", sceneStr);
			redisClient.set(reqKey, qrcodeUrl+"_"+sceneStr, 9 * 60);
		} catch (Exception e) {
			logger.info("获取微信二维码失败 【{}】", e.getMessage(), e);
		}
		return scanInfo;
	}


	/**
	 * 检查用户登录态
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLogin", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询用户登录状态", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询用户登录状态", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<String> checkLogin(HttpServletRequest request) {
		UserConsumer userConsumer = getLoginUsr(request);
		try {
			if (userConsumer == null) {
				logger.info(logCheckLogin+"用户未登录");
				return CommonResponse.withErrorResp("用户未登录");
			}
			logger.info(logCheckLogin+"用户已登录");
			return CommonResponse.withSuccessResp("用户已登录");
		} catch (Exception e) {
			logger.error("查询用户登录状态出现异常，用户id:{},异常信息:{}", userConsumer.getId(), e.getMessage());
			return CommonResponse.withErrorResp("查询用户登录状态失败");
		}
	}


	/**
	 * PC端用户退出
	 */
	@RequestMapping(value = "/wxLoginout",method = RequestMethod.POST)
	@ResponseBody
	public  CommonResponse<Void> wxLoginout(HttpServletRequest request,HttpServletResponse response){
		try {
			CookieUtils.deleteCookie(request,response,MemberConstants.WX_ACCOUNT_LOGIN_COOKIE_SID);
			logger.info(logPrefix+"删除cookie");
			return CommonResponse.withSuccessResp("退出成功");
		}catch (Exception e){
			logger.info(logPrefix+"发生异常={}",e.getMessage(),e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
}
