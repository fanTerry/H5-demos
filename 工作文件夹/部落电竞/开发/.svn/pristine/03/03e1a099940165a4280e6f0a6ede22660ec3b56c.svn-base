package com.esportzoo.esport.controller.expert;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.expert.RecOrderServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.expert.ArticlePayRequestParam;
import com.esportzoo.esport.connect.request.expert.ArticlePaySubmitRequestParam;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.expert.ArticleDetailResponse;
import com.esportzoo.esport.connect.response.expert.RecOrderResponse;
import com.esportzoo.esport.connect.response.payment.PayOrderResponse;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.constants.cms.expert.ArticleChoosePayWay;
import com.esportzoo.esport.constants.cms.expert.PayWay;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.RecExpertColumnArticle;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserWalletRec;
import com.esportzoo.esport.manager.UserManager;
import com.esportzoo.esport.manager.UserWalletManager;
import com.esportzoo.esport.manager.expert.ExpertArticleManager;
import com.esportzoo.esport.service.exception.errorcode.RecOrderErrorTable;
import com.esportzoo.esport.service.exception.errorcode.WalletErrorTable;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.ChargePayRequest;
import com.esportzoo.esport.vo.expert.ExpertArticlePayParam;
import com.esportzoo.esport.vo.expert.ExpertArticlePayParamV2;
import com.esportzoo.esport.vo.expert.ExpertArticlePayResult;
import com.esportzoo.esport.vo.partner.UboxBalanceVo;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 专家文章相关Controller
 * @author: jing.wu
 * @date:2019年5月11日下午2:12:15
 */
