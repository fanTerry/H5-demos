package com.esportzoo.esport.manager;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.consumer.UserThirdLoginServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.LoginRequest;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserThirdLogin;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.util.HttpUtil;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.ThirdLoginVo;
import com.esportzoo.esport.vo.ubox.UboxLoginResponse;
import com.esportzoo.esport.vo.ubox.UboxUserInfo;
import com.esportzoo.esport.vo.ubox.UboxUserInfoResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tingting.shen
 * @date 2019/06/10
 */
@Component
public class UboxManager {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static String WECHATSESSIONKEY = "WECHATSESSIONKEY_";

	@Value("${ubox.url}")
    private String uboxUrl;
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;
	@Autowired
	private UserThirdLoginServiceClient userThirdLoginServiceClient;
	@Autowired
	private RedisClientManager redisClientManager;

	/***
	 * 绑定友宝用户
	 * 一条userconsumer
	 * 两条绑定关系：
	 *  1 与微信openId的绑定
	 *  2 与友宝用户id的绑定
	 * @param loginRequest
	 * @param baseRequest
	 * @param request
	 * @return
	 */
	public UserConsumer synchronizeUserInfo(LoginRequest loginRequest, BaseRequest baseRequest, HttpServletRequest request, String sid) {
		String logPrefix = "友宝用户同步用户信息_";
		try {
			UboxUserInfo uboxUserInfo = getUboxUser(loginRequest, sid);
			if (uboxUserInfo == null) {
				logger.info(logPrefix + "获取友宝用户信息为空");
				return null;
			}
			Long uboxUserId = uboxUserInfo.getUid();
			//此处openId是橘子电竞小程序的openId
			String openId = uboxUserInfo.getOpenId();
			String unionId = uboxUserInfo.getUnionId();
			logger.info(logPrefix + "uboxUserId={},openId={},unionId={}", uboxUserId, openId, unionId);

			UserConsumer bindUser = null;
			//根据unionid查询是否存在第三方登录
			List<UserThirdLogin> unionIdBindList =  userThirdLoginServiceClient.queryByUnionId(unionId).getModel();
			//存在unionid三方登录
			if (unionIdBindList!=null && unionIdBindList.size()>0) {
				UserThirdLogin unionIdBind = unionIdBindList.get(0);
				bindUser = userConsumerServiceClient.queryConsumerById(unionIdBind.getUserId(), null).getModel();
			} else {
				//根据openid跟三方登录类型查询
				UserThirdLogin wxBind = userManager.getUserThirdLogin(openId, ThirdType.WECHAT_MINI.getIndex());
				if (wxBind != null) {
					bindUser = userConsumerServiceClient.queryConsumerById(wxBind.getUserId(), null).getModel();
				} else {
					UserThirdLogin uboxBind = userManager.getUserThirdLogin(uboxUserId+"", ThirdType.UBOX.getIndex());
					if (uboxBind != null) {
						bindUser = userConsumerServiceClient.queryConsumerById(uboxBind.getUserId(), null).getModel();
					}
				}
			}
			//没有登录记录 注册绑定
			if (bindUser==null) {
				UserOperationParam usrOperationParm = new UserOperationParam(RequestUtil.getClientIp(request), baseRequest.getDefaultClientType().getIndex(),
						baseRequest.getAgentId(), baseRequest.getVersion());
				if (StringUtils.isBlank(uboxUserInfo.getNickName())) {
					uboxUserInfo.setNickName("mini" + DateUtil.dateToString(new Date(), "yyyyMMdd") + RandomStringUtils.random(6, false, true));
				}
				UserConsumer reqMember = new UserConsumer();
				reqMember.setNickName(uboxUserInfo.getNickName());
				reqMember.setIcon(uboxUserInfo.getAvatarUrl());
				reqMember.setStatus(UserConsumerStatus.VALID.getIndex());
				reqMember.setRegisterTime(new Date());
				reqMember.setLastLoginTime(new Date());
				reqMember.setCity(uboxUserInfo.getCity());
				reqMember.setProvince(uboxUserInfo.getProvince());
				reqMember.setGender(uboxUserInfo.getGender());
				reqMember.setRegisterType(RegisterType.third_register.getIndex());
				reqMember.setBizSystem(BizSystem.UBOX.getIndex());
				//根据分享码邀请注册
				if (StringUtils.isNotEmpty(baseRequest.getShareCode())){
					reqMember.setupFeature("shareCode", baseRequest.getShareCode());
				}
				ModelResult<UserConsumer> modelResult = userConsumerServiceClient.register(reqMember, usrOperationParm);
				if (!modelResult.isSuccess()) {
					logger.info(logPrefix + "注册失败,失败信息={}", modelResult.getErrorMsg());
					throw new BusinessException(ResponseConstant.RESP_PARAM_ERROR_CODE, modelResult.getErrorMsg());
				} else if (modelResult.getModel()==null) {
					logger.info(logPrefix + "注册失败,没有返回注册用户");
					throw new BusinessException(ResponseConstant.RESP_PARAM_ERROR_CODE, "没有返回注册用户");
				} else {
					bindUser = modelResult.getModel();
				}
			} else {
				//更新昵称头像
				if (StringUtils.isNotBlank(uboxUserInfo.getNickName()) && !uboxUserInfo.getNickName().equals(bindUser.getNickName())) {
					userConsumerServiceClient.updateConsumerNickname(bindUser.getId(), uboxUserInfo.getNickName());
				}
				if (StringUtils.isNotBlank(uboxUserInfo.getAvatarUrl()) && !uboxUserInfo.getAvatarUrl().equals(bindUser.getIcon())) {
					userConsumerServiceClient.updateConsumerIcon(bindUser.getId(), uboxUserInfo.getAvatarUrl());
				}
			}


			//来查询三方登录表中小程序跟友宝的三方登录类型是否存在 TODO 问题记录 这里查询次数可能过多
			UserThirdLogin wxBind = userManager.getUserThirdLogin(openId, ThirdType.WECHAT_MINI.getIndex());
			if (wxBind == null) {
				ThirdLoginVo thirdLoginVo = new ThirdLoginVo(unionId, openId, ThirdType.WECHAT_MINI.getIndex());
				userConsumerServiceClient.insertUserThirdLogin(thirdLoginVo, bindUser.getId());
				logger.info(logPrefix + "微信绑定关系成功,userId={},openId={},unionId={}", bindUser.getId(), openId, unionId);
			} else {
				if (StringUtils.isBlank(wxBind.getUnionId())) {
					userThirdLoginServiceClient.updateUnionId(wxBind.getId(), unionId);
				}
			}
			
			UserThirdLogin uboxBind = userManager.getUserThirdLogin(uboxUserId+"", ThirdType.UBOX.getIndex());
			if (uboxBind == null) {
				ThirdLoginVo thirdLoginVo = new ThirdLoginVo(uboxUserId+"", ThirdType.UBOX.getIndex());
				userConsumerServiceClient.insertUserThirdLogin(thirdLoginVo, bindUser.getId());
				logger.info(logPrefix + "友宝绑定关系成功,userId={},uboxUserId={}", bindUser.getId(), uboxUserId);
			} else {
				if (!uboxBind.getThirdId().equals(uboxUserId+"")) {
					userThirdLoginServiceClient.updateThirdId(uboxBind.getId(), uboxUserId+"");
					logger.info(logPrefix + "友宝id更改为uboxUserId={},之前为={}", uboxUserId, uboxBind.getThirdId());
				}
			}
			
			return bindUser;
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常exception={}", e.getMessage(), e);
			return null;
		}
	}
	
