package com.esportzoo.esport.controller.yeyun;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserThirdLoginServiceClient;
import com.esportzoo.esport.client.service.yeyun.YeYunOrderServiceClient;
import com.esportzoo.esport.client.service.yeyun.YeYunUserServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.yeyun.YeYunOrderRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.yeyun.YeYunOrderInfoResponse;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.constants.yeyun.order.YeYunOrderInfo;
import com.esportzoo.esport.constants.yeyun.user.YeYunUserInfo;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserThirdLogin;
import com.esportzoo.esport.service.common.SysConfigPropertyService;
import com.esportzoo.esport.service.exception.ExchangeException;
import com.esportzoo.esport.service.exception.errorcode.WalletErrorTable;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.yeyun.YeYunOrderParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tingjun.wang
 * @date 2019/9/24 18:22
 */
@Controller
@RequestMapping("yeYunOrder")
@Api(value = "椰云订单相关接口", tags = { "椰云订单相关接口" })
public class YeYunOrderController extends BaseController {

	private transient Logger logger = LoggerFactory.getLogger(YeYunOrderController.class);
	public static final String logPrefix = "椰云积分系统订单接口_";

	@Autowired
	@Qualifier("yeYunOrderServiceClient")
	private YeYunOrderServiceClient yeYunOrderServiceClient;
	@Autowired
	@Qualifier("userThirdLoginServiceClient")
	private UserThirdLoginServiceClient userThirdLoginServiceClient;
	@Autowired
	@Qualifier("yeYunUserServiceClient")
	private YeYunUserServiceClient yeYunUserServiceClient;
	@Autowired
	private SysConfigPropertyService sysConfigPropertyService;



