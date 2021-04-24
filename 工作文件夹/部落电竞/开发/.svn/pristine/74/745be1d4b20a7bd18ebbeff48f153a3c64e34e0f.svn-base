package com.esportzoo.esport.controller.quiz;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.esport.connect.request.quiz.QuizSubmitBetRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizSubmitBetResponse;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.quiz.QuizBetManager;

/**
 * 竞猜投注相关:主要处理竞猜投注和投注记录
 * 
 * @author jing.wu
 * @version 创建时间：2019年10月21日 下午6:30:08
 */
@Controller
@RequestMapping("/quiz/bet")
public class QuizBetController extends BaseController {
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String logPrefix = "竞猜投注相关";
	@Autowired
	private QuizBetManager quizBetManager;

	/** 点击立即竞猜逻辑 */
	@RequestMapping(value = "/doSubmit", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "立即竞猜", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "立即竞猜接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<QuizSubmitBetResponse> doBet(HttpServletRequest request, QuizSubmitBetRequest quizDoBetRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			return quizBetManager.doBet(request, quizDoBetRequest, userConsumer);
		} catch (Exception e) {
			logger.error(logPrefix + "立即竞猜接口异常, 参数【{}】 异常【{}】", JSONObject.toJSONString(quizDoBetRequest), e.getMessage(), e);
			return CommonResponse.withErrorResp("投注异常");
		}
	}
}