	public UboxUserInfo getUboxUser(LoginRequest loginRequest, String sid) {
		String logPrefix = "获取友宝用户信息_";
		logger.info(logPrefix + "接收到的参数loginRequest={}", JSON.toJSONString(loginRequest));
		UboxUserInfo uboxUserInfo = null;
		try {
			//获取友宝用户信息,session_key,ubox_session等 code为 小程序调用wx.login的返回值code
			UboxLoginResponse uboxLoginResponse = uboxUserLogin(loginRequest.getCode());
			
			String openid = uboxLoginResponse.getOpenid();
			String sessionKey = uboxLoginResponse.getSession_key();
			logger.info(logPrefix + "openId={},session_key={}", openid, sessionKey);
			
			String cacheValue = sessionKey + "|" + openid;
			redisClientManager.set(WECHATSESSIONKEY + sid, cacheValue,5*60);
			logger.info(logPrefix + "当前sid={}, 缓存值={}", sid, cacheValue);
			//友宝用户信息
			uboxUserInfo = uboxLoginResponse.getUserinfo();
			if (uboxUserInfo!=null) {
				logger.info(logPrefix + "调用友宝登陆接口获得用户信息uboxUserInfo={}", JSON.toJSONString(uboxUserInfo));
				return uboxUserInfo;
			}
			//此接口只是单纯的获取友宝用户信息
			UboxUserInfoResponse uboxUserInfoResponse = getUboxUserInfo(uboxLoginResponse.getUbox_session(), loginRequest.getEncrypteData(), loginRequest.getIv());
			uboxUserInfo = uboxUserInfoResponse.getData();
			logger.info(logPrefix + "调用友宝信息接口获得用户信息uboxUserInfo={}", JSON.toJSONString(uboxUserInfo));
			return uboxUserInfo;
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常， loginRequest={}, exception={}", JSON.toJSONString(loginRequest), e.getMessage(), e);
			return null;
		}
	}
	
