package com.esportzoo.esport.controller.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.wallet.UserWalletLogRecServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.user.WalletLogReqeust;
import com.esportzoo.esport.connect.response.BindingPhoneResponse;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.sms.SendSmsEnum;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserCenterInfoVo;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserWalletLogRec;
import com.esportzoo.esport.manager.RedisClientManager;
import com.esportzoo.esport.manager.UserCenterManager;
import com.esportzoo.esport.manager.UserWalletManager;
import com.esportzoo.esport.vo.wallet.WalletLogQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * @description: 个人中心数据获取
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-20 09:29
 **/
@Controller
@RequestMapping("usercenter")
public class UserCenterController  extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static String loggerPre = "查询用户星星流水_";

	@Autowired
	private UserCenterManager userCenterManager;
	@Autowired
	private UserWalletManager userWalletManager;
	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;

	@Autowired
	private UserWalletLogRecServiceClient userWalletLogServiceClient;

	@Autowired
	private RedisClientManager redisClientManager;


	@RequestMapping(value = "/ucIndexdata", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "个人中心首页数据接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "个人中心首页数据接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<UserCenterInfoVo> ucIndexdata(HttpServletRequest request, BaseRequest baseRequest) {
		UserCenterInfoVo userCenterInfoVo = new UserCenterInfoVo();
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer != null) {
			userCenterInfoVo = userCenterManager.getUserInfo(userConsumer.getId());
		}
		SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.CHARGE_SHOW_SWITCH, baseRequest.getClientType(),baseRequest.getAgentId());
		String value = sysConfigProperty.getValue();
		if (StringUtils.isNotBlank(value) && value.trim().equals("1")) {// 充值是关闭的
			userCenterInfoVo.setChargeFlag(true);
		}
		SysConfigProperty sysConfigPropertyByKey = getSysConfigByKey(SysConfigPropertyKey.WECHAT_MINI_SHOP_SWITCH, baseRequest.getClientType(),baseRequest.getAgentId());
		if (null != sysConfigPropertyByKey && StringUtils.isNotBlank(sysConfigPropertyByKey.getValue()) && sysConfigPropertyByKey.getValue().trim().equals("1")) {
			userCenterInfoVo.setShopFlag(true);
		}
		return CommonResponse.withSuccessResp(userCenterInfoVo);
	}


	@RequestMapping(value = "/getWalletLog", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取用户钱包流水", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取用户钱包流水", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> getWalletLog(HttpServletRequest request, WalletLogReqeust walletLogReqeust) {
		UserConsumer userConsumer = getLoginUsr(request);
		DataPage<UserWalletLogRec> page = new DataPage<>();
		if (walletLogReqeust.getPageNo() != null) {
			page.setPageNo(walletLogReqeust.getPageNo());
		}
		if (walletLogReqeust.getPageSize() != null) {
			page.setPageSize(walletLogReqeust.getPageSize());
		}
		CommonResponse<JSONObject> commonResponse = userWalletManager.getPageUserWalletLog(userConsumer.getId(), page,walletLogReqeust);
		return commonResponse;
	}


	@RequestMapping(value = "/getWalletPCLog", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "PC获取用户钱包流水", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "PC获取用户钱包流水", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> getWalletPCLog(HttpServletRequest request, WalletLogReqeust walletLogReqeust) {
		JSONObject jsonObject = new JSONObject();
		UserConsumer userConsumer = getLoginUsr(request);
		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			DataPage<UserWalletLogRec> page = new DataPage<>();
			if (walletLogReqeust.getPageNo() == null || walletLogReqeust.getPageSize() == null ) {
					logger.error(loggerPre + "必要参数有误，walletLogReqeust{}",JSON.toJSONString(walletLogReqeust));
				return CommonResponse.withErrorResp("分页参数有误");
			}
			page.setPageNo(walletLogReqeust.getPageNo());
			page.setPageSize(walletLogReqeust.getPageSize());
			WalletLogQueryVo queryVo = new WalletLogQueryVo();
			queryVo.setUserId(Integer.parseInt(userConsumer.getId().toString()));
			//按照时间倒序
			queryVo.setOrderBy("create_time DESC");
//			queryVo.setClientType(walletLogReqeust.getClientType());
			PageResult<UserWalletLogRec> pageResult = userWalletLogServiceClient.queryPage(queryVo, page);
			if (!pageResult.isSuccess()){
				logger.info(loggerPre+"errorMsg={},queryVo={}",pageResult.getErrorMsg(), JSON.toJSONString(queryVo));
				return CommonResponse.withErrorResp(pageResult.getErrorMsg());
			}
			jsonObject.put("dataList",pageResult.getPage().getDataList());
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.error(loggerPre + "查询用户星星流水出现异常，用户id:{}", userConsumer.getId(), e);
			return CommonResponse.withErrorResp("查询用户星星流水出现异常");
		}
	}


	//绑定手机
	@RequestMapping(value = "/bindingPhone", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse bindingPhone(HttpServletRequest request,String code,String phone){
		String prefix="绑定手机号_";
		if (StrUtil.isBlank(code) || StrUtil.isBlank(phone)){
			logger.info(prefix+"重要参数不能为空 code[{}] phone[{}]",code,phone);
			return CommonResponse.withErrorResp("重要参数为空！");
		}
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (null==userConsumer){
				return CommonResponse.withErrorResp("请先登录!");
			}
			if (StrUtil.isNotBlank(userConsumer.getPhone())){
				logger.info("用户[{}] 已经绑定手机[{}] 不能再次绑定！", userConsumer.getId(),userConsumer.getPhone());
				return CommonResponse.withErrorResp("已绑定手机!");
			}
			String cacheKey = StrUtil.format(SendSmsEnum.BINDING_CODE.getCacheKey(), phone);
			if (!redisClientManager.hasKey(cacheKey)) {
				logger.info(prefix + "验证码失效");
				return CommonResponse.withErrorResp("验证码失效");
			}
			String cacheCode = redisClientManager.get(cacheKey);
			if (!code.equals(cacheCode)) {
				logger.info(prefix + "验证码不正确");
				return CommonResponse.withErrorResp("验证码不正确");
			}
			ModelResult<Boolean> booleanModelResult = userConsumerServiceClient.updateConsumerPhone(userConsumer.getId(), phone, null);
			Boolean model = booleanModelResult.getModel();
			if (!booleanModelResult.isSuccess()|| null==model || !model){
				logger.info(prefix+"绑定手机号[{}]异常[{}]", phone,booleanModelResult.getErrorMsg());
				return CommonResponse.withErrorResp("绑定失败!");
			}
			BindingPhoneResponse res = new BindingPhoneResponse();
			res.setSuccess(true);
			res.setPhone(phone);
			//更新缓存信息
			userConsumer.setPhone(phone);
			cachedManager.cachedMemberSession(userConsumer, userConsumer.getsId());
			return CommonResponse.withSuccessResp(res);
		} catch (Exception e) {
			logger.info(prefix + "发生异常exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}

	}
}
