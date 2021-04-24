package com.esportzoo.esport.controller.yeyun;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.client.service.consumer.UserThirdLoginServiceClient;
import com.esportzoo.esport.client.service.yeyun.YeYunUserServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.yeyun.YeYunUserInfoResponse;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.yeyun.user.YeYunUrl;
import com.esportzoo.esport.constants.yeyun.user.YeYunUserInfo;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tingjun.wang
 * @date 2019/9/24 18:22
 */
@Controller
@RequestMapping("yeYunUser")
@Api(value = "椰云用户相关接口", tags = { "椰云用户相关接口" })
public class YeYunUserController extends BaseController {

	private transient Logger logger = LoggerFactory.getLogger(YeYunUserController.class);
	public static final String logPrefix = "椰云积分系统用户接口_";

	@Autowired
	@Qualifier("yeYunUserServiceClient")
	private YeYunUserServiceClient yeYunUserServiceClient;

	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;

	@Autowired
	@Qualifier("userThirdLoginServiceClient")
	private UserThirdLoginServiceClient userThirdLoginServiceClient;
	@Value("${environ}")
	private String environ;

	@RequestMapping(value = "/getYeYunUrlList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取椰云各模块地址", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取椰云各模块地址", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<String>> getYeYunUrlList(HttpServletRequest request){
		Long userId = null;
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null){
				return CommonResponse.withErrorResp("请先登录");
			}
			String token = "";
			userId = userConsumer.getId();
			ModelResult<String> modelResult = yeYunUserServiceClient.getTokenAndIndexUrl(userId);
			if (!modelResult.isSuccess() || modelResult.getModel() == null) {
				logger.info("{}获取椰云登录token，错误：{}", logPrefix, modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("获取椰云登录token失败");
			}
			token = modelResult.getModel();
			List<String> urlList = YeYunUrl.getUrlList();
			List<String> urlListResult = new ArrayList<>(urlList.size());
			String urlPre = "daily".equals(environ)?"https://t":"https://";
			for (int i = 0; i < urlList.size(); i++) {
				urlListResult.add(urlPre+urlList.get(i)+token);
			}
			return CommonResponse.withSuccessResp(urlListResult);
		}catch (Exception e){
			logger.error("{}获取椰云各模块地址异常，参数userId：{}",logPrefix,userId,e);
			return CommonResponse.withErrorResp("获取椰云登录token异常");
		}
	}


	@RequestMapping(value = "/getYeYunIndex", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取椰云首页地址", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取椰云首页地址", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<String> getYeYunIndex(HttpServletRequest request){
		Long userId = null;
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			String token = "";
			logger.info("用户[{}]请求获取椰云首页地址",userConsumer == null?null:userConsumer.getId());
			if (userConsumer != null) {
				userId = userConsumer.getId();
				ModelResult<String> modelResult = yeYunUserServiceClient.getTokenAndIndexUrl(userId);
				if (!modelResult.isSuccess() || modelResult.getModel() == null) {
					logger.info("{}获取椰云登录token，错误：{}", logPrefix, modelResult.getErrorMsg());
					return CommonResponse.withErrorResp("获取椰云登录token失败");
				}
				token = modelResult.getModel();
			}
			String urlPre = ("daily".equals(environ)?"https://t":"https://");
			String indexUrl =  urlPre + YeYunUrl.INDEX_URL +token;
			String centerUrl = urlPre + YeYunUrl.USER_CENTER +token;
			CommonResponse<String> response = CommonResponse.withSuccessResp(indexUrl);
			logger.info("用户[{}]获取椰云首页地址:{}",userConsumer.getId(),JSON.toJSONString(response));
			response.setData(centerUrl);
			return response;
		}catch (Exception e){
			logger.error("{}获取椰云首页地址异常，参数userId：{}",logPrefix,userId,e);
			return CommonResponse.withErrorResp("获取椰云首页地址异常");
		}
	}

	@RequestMapping(value = "/yeYunUserInfo", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取椰云用户信息", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取椰云用户信息接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<YeYunUserInfoResponse> yeYunUserInfo(HttpServletRequest request){
		CommonResponse<YeYunUserInfoResponse> commonResponse = new CommonResponse<>();
		Long userId = null;
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}获取用户信息，未获取到登录信息",logPrefix);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			userId = userConsumer.getId();

			ModelResult<YeYunUserInfo> modelResult = yeYunUserServiceClient.getUserInfoById(userId);
			if (!modelResult.isSuccess() || modelResult.getModel() == null) {
				logger.info("{}获取用户信息，错误：{}", logPrefix, modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("获取用户信息失败");
			}
			YeYunUserInfoResponse yeYunUserInfo = new YeYunUserInfoResponse();
			BeanUtils.copyProperties(modelResult.getModel(),yeYunUserInfo, "userId","myUserId");
			//一个用户一天可兑换限制值
			String exchangeRecLimit = sysConfigPropertyServiceClient.getConfigValueByKey(SysConfigPropertyKey.YEYUN_USER_DAY_EXCHANGE_REC);
			if (StringUtils.isNotBlank(exchangeRecLimit)) {
				BigDecimal payScoreLimit = new BigDecimal(exchangeRecLimit.trim());
				yeYunUserInfo.setExchangeScore(payScoreLimit);
			}
			//兑换比例
			String exchangeRatio = sysConfigPropertyServiceClient.getConfigValueByKey(SysConfigPropertyKey.YEYUN_EXCHANGE_RATIO);
			if (StringUtils.isNotBlank(exchangeRatio)){
				yeYunUserInfo.setExchangeRatio(exchangeRatio);
			}
			return commonResponse.withSuccessResp(yeYunUserInfo);
		}catch (Exception e){
			logger.error("{}获取用户信息异常，参数userId：{}",logPrefix,userId,e);
			return CommonResponse.withErrorResp("获取用户信息异常");
		}
	}

	/**
	 * 重定向跳转到椰云积分，并登录
	 * */
	@RequestMapping("/yyHome")
	public ModelAndView yeYunLogin(BaseRequest baseRequest, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			CommonResponse<String> yeYunIndex = getYeYunIndex(request);
			if (yeYunIndex.getCode().equals("200")){
				modelAndView.setView(new RedirectView(yeYunIndex.getMessage()));
				return modelAndView;
			}
			logger.info("获取椰云登录地址异常，response：{}",JSON.toJSONString(yeYunIndex));
		}catch (Exception e){
			logger.error("重定向跳转到椰云积分异常",e);
		}
		return null;
	}
}