	public UboxLoginResponse uboxUserLogin(String code) {
		String logPrefix = "友宝用户登陆接口_";
		logger.info(logPrefix + "接收到的参数code={}", code);
		try {
			String interfaceUrl = uboxUrl + "/app/login_esports?code=" + code;
			logger.info(logPrefix + "调用的接口地址interfaceUrl={}", interfaceUrl);
			String responseStr = HttpUtil.httpSSLGet(interfaceUrl, "utf-8");
			logger.info(logPrefix + "接口响应字符串responseStr={}", responseStr);
			UboxLoginResponse uboxLoginResponse = JSON.parseObject(responseStr, UboxLoginResponse.class);
			logger.info(logPrefix + "uboxLoginResponse={}", JSON.toJSONString(uboxLoginResponse));
			return uboxLoginResponse;
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常， code={}, exception={}", code, e.getMessage(), e);
			return null;
		}
	}
	
	public UboxUserInfoResponse getUboxUserInfo(String uboxSession, String encryptedData, String iv) {
		String logPrefix = "友宝用户信息接口_";
		logger.info(logPrefix + "接收到的参数 uboxSession={}, encryptedData={}, iv={}", uboxSession,  encryptedData, iv);
		try {
			String interfaceUrl = uboxUrl + "/app/userinfo_esports";
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("uboxSession", uboxSession);
			paramMap.put("encryptedData", encryptedData);
			paramMap.put("iv", iv);
			logger.info(logPrefix + "调用的接口地址interfaceUrl={},paramMap={}", interfaceUrl, JSON.toJSONString(paramMap));
			String responseStr = HttpUtil.httpSSLClientPost(interfaceUrl, paramMap, "utf-8");
			logger.info(logPrefix + "接口响应字符串responseStr={}", responseStr);
			UboxUserInfoResponse uboxUserInfoResponse = JSON.parseObject(responseStr, UboxUserInfoResponse.class);
			logger.info(logPrefix + "uboxUserInfoResponse={}", JSON.toJSONString(uboxUserInfoResponse));
			return uboxUserInfoResponse;
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常， uboxSession={}, encryptedData={}, iv={}, exception={}", 
					uboxSession,  encryptedData, iv, e.getMessage(), e);
			return null;
		}
	}

}