	@ApiOperation(value = "椰子积分兑换接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/exchangeYeYunScore", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> exchangeYeYunScore(@ApiParam(required = true, name = "兑换金额") BigDecimal walletScore, BaseRequest baseRequest, HttpServletRequest request){
		CommonResponse<String> commonResponse = new CommonResponse<>();
		Long userId = null;
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}兑换椰云积分，未获取到登录信息",logPrefix);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			userId = userConsumer.getId();
			logger.info("param：{}",JSON.toJSONString(baseRequest));
			//金额最小和倍数限制
			String exchangeMix = sysConfigPropertyService.getValueByCondition(SysConfigPropertyKey.YEYUN_EXCHANGE_MIX,baseRequest.getClientType(),baseRequest.getAgentId());
			if (walletScore.compareTo(new BigDecimal(exchangeMix)) <0 || walletScore.intValue()%1000 != 0){
				logger.info("椰子积分兑换，交易金额小于最小值 walletScore：{}",walletScore);
				throw new ExchangeException(WalletErrorTable.ILLEGAL_TRANS_AMOUNT.code, WalletErrorTable.ILLEGAL_TRANS_AMOUNT.msg);
			}

			//获取用户第三方登录信息
			String thirdUserId = null;
			ModelResult<List<UserThirdLogin>> thirdLoginResult = userThirdLoginServiceClient.queryByConsumerIdAndTypeForList(userId, ThirdType.YEYUN_SCORE.getIndex());
			List<UserThirdLogin> userThirds = thirdLoginResult.getModel();
			if (CollectionUtil.isEmpty(userThirds)){
				//用户未绑定，前往绑定
				logger.info("{}兑换椰云积分，{}，{}前往绑定",logPrefix,userConsumer.getNickName(),userId);
				ModelResult<YeYunUserInfo> yeYunUserInfoResult = yeYunUserServiceClient.bindUser(userId);
				if (yeYunUserInfoResult.getModel() == null || StringUtils.isEmpty(yeYunUserInfoResult.getModel().getUserId())){
					logger.info("{}兑换椰云积分，用户未绑定时去绑定发生异常",logPrefix,yeYunUserInfoResult.getErrorMsg());
					return CommonResponse.withErrorResp("绑定椰云用户异常");
				}
				thirdUserId = yeYunUserInfoResult.getModel().getUserId();
			}else if (userThirds.size()!=1 || userThirds.get(0).getStatus()!= ThirdLoginStatus.ACTIVE.getIndex()){
				logger.info("{}兑换椰云积分，用户状态【{}】异常",logPrefix,userThirds.get(0).getStatus());
				return CommonResponse.withErrorResp("用户状态异常");
			}
			if (thirdUserId == null){
				thirdUserId = userThirds.get(0).getThirdId();
			}
			String exchangeRatio = sysConfigPropertyService.getValueByCondition(SysConfigPropertyKey.YEYUN_EXCHANGE_RATIO,baseRequest.getClientType(),baseRequest.getAgentId());
			//组装参数
			YeYunOrderParam yeYunOrderParam = new YeYunOrderParam();
			yeYunOrderParam.setPointNum(walletScore.divide(new BigDecimal(exchangeRatio)));
			yeYunOrderParam.setUniqueUserId(userId);
			yeYunOrderParam.setWalletScore(walletScore);
			yeYunOrderParam.setAccount(userConsumer.getAccount());
			yeYunOrderParam.setThirdUserId(thirdUserId);
			UserOperationParam userOperationParam = new UserOperationParam();
			userOperationParam.setOperIp(RequestUtil.getClientIp(request));
			userOperationParam.setClientType(baseRequest.getClientType());
			userOperationParam.setChannelNo(baseRequest.getAgentId());
			userOperationParam.setOpRemark(ClientType.valueOf(baseRequest.getClientType()).getDescription()+"_兑换椰子积分");

			logger.info("{}兑换椰云积分，{}，兑换参数：{}",logPrefix,userConsumer.getNickName(),JSON.toJSONString(yeYunOrderParam));
			ModelResult<YeYunOrderInfo> yeYunOrderInfoModelResult = yeYunOrderServiceClient.addUserScore(yeYunOrderParam,userOperationParam);
			YeYunOrderInfo orderInfoResult = yeYunOrderInfoModelResult.getModel();
			//兑换失败
			if (orderInfoResult == null){
				logger.info("{}兑换椰云积分，{}，兑换失败：{}",logPrefix,userConsumer.getNickName(),yeYunOrderInfoModelResult.getErrorMsg());
				return CommonResponse.withErrorResp(yeYunOrderInfoModelResult.getErrorMsg());
			}
			//兑换成功，返回椰云用户信息
			YeYunOrderInfoResponse orderInfoResponse = new YeYunOrderInfoResponse();
			orderInfoResponse.setDate(orderInfoResult.getDate());
			orderInfoResponse.setTotal(orderInfoResult.getUserTotal());
			logger.info("{}兑换椰云积分，{}，兑换成功orderInfoResponse：{}",logPrefix,userConsumer.getNickName(),JSON.toJSONString(orderInfoResponse));
			return CommonResponse.withSuccessResp(JSON.toJSONString(orderInfoResponse));
		}catch (Exception e){
			logger.error("{}兑换椰云积分异常，参数userId：{}",logPrefix,userId,e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	/*@RequestMapping(value = "/checkYeYunOrder", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "验证订单", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "验证订单接口", response = CommonResponse.class)
	public CommonResponse<Boolean> checkYeYunOrder(YeYunOrderRequest yeYunOrderRequest, HttpServletRequest request){
		CommonResponse<Boolean> commonResponse = new CommonResponse<>();
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			YeYunOrderParam yeYunOrderParam = new YeYunOrderParam();
			yeYunOrderParam.setTradeNo(yeYunOrderRequest.getTradeNo());
			yeYunOrderParam.setUniqueUserId(yeYunOrderRequest.getUserId());
			ModelResult<Boolean> modelResult = yeYunOrderServiceClient.checkOrder(yeYunOrderParam);
			if (!modelResult.isSuccess() || modelResult.getModel() == null){
				logger.info("{}验证订单错误，参数：{}，errorMsg：{}",logPrefix, JSON.toJSONString(yeYunOrderParam),modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("椰云订单验证异常");
			}
			commonResponse.setData(modelResult.getModel());
		}catch (Exception e){
			logger.error("{}增加用户积分异常，参数userId：{}",logPrefix,yeYunOrderRequest.getUserId(),e);
			CommonResponse.withErrorResp("增加用户积分");
		}
		return commonResponse;
	}*/

	//暂无用
	@RequestMapping(value = "/listYeYunOrder", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "椰云用户积分订单记录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "椰云用户积分订单记录接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<YeYunOrderInfoResponse>> listYeYunOrder(YeYunOrderRequest yeYunOrderRequest, HttpServletRequest request, DataPage dataPage){
		CommonResponse<List<YeYunOrderInfoResponse>> commonResponse = new CommonResponse<>();
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}获取积分订单记录，未获取到登录信息",logPrefix);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			if (yeYunOrderRequest.getStartDate() == null){
				yeYunOrderRequest.setStartDate(DateUtil.getTodayStartTime());
			}
			if (yeYunOrderRequest.getEndDate() == null){
				yeYunOrderRequest.setEndDate(DateUtil.getTodayEndTime());
			}
			logger.info("param：{}",JSON.toJSONString(yeYunOrderRequest));
			YeYunOrderParam yeYunOrderParam = new YeYunOrderParam();
			yeYunOrderParam.setUniqueUserId(userConsumer.getId());
			yeYunOrderParam.setDateFrom(yeYunOrderRequest.getStartDate());
			yeYunOrderParam.setDateTo(yeYunOrderRequest.getEndDate());
			logger.info("service-param：{}",JSON.toJSONString(yeYunOrderParam));
			PageResult<YeYunOrderInfo> pageResult = yeYunOrderServiceClient.listSearchOrders(yeYunOrderParam, dataPage);
			if (!pageResult.isSuccess() || pageResult.getPage() == null) {
				logger.info("{}用户积分订单记录接口错误，参数：{}，errorMsg：{}", logPrefix, JSON.toJSONString(yeYunOrderParam),pageResult.getErrorMsg());
				return CommonResponse.withErrorResp("获取token和url失败");
			}
			List<YeYunOrderInfoResponse> listOrderInfo = new ArrayList<>();
			List<YeYunOrderInfo> dataList = pageResult.getPage().getDataList();
			for (YeYunOrderInfo yeYunOrderInfo : dataList) {
				YeYunOrderInfoResponse orderInfo = new YeYunOrderInfoResponse();
				BeanUtils.copyProperties(yeYunOrderInfo,orderInfo,"userId","desc");
				listOrderInfo.add(orderInfo);
			}
			return commonResponse.withSuccessResp(listOrderInfo);
		}catch (Exception e){
			logger.error("{}获取token和url异常，参数request：{}",logPrefix,JSON.toJSONString(yeYunOrderRequest),e);
			return CommonResponse.withErrorResp("获取token和url异常");
		}
	}

}