@Controller
@RequestMapping("article")
@Api(value = "专家文章推荐相关接口", tags = { "专家文章推荐相关接口" })
public class ExpertArticleController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ExpertArticleManager expertArticleManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserWalletManager userWalletManager;
	@Autowired
	@Qualifier("recOrderServiceClient")
	private RecOrderServiceClient recOrderServiceClient;
	

	/**专家文章支付接口,目前小程序在用,后面需要统一走/commonPay/{articleId}接口*/
	@RequestMapping(value = "/pay/{articleId}", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "推荐文章支付接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "推荐文章支付接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<RecOrderResponse> toPayArticle(@ApiParam(required=true, name="文章id")@PathVariable("articleId") Long articleId, 
			ArticlePayRequestParam payParam, HttpServletRequest request) {
		logger.info("根据文章id支付接口,接收到的参数articleId={},agentId={},choosedPayWay={}", articleId, payParam.getAgentId(), payParam.getChoosedPayWay());
		int choosedPayWay = payParam.getChoosedPayWay();
		int agentId = payParam.getAgentId();
		try {
			if (!ArticleChoosePayWay.getAllIndex().contains(choosedPayWay)) {
				return CommonResponse.withErrorResp("选择的支付方式不对");
			}
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据文章id支付接口,未获取到登录用户信息，articleId={}", articleId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			UserOperationParam userOperationParam = new UserOperationParam();
			userOperationParam.setSellChannel(new Long(agentId));
			userOperationParam.setClientType(ClientType.WXXCY.getIndex());
			userOperationParam.setOperIp(RequestUtil.getClientIp(request));
			userOperationParam.setOpRemark("付费阅读文章");
			ExpertArticlePayParam param = new ExpertArticlePayParam(articleId, userConsumer.getId());
			if (choosedPayWay == ArticleChoosePayWay.UBOX_WALLET.getIndex()) {
				param.setPayModel(ArticlePayModel.PAY_DIRECT.getIndex());
				param.setChannelIndex(ChannelProxy.UBOX_WALLET.getIndex());
				param.setPayWayIndex(EsportPayway.UBOX_PAY.getIndex());
			} else {
				param.setPayModel(ArticlePayModel.PAY_WALLET.getIndex());
			}
			ModelResult<ExpertArticlePayResult> modelResult = expertArticleManager.toPay(param, userOperationParam);
			PayResultStatus payResultStatus = PayResultStatus.PAY_SUCCESS;
			if (!modelResult.isSuccess()){
				if(WalletErrorTable.REC_NOT_SUFFICIENT_FUNDS.getCode().equals(modelResult.getErrorCode())){
					//余额不足返回 payResultStatus
					payResultStatus = PayResultStatus.INSUFFICIENT_BALANCE;
				}else if(RecOrderErrorTable.REC_PAY_EXCEPTION.getCode().equals(modelResult.getErrorCode())){
					//支付异常返回错误信息
					payResultStatus = PayResultStatus.PAYMENT_EXCEPTION;
					return CommonResponse.withErrorResp(modelResult.getErrorMsg());
				}else {
					//支付失败返回错误信息
					payResultStatus = PayResultStatus.PAY_FAIL;
					return CommonResponse.withErrorResp(modelResult.getErrorMsg());
				}
			}
			return CommonResponse.withSuccessResp(new RecOrderResponse(payResultStatus.getIndex()));
		} catch (Exception e) {
			logger.info("根据文章id支付接口,发生异常，articleId={}，agentId={}, exception={}", articleId, agentId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	/**新版专家文章支付接口,目前H5在用*/
	@RequestMapping(value = "/commonPay/{articleId}", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "新版推荐文章支付接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "新版推荐文章支付接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<RecOrderResponse> toCommonPayArticle(@ApiParam(required=true, name="文章id")@PathVariable("articleId") Long articleId, 
			ArticlePayRequestParam payParam, HttpServletRequest request) {
		logger.info("根据文章id支付接口,接收到的参数articleId={},agentId={},choosedPayWay={}", articleId, payParam.getAgentId(), payParam.getChoosedPayWay());
		int choosedPayWay = payParam.getChoosedPayWay();
		int agentId = payParam.getAgentId();
		try {
			if (!PayWay.getAllIndex().contains(choosedPayWay)) {
				return CommonResponse.withErrorResp("选择的支付方式不对");
			}
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据文章id支付接口,未获取到登录用户信息，articleId={}", articleId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			UserOperationParam userOperationParam = new UserOperationParam();
			userOperationParam.setSellChannel(new Long(agentId));
			userOperationParam.setClientType(ClientType.H5.getIndex());
			userOperationParam.setOperIp(RequestUtil.getClientIp(request));
			userOperationParam.setOpRemark("付费阅读文章");
			ExpertArticlePayParam param = new ExpertArticlePayParam(articleId, userConsumer.getId());
			if (choosedPayWay == PayWay.UBOX_WALLET.getIndex()) {
				param.setPayModel(ArticlePayModel.PAY_DIRECT.getIndex());
				param.setChannelIndex(ChannelProxy.UBOX_WALLET.getIndex());
				param.setPayWayIndex(EsportPayway.UBOX_PAY.getIndex());
			} else {
				param.setPayModel(ArticlePayModel.PAY_WALLET.getIndex());
			}
			ModelResult<ExpertArticlePayResult> modelResult = expertArticleManager.toPay(param, userOperationParam);
			PayResultStatus payResultStatus = PayResultStatus.PAY_SUCCESS;
			if (!modelResult.isSuccess()){
				if(WalletErrorTable.REC_NOT_SUFFICIENT_FUNDS.getCode().equals(modelResult.getErrorCode())){
					//余额不足返回 payResultStatus
					payResultStatus = PayResultStatus.INSUFFICIENT_BALANCE;
				}else if(RecOrderErrorTable.REC_PAY_EXCEPTION.getCode().equals(modelResult.getErrorCode())){
					//支付异常返回错误信息
					payResultStatus = PayResultStatus.PAYMENT_EXCEPTION;
					return CommonResponse.withErrorResp(modelResult.getErrorMsg());
				}else {
					//支付失败返回错误信息
					payResultStatus = PayResultStatus.PAY_FAIL;
					return CommonResponse.withErrorResp(modelResult.getErrorMsg());
				}
			}
			return CommonResponse.withSuccessResp(new RecOrderResponse(payResultStatus.getIndex()));
		} catch (Exception e) {
			logger.info("根据文章id支付接口,发生异常，articleId={}，agentId={}, exception={}", articleId, agentId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/detail/{articleId}", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询文章详情接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询文章详情接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ArticleDetailResponse> getDetail(@ApiParam(required=true, name="文章id") @PathVariable("articleId") Long articleId, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("查询文章详情接口，未获取到登录用户信息，articleId={}", articleId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			RecExpertColumnArticle article = expertArticleManager.getByArticleId(articleId);
			if (article == null) {
				logger.info("查询文章详情接口，未获取到文章信息，articleId={}", articleId);
				return CommonResponse.withErrorResp("未获取到文章信息");
			}
			boolean isUserPay = false;
			if (article.getIsFree().intValue() == ArticleFreeType.FREE.getIndex()) {
				cachedManager.cacheSet(CacheType.EPERT_ARTICEL.getIndex(),article.getId()+"",userConsumer.getId()+"");
//				article.setViews(cachedManager.getCacheSetSize(CacheType.EPERT_ARTICEL.getIndex(),article.getId()+"").intValue());
				isUserPay = true;
			} else if (article.getUserId().longValue() == userConsumer.getId().longValue()) {
				isUserPay = true;
			} else if (article.getPrice().compareTo(new BigDecimal(0)) <= 0) {
				isUserPay = true;
			}
			else if (expertArticleManager.isPay(userConsumer.getId(), articleId)){
				isUserPay = true;
			}
			return CommonResponse.withSuccessResp(expertArticleManager.converToArticleDetail(article,isUserPay));
		} catch (Exception e) {
			logger.info("查询文章详情接口，发生异常，articleId={}，exception={}", articleId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/detail/new/{articleId}", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "新的查询文章详情接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "新的查询文章详情接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ArticleDetailResponse> getDetailNew(@ApiParam(required=true, name="文章id") @PathVariable("articleId") Long articleId, HttpServletRequest request) {
		try {
			RecExpertColumnArticle article = expertArticleManager.getByArticleId(articleId);
			if (article == null) {
				logger.info("查询文章详情接口，未获取到文章信息，articleId={}", articleId);
				return CommonResponse.withErrorResp("未获取到文章信息");
			}
			if ((article.getIsFree().intValue() == ArticleFreeType.FREE.getIndex())
					|| (article.getPrice().compareTo(new BigDecimal(0)) <= 0)) {
				return CommonResponse.withSuccessResp(expertArticleManager.converToArticleDetail(article,true));
			} else {
				UserConsumer userConsumer = getLoginUsr(request);
				if (userConsumer == null) {
					return CommonResponse.withResp("555", "用户未登录");
				}
				if (article.getUserId().longValue() == userConsumer.getId().longValue()) {
					return CommonResponse.withSuccessResp(expertArticleManager.converToArticleDetail(article,true));
				}
				if (expertArticleManager.isPay(userConsumer.getId(), articleId)) {
					return CommonResponse.withSuccessResp(expertArticleManager.converToArticleDetail(article,true));
				} else {
					return CommonResponse.withSuccessResp(expertArticleManager.converToArticleDetail(article,false));
				}
			}
		} catch (Exception e) {
			logger.info("新的查询文章详情接口，发生异常，articleId={}，exception={}", articleId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/choosePayWay", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询用户可以选择的支付方式", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询用户可以选择的支付方式", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<ArticleChoosePayWay>> choosePayWay(@ApiParam(required=true, name="文章id")Long articleId, HttpServletRequest request, BaseRequest baseRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("查询用户可以选择的支付方式接口，未获取到登录用户信息，articleId={}", articleId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			logger.info("查询用户可以选择的支付方式接口，获取到登录用户Id={},昵称={}，bizsystem={}", userConsumer.getId(), userConsumer.getNickName(), userConsumer.getBizSystem());
			RecExpertColumnArticle article = expertArticleManager.getByArticleId(articleId);
			if (article == null) {
				logger.info("查询用户可以选择的支付方式接口，未获取到文章信息，articleId={}", articleId);
				return CommonResponse.withErrorResp("未获取到文章信息");
			}
			BigDecimal price = new BigDecimal(0);
			if (article.getIsFree().intValue() != ArticleFreeType.FREE.getIndex()
					&& article.getUserId().longValue() != userConsumer.getId().longValue()
					&& article.getPrice().compareTo(new BigDecimal(0)) > 0) {
				price = article.getPrice();
			}
			List<ArticleChoosePayWay> resultList = new ArrayList<>();
			Long userId = userConsumer.getId();
			if (userManager.getBind(userConsumer.getId(), ThirdType.UBOX) != null) {
				SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.PAYMENT_CHANNEL_SWITCH_UBOX, baseRequest.getClientType(),baseRequest.getAgentId());
				if (sysConfigProperty != null) {
					String value = sysConfigProperty.getValue();
					if (StringUtils.isNotBlank(value) && value.trim().equals("1")) {
						ArticleChoosePayWay payWay2 = ArticleChoosePayWay.UBOX_WALLET;
						payWay2.setAmount(price.toString());
						UboxBalanceVo vo = userWalletManager.getUboxBalance(userId);
						if (null != vo) {
							//payWay2.setBalance(vo.getBalance() / 100 + "");
							DecimalFormat df = new DecimalFormat("#0.00");
							BigDecimal bd = new BigDecimal(vo.getBalance()).divide(new BigDecimal(100));
							String s = df.format(bd);
							payWay2.setBalance(s);
						}
						resultList.add(payWay2);
					}
				}
			}
			ArticleChoosePayWay payWay1 = ArticleChoosePayWay.ESPORT_WALLET;
			payWay1.setAmount(price.toString());
			UserWalletRec userWalletRec = userWalletManager.getUserWalletRec(userId);
			if(null!=userWalletRec){
				payWay1.setBalance(userWalletRec.getAbleRecScore().toString());
			}
			resultList.add(payWay1);
			return CommonResponse.withSuccessResp(resultList);
		} catch (Exception e) {
			logger.info("查询用户可以选择的支付方式，发生异常，articleId={}，exception={}", articleId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getPayWayList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询用户可以选择的支付方式", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询用户可以选择的支付方式", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<PayWay>> getPayWayList(HttpServletRequest request, BaseRequest baseRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("查询用户可以选择的支付方式接口，未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			/*Boolean isWeiXinEnv = false;
			String userAgent = request.getHeader("user-agent");
			if (StringUtils.isNotBlank(userAgent) && userAgent.contains("MicroMessenger")) {
				isWeiXinEnv = true;
			}*/
			logger.info("查询用户可以选择的支付方式接口，获取到登录用户Id={},昵称={}，bizsystem={}", userConsumer.getId(), userConsumer.getNickName(), userConsumer.getBizSystem());
			List<PayWay> resultList = new ArrayList<>();
			Long userId = userConsumer.getId();
			// 友宝余额支付方式
			if (userManager.getBind(userConsumer.getId(), ThirdType.UBOX) != null) {
				SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.PAYMENT_CHANNEL_SWITCH_UBOX, baseRequest.getClientType(),baseRequest.getAgentId());
				if (sysConfigProperty != null) {
					String value = sysConfigProperty.getValue();
					if (StringUtils.isNotBlank(value) && value.trim().equals("1")) {
						PayWay uboxPay = PayWay.UBOX_WALLET;
						UboxBalanceVo vo = userWalletManager.getUboxBalance(userId);
						if (null != vo) {
							BigDecimal bd = new BigDecimal(vo.getBalance()).divide(new BigDecimal(100));
							double s = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
							uboxPay.setBalance(s);
						}
						resultList.add(uboxPay);
					}
				}
			}
			// 不是微信体系,支持H5支付
			/*if (!isWeiXinEnv) {
				PayWay h5Pay = PayWay.WXH5_PAY;
				resultList.add(h5Pay);
			}*/
			// 星星支付方式,默认都有
			PayWay starPay = PayWay.ESPORT_WALLET;
			UserWalletRec userWalletRec = userWalletManager.getUserWalletRec(userId);
			if (null != userWalletRec) {
				starPay.setBalance(userWalletRec.getAbleRecScore().doubleValue());
			}
			resultList.add(starPay);
			return CommonResponse.withSuccessResp(resultList);
		} catch (Exception e) {
			logger.info("查询用户可以选择的支付方式，发生异常，exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	
	/** 新版公用专家文章支付接口0819,支持小程序和H5 */
	@RequestMapping(value = "/payArticle/{articleId}", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "新版推荐文章支付接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "新版推荐文章支付接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<PayOrderResponse> payArticle(@ApiParam(required = true, name = "文章id") @PathVariable("articleId") Long articleId,
			ArticlePaySubmitRequestParam payParam, HttpServletRequest request) {
		logger.info("根据文章id支付接口,接收到的参数articleId={},agentId={},choosedPayWay={}", articleId, payParam.getAgentId(), payParam.getChoosedPayWay());
		Integer choosedPayWay = payParam.getChoosedPayWay();
		PayOrderResponse resp = new PayOrderResponse();
		try {
			if (null == choosedPayWay) {
				return CommonResponse.withErrorResp("请选择支付方式");
			}
			if (null == EsportPayway.valueOf(choosedPayWay)) {
				return CommonResponse.withErrorResp("选择的支付方式不对");
			}
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据文章id支付接口,未获取到登录用户信息，articleId={}", articleId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}

			UserOperationParam userOperationParam = new UserOperationParam();
			userOperationParam.setChannelNo(payParam.getAgentId());
			userOperationParam.setClientType(payParam.getClientType());
			userOperationParam.setOperIp(RequestUtil.getClientIp(request));
			userOperationParam.setOpRemark(ClientType.valueOf(payParam.getClientType()).getDescription() + "付费阅读文章");

			ExpertArticlePayParamV2 param = new ExpertArticlePayParamV2(articleId, userConsumer.getId());
			param.setPayWayIndex(choosedPayWay);
			ModelResult<ChargePayRequest> modelResult = recOrderServiceClient.payArticleV2(param, userOperationParam);
			if (modelResult == null || !modelResult.isSuccess() || null == modelResult.getModel()) {
				logger.info("根据文章id支付接口,调用文章支付接口异常,articleId={},用户id:{},modelResult={}", articleId, userConsumer.getId(), JSONObject.toJSONString(modelResult));
				return CommonResponse.withErrorResp("服务繁忙,请稍后再试~");
			}
			BeanUtils.copyProperties(modelResult.getModel(), resp);
			resp.setSuccessFlag(true);
			resp.setChargeWay(choosedPayWay);
			return CommonResponse.withSuccessResp(resp);
		} catch (Exception e) {
			logger.info("根据文章id支付接口,发生异常，articleId={}，agentId={}, exception={}", articleId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("#0.00");
		BigDecimal bd = new BigDecimal(0).divide(new BigDecimal(100));
		String s = df.format(bd);
		System.out.println(s);
	}

}
