package com.esportzoo.esport.manager;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.consumer.UserThirdLoginServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.BizSystem;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.RegisterType;
import com.esportzoo.esport.constants.ThirdType;
import com.esportzoo.esport.constants.UserConsumerStatus;
import com.esportzoo.esport.constants.UserOperationParam;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserThirdLogin;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.util.HttpUtil;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.ThirdLoginVo;
import com.esportzoo.esport.vo.ubox.UboxUserH5Vo;

/**
 * @author tingting.shen
 * @date 2019/07/24
 */
@Component
public class UboxH5Manager {
	
	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	public final static String OAUTH_GET_CODE_URL = "apps/uboxgame/game_open/oauth?app_name=AppName&redirect=EncodedCallBack";
	public final static String OAUTH_GET_USER_URL = "game_open/load_user?app_name=AppName&uboxauthcode=UBOX_CODE&timestamp=TimeStamp&sign=SignStr";
	
	@Value("${ubox.h5.code.domain}")
    private String uboxH5CodeDomain;
	
	@Value("${ubox.h5.user.domain}")
    private String uboxH5UserDomain;
	
	@Value("${ubox.h5.appname}")
    private String uboxH5AppName;
	
	@Value("${ubox.h5.appkey}")
    private String uboxH5AppKey;
	
	@Value("${ubox.h5.callback.url}")
    private String uboxH5CallbackUrl;
	
	@Value("${ubox.h5.callback.url1}")
    private String uboxH5CallbackUrl1;
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;
	@Autowired
	@Qualifier("cachedManager")
	private CachedManager cachedManager;
	
	
	public boolean uboxUserRegistAndLogin(UboxUserH5Vo uboxUserH5Vo, Long channelNo, HttpServletRequest request, HttpServletResponse response) {
		String logPrefix = "友宝用户h5登陆_";
		if (uboxUserH5Vo == null) {
			logger.info(logPrefix + "友宝用户信息为空");
			return false;
		}
		if (StringUtils.isBlank(uboxUserH5Vo.getId())) {
			logger.info(logPrefix + "友宝用户Id为空");
			return false;
		}
		UserThirdLogin uboxBind = userManager.getUserThirdLogin(uboxUserH5Vo.getId(), ThirdType.UBOX.getIndex());
		UserConsumer user = null;
		if (uboxBind != null) {
			user = userConsumerServiceClient.queryConsumerById(uboxBind.getUserId(), null).getModel();
			logger.info(logPrefix + "该友宝用户已经有绑定关系，uboxId={},userId={}", uboxUserH5Vo.getId(), uboxBind.getUserId());
		} else {
			UserOperationParam usrOperationParm = new UserOperationParam(RequestUtil.getClientIp(request),ClientType.H5.getIndex(), channelNo, "1.0");
			if (StringUtils.isBlank(uboxUserH5Vo.getNickName())) {
				uboxUserH5Vo.setNickName("ubox" + DateUtil.dateToString(new Date(), "yyyyMMdd") + RandomStringUtils.random(6, false, true));
			}
			UserConsumer reqMember = new UserConsumer();
			reqMember.setNickName(uboxUserH5Vo.getNickName());
			reqMember.setIcon(StringUtils.isBlank(uboxUserH5Vo.getIcon()) ? "" : uboxUserH5Vo.getIcon());
			reqMember.setStatus(UserConsumerStatus.VALID.getIndex());
			reqMember.setRegisterTime(new Date());
			reqMember.setLastLoginTime(new Date());
			reqMember.setRegisterType(RegisterType.third_register.getIndex());
			reqMember.setBizSystem(BizSystem.UBOX.getIndex());
			ModelResult<UserConsumer> modelResult = userConsumerServiceClient.register(reqMember, usrOperationParm);
			if (!modelResult.isSuccess()) {
				logger.info(logPrefix + "注册失败,失败信息={}", modelResult.getErrorMsg());
				return false;
			} else if (modelResult.getModel()==null) {
				logger.info(logPrefix + "注册失败,没有返回注册用户");
				return false;
			} else {
				logger.info(logPrefix + "注册成功");
				user = modelResult.getModel();
			}
			ThirdLoginVo thirdLoginVo = new ThirdLoginVo(uboxUserH5Vo.getId(), ThirdType.UBOX.getIndex());
			ModelResult<UserThirdLogin> thirdLogin = userConsumerServiceClient.insertUserThirdLogin(thirdLoginVo, user.getId());
			if (!thirdLogin.isSuccess()) {
				logger.info(logPrefix + "友宝绑定关系失败,原因={},userId={},uboxUserId={}", thirdLogin.getErrorMsg(), user.getId(), uboxUserH5Vo.getId());
				return false;
			}
			logger.info(logPrefix + "友宝绑定关系成功,userId={},uboxUserId={}", user.getId(), uboxUserH5Vo.getId());
		}
		
		String sid = UUID.randomUUID().toString();
	    cachedManager.cachedMemberSession(user, sid);
	    CookieUtils.setCookie(request, response, MemberConstants.UBOX_LOGIN_COOKIE_SID, sid);
	    CookieUtils.setCookie(request, response, "uboxh5"+user.getId(), "uboxh5");
	    
	    userConsumerServiceClient.updateUserConsumerLastLoginTime(user);
	    logger.info(logPrefix + "更新用户最新登陆时间，userId={}", user.getId());
	    return true;
	}
	
	
	public String getOauthRequestUrl(String agentId, String biz, String redirect) throws Exception {
		String encodedCallBack = URLEncoder.encode(uboxH5CallbackUrl+ redirect +"&agentId="+agentId+"&biz="+biz, "UTF-8");
		String requestUrl = uboxH5CodeDomain + OAUTH_GET_CODE_URL;
		requestUrl = requestUrl.replace("AppName", uboxH5AppName);
		requestUrl = requestUrl.replace("EncodedCallBack", encodedCallBack);
		logger.info("获取友宝授权地址" + "requestUrl={}", requestUrl);
		return requestUrl;
	}
	
