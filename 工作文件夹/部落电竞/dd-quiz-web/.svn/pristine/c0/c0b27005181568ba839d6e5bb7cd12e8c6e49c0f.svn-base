package com.esportzoo.esport.manager.quiz;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.connect.request.quiz.QuizSubmitBetRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizSubmitBetResponse;
import com.esportzoo.esport.constants.EsportPayway;
import com.esportzoo.esport.constants.quiz.QuizAcceptBetterSpType;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.service.quiz.QuizOrderService;
import com.esportzoo.esport.vo.quiz.SubmitOrderParam;

/**
 * 竞猜投注相关:主要处理竞猜投注和投注记录
 * @author jing.wu
 * @version 创建时间：2019年10月24日 下午4:12:58
 */
@Component
public class QuizBetManager {
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String logPrefix = "竞猜投注相关";
	@Autowired
	private QuizOrderService quizOrderService;

	public CommonResponse<QuizSubmitBetResponse> doBet(HttpServletRequest request, QuizSubmitBetRequest quizDoBetRequest, UserConsumer userConsumer) {
		logger.info(logPrefix + "用户id={},立即竞猜接口参数:{}", userConsumer.getId(), JSONObject.toJSONString(quizDoBetRequest));
		try {
			// 校验请求参数
			if (null == quizDoBetRequest || StringUtils.isBlank(quizDoBetRequest.getMatchNo())
					|| null == quizDoBetRequest.getPlayNo()
					|| null == quizDoBetRequest.getBetSp() || null == quizDoBetRequest.getUserBetNum()) {
				logger.info(logPrefix + "用户id={},必要参数为空,传入接口参数:{}", userConsumer.getId(), JSONObject.toJSONString(quizDoBetRequest));
				return CommonResponse.withErrorResp("必要参数为空");
			}

			SubmitOrderParam submitOrderParam = new SubmitOrderParam();
			submitOrderParam.setUserId(userConsumer.getId());
			submitOrderParam.setMatchGameId(quizDoBetRequest.getMatchGameId());
			submitOrderParam.setMatchNo(quizDoBetRequest.getMatchNo());
			submitOrderParam.setPlayNo(quizDoBetRequest.getPlayNo());
			submitOrderParam.setAmount(quizDoBetRequest.getUserBetNum());
			submitOrderParam.setOption(quizDoBetRequest.getOptIndex());
			submitOrderParam.setOdds(quizDoBetRequest.getBetSp());
			submitOrderParam.setPayType(EsportPayway.REC_PAY.getIndex());
			submitOrderParam.setBizSystem(quizDoBetRequest.getBiz());
			submitOrderParam.setClientType(quizDoBetRequest.getClientType());
			submitOrderParam.setChannelNo(quizDoBetRequest.getAgentId().intValue());
			submitOrderParam.setAcceptBetterSp(QuizAcceptBetterSpType.TRUE.getIndex());

			ModelResult<Long> modelResult = quizOrderService.submitOrder(submitOrderParam);
			logger.info(logPrefix + "用户id={},立即竞猜接口参数:{},调用submitOrder接口返回值:{}", userConsumer.getId(), JSONObject.toJSONString(quizDoBetRequest), JSONObject.toJSONString(modelResult));
			if (null == modelResult) {
				return CommonResponse.withErrorResp("调用接口异常");
			}
			if (!modelResult.isSuccess()) {
				return CommonResponse.withResp(modelResult.getErrorCode(),modelResult.getErrorMsg());
			}
			QuizSubmitBetResponse response = new QuizSubmitBetResponse();
			response.setBetOrderId(modelResult.getModel());
			return CommonResponse.withSuccessResp(response);
		} catch (Exception e) {
			logger.error(logPrefix + "立即竞猜接口异常, 参数【{}】 异常【{}】", JSONObject.toJSONString(quizDoBetRequest), e.getMessage(), e);
			return CommonResponse.withErrorResp("投注异常");
		}
	}
}
