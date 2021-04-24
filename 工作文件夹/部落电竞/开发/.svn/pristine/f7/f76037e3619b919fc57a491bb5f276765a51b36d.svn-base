package com.esportzoo.esport.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.esport.util.DesUtil;
import com.esportzoo.common.util.RsaCryptoUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.LoginRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.H5LoginUserResponse;
import com.esportzoo.esport.connect.response.UsrInfoResponse;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.UserConsumerStatus;
import com.esportzoo.esport.constants.sms.SendSmsEnum;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.manager.RedisClientManager;
import com.esportzoo.esport.manager.UboxManager;
import com.esportzoo.esport.manager.WeChatManager;
import com.esportzoo.esport.util.MD5;
import com.esportzoo.esport.util.RegisterValidUtil;
import com.esportzoo.esport.vo.user.PhoneLoginRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 登录
 * 
 * @author: wujing
 * @date:2019年4月18日下午4:36:05
 */
@Controller
@Api(value = "登录接口", tags = { "小程序登录controller" })
public class LoginController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static List<Long> uboxChannelList = new ArrayList<>();
	static {
		uboxChannelList.add(100101L);// 友宝渠道小程序
		uboxChannelList.add(100102L);// 友宝渠道公众号
	}
	@Autowired
	private UboxManager uboxManager;
	@Autowired
	private WeChatManager weChatManager;
	@Autowired
	@Qualifier("cachedManager")
	private CachedManager cachedManager;
	@Autowired
	private RedisClientManager redisClientManager;
	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;

	@RequestMapping(value = "/login", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "登录接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "首页数据POST", response = CommonResponse.class)
	public @ResponseBody CommonResponse<UsrInfoResponse> doLogin(@ModelAttribute LoginRequest loginRequest, BaseRequest baseRequest, HttpServletRequest request) {
		logger.info("请求登录接口,请求参数loginRequest={}, baseRequest={}", JSONObject.toJSONString(loginRequest), JSONObject.toJSONString(baseRequest));
		try {
			String sid = UUID.randomUUID().toString();// uuid生成唯一key
			UserConsumer userConsumer = null;
			if (uboxChannelList.contains(baseRequest.getAgentId())) {
				userConsumer = uboxManager.synchronizeUserInfo(loginRequest, baseRequest, request, sid);
			} else {
				userConsumer = weChatManager.synchronizeUserInfo(loginRequest, baseRequest, request, sid);
			}
			if (null != userConsumer) {
				cachedManager.cachedMemberSession(userConsumer, sid);// 缓存一份新的

				userConsumerServiceClient.updateUserConsumerLastLoginTime(userConsumer);
				logger.info("登陆接口,更新用户最新登陆时间，userId={},ninkName={}", userConsumer.getId(), userConsumer.getNickName());

				return CommonResponse.withSuccessResp(UsrInfoResponse.convertByUserConsumer(userConsumer, sid));
			}
		} catch (Exception e) {
			logger.info("调用登录接口异常{}", e);
		}
		return CommonResponse.withErrorResp("获取用户数据异常");
	}

	@RequestMapping(value = "/phonelogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<H5LoginUserResponse> phoneLogin(PhoneLoginRequest phoneLoginRequest, HttpServletRequest request, HttpServletResponse response) {
		String logPrefix = "手机登陆请求_";
        ModelResult<List<UserConsumer>> modelResult =null;
		logger.info(logPrefix + "请求参数phoneLoginRequest={}", JSONObject.toJSONString(phoneLoginRequest));
		try {
			/*if (phoneLoginRequest.getBiz() != null && phoneLoginRequest.getBiz().intValue() != BizSystem.LOCAL.getIndex()) {
				logger.info(logPrefix + "第三方系统禁止使用账号密码登录");
				return CommonResponse.withErrorResp("第三方系统禁止使用账号密码登录");
			}*/
			String accountOrPhone = phoneLoginRequest.getAccountOrPhone();
			String encryPassword = phoneLoginRequest.getPassword();
			if (StringUtils.isBlank(accountOrPhone) || StringUtils.isBlank(encryPassword)) {
				logger.info(logPrefix + "必要参数为空");
				return CommonResponse.withErrorResp("必要参数为空");
			}
			String password = RsaCryptoUtil.decryptByPrivateKey(encryPassword, RsaCryptoUtil.privateKey, null);
			 modelResult = userConsumerServiceClient.queryConsumerByAccountOrPhone(accountOrPhone);
			if (!modelResult.isSuccess() || modelResult.getModel() == null || modelResult.getModel().size() <= 0) {
                modelResult = userConsumerServiceClient.queryConsumerByAccountOrPhone(DesUtil.encrypt(accountOrPhone));
                if (!modelResult.isSuccess() || modelResult.getModel() == null || modelResult.getModel().size() <= 0) {
                    logger.info(logPrefix + "没有此用户");
                    return CommonResponse.withErrorResp("没有此用户");
                }
			}
			List<UserConsumer> userList = modelResult.getModel();
			userList = userList.stream().filter(e -> e.getStatus().intValue() == UserConsumerStatus.VALID.getIndex()
					/*&& (e.getRegisterType().intValue() == RegisterType.phone_register.getIndex() || e.getRegisterType().intValue() == RegisterType.system_register.getIndex())*/).collect(Collectors.toList());
			if (userList == null || userList.size() <= 0) {
				logger.info(logPrefix + "用户状态无效");
				return CommonResponse.withErrorResp("用户状态无效");
			}
			userList = userList.stream().sorted((e1, e2) -> (int) (e2.getId() - e1.getId())).collect(Collectors.toList());
			UserConsumer user = userList.get(0);
			if (!MD5.md5Encode(password).equals(user.getPassword())) {
				logger.info(logPrefix + "密码错误");
				return CommonResponse.withErrorResp("密码错误");
			}
			/*if (user.getBizSystem() != BizSystem.LOCAL.getIndex()) {
				logger.info(logPrefix + "第三方系统禁止使用账号密码登录1");
				return CommonResponse.withErrorResp("第三方系统禁止使用账号密码登录1");
			}*/

			String sid = UUID.randomUUID().toString();
			CookieUtils.deleteCookie(request, response,MemberConstants.H5_LOGIN_COOKIE_SID);
			if (null != phoneLoginRequest.getClientType()
					&& (phoneLoginRequest.getClientType().intValue() == ClientType.ANDROID.getIndex()
					|| phoneLoginRequest.getClientType().intValue() == ClientType.IOS.getIndex())) {
				logger.info(logPrefix + "手机号登录,当前是clientType是:{},单独设置cookie", phoneLoginRequest.getClientType());
				CookieUtils.setCookie(request, response, MemberConstants.H5_LOGIN_COOKIE_SID, sid, 30 * 24 * 60 * 60);
				cachedManager.cachedMemberSessionByDay(user, sid);
			} else {
				CookieUtils.setCookie(request, response, MemberConstants.H5_LOGIN_COOKIE_SID, sid, 24 * 60 * 60);
				cachedManager.cachedMemberSession(user, sid);
			}

			userConsumerServiceClient.updateUserConsumerLastLoginTime(user);
			logger.info(logPrefix + "更新用户最新登陆时间，userId={},phone={}", user.getId(), user.getPhone());
			H5LoginUserResponse userResponse = new H5LoginUserResponse();
			userResponse.setToken(sid);
			BeanUtils.copyProperties(userResponse, user);
			return CommonResponse.withSuccessResp(userResponse);
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	// 手机号&验证码登录
	@RequestMapping(value = "/phoneCodeLogin", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<H5LoginUserResponse> phoneLogin(String phone, String code, HttpServletRequest request, HttpServletResponse response) {
		if (StrUtil.isBlank(phone) || StrUtil.isBlank(code)) {
			return CommonResponse.withErrorResp("请填写正确的手机号码与验证码！");
		}
		try {
			// 检查手机
			RegisterValidUtil.validPhone(phone);
			String cacheKey = StrUtil.format(SendSmsEnum.LOG_IN_CODE.getCacheKey(), phone);
			String s = redisClientManager.get(cacheKey);
			if (StrUtil.isBlank(s)) {
				return CommonResponse.withErrorResp("验证码已失效！");
			}
			if (!code.equalsIgnoreCase(s)) {
				return CommonResponse.withErrorResp("验证码错误！");
			}
			ModelResult<List<UserConsumer>> consumerByPhone = userConsumerServiceClient.queryConsumerByPhone(phone);
			// 手机号绑定用户 是一对一的关系
			List<UserConsumer> userConsumers = consumerByPhone.getModel();
			if (!consumerByPhone.isSuccess() || null==userConsumers) {
				return CommonResponse.withErrorResp("服务器异常！");
			}
			if (1 != userConsumers.size()){
			    logger.info("手机验证码登录 根据手机号[{}] 查到用户数量[{}]", phone,userConsumers.size());
				return CommonResponse.withErrorResp("服务器异常！");
			}
			UserConsumer userConsumer = userConsumers.get(0);
			if (null != userConsumer) {
				if (userConsumer.getStatus().intValue()==UserConsumerStatus.INVALID.getIndex()){
					return CommonResponse.withErrorResp("无效用户！");
				}
				String sid = UUID.randomUUID().toString();
				CookieUtils.setCookie(request, response, MemberConstants.H5_LOGIN_COOKIE_SID, sid,
						24 * 60 * 60);
				cachedManager.cachedMemberSession(userConsumer, sid);
				userConsumerServiceClient.updateUserConsumerLastLoginTime(userConsumer);
				logger.info("登陆接口,更新用户最新登陆时间，userId={},ninkName={}", userConsumer.getId(),
						userConsumer.getNickName());
			}
			H5LoginUserResponse userResponse = new H5LoginUserResponse();
			BeanUtil.copyProperties(userConsumer,userResponse);
			return CommonResponse.withSuccessResp(userResponse);
		} catch (Exception e) {
			logger.info("用户手机号登录发生异常 phone[{}] [{}]", phone, e.getMessage(), e);
			return CommonResponse.withErrorResp("服务器异常！");
		}
	}

	@RequestMapping(value = "/phoneloginout", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Void> phoneLoginout(HttpServletRequest request, HttpServletResponse response) {
		String logPrefix = "手机登陆退出请求_";
		try {
			CookieUtils.deleteCookie(request, response, MemberConstants.H5_LOGIN_COOKIE_SID);
			logger.info(logPrefix + "删除cookie");
			return CommonResponse.withSuccessResp("退出成功");
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	public static void main(String[] args) {
		System.out.println(MD5.md5Encode("123123"));
	}

}