	public UboxUserH5Vo getUboxUserInfo(String authCode) throws Exception {
		String logPrefix = "向友宝发送获取用户信息请求_";
		String timestamp = new Date().getTime()+"";
		String[] strs= {authCode, uboxH5AppName, timestamp};
		String signStr = uboxSign(strs);
		String requestUrl = uboxH5UserDomain + OAUTH_GET_USER_URL;
		requestUrl = requestUrl.replace("AppName", uboxH5AppName);
		requestUrl = requestUrl.replace("UBOX_CODE", authCode);
		requestUrl = requestUrl.replace("TimeStamp", timestamp);
		requestUrl = requestUrl.replace("SignStr", signStr);
		logger.info(logPrefix + "requestUrl={}", requestUrl);
		String responseStr = HttpUtil.httpSSLGet(requestUrl, "utf-8");
		logger.info(logPrefix + "响应字符串responseStr={}", responseStr);
		JSONObject json = JSONObject.parseObject(responseStr);
		if (json.getIntValue("code") != 200) {
			logger.info(logPrefix + "错误码={},错误信息={}", json.getIntValue("code"), json.getString("msg"));
			return null;
		}
		JSONObject data = (JSONObject) json.get("data");
		UboxUserH5Vo uboxUserInfo = new UboxUserH5Vo();
		uboxUserInfo.setId(data.getString("ubox_user_id"));
		uboxUserInfo.setNickName(data.getString("nickname"));
		uboxUserInfo.setIcon(data.getString("headimgurl"));
		logger.info(logPrefix + "uboxUserInfo={}", JSON.toJSONString(uboxUserInfo));
		return uboxUserInfo;
	}
	
	public String uboxSign(String[] data) throws Exception {
        Arrays.sort(data);
        String signStr = StringUtils.join(data);
        signStr += uboxH5AppKey;
        return sha1(signStr);
    }
	
	public static String sha1(String signStr) throws Exception{
    	MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(signStr.getBytes("UTF-8"));
        
        Formatter formatter = new Formatter();
        for (byte b : crypt.digest()){
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
	
	public String getOauthRequestUrl1(String redirect, String agentId) throws Exception {
		String callBackUrl = uboxH5CallbackUrl1 + "agentId=" + agentId + "&callBackUrl=" + redirect ;
		String encodedCallBack = URLEncoder.encode(callBackUrl, "UTF-8");
		String requestUrl = uboxH5CodeDomain + OAUTH_GET_CODE_URL;
		requestUrl = requestUrl.replace("AppName", uboxH5AppName);
		requestUrl = requestUrl.replace("EncodedCallBack", encodedCallBack);
		logger.info("获取友宝授权地址1" + "requestUrl={}", requestUrl);
		return requestUrl;
	}
	
}
