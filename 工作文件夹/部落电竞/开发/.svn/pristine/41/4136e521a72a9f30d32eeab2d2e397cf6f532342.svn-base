package com.esportzoo.esport.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.consumer.UserThirdLoginServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizOrderServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizPlanServiceClient;
import com.esportzoo.esport.connect.response.UserInfoResponseVo;
import com.esportzoo.esport.connect.response.quiz.QuizUserInfoResponse;
import com.esportzoo.esport.constants.FeatureKey;
import com.esportzoo.esport.constants.ThirdType;
import com.esportzoo.esport.constants.quiz.QuizOrderStatus;
import com.esportzoo.esport.constants.quiz.QuizPlanWinStatus;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserThirdLogin;
import com.esportzoo.esport.domain.quiz.QuizOrder;
import com.esportzoo.esport.domain.quiz.QuizPlan;
import com.esportzoo.esport.vo.quiz.QuizOrderQueryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tingting.shen
 * @date 2019/06/10
 */
@Component
public class UserManager {
	
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;
	@Autowired
	private UserThirdLoginServiceClient userThirdLoginServiceClient;

	@Autowired
	private QuizOrderServiceClient quizOrderServiceClient;
	@Autowired
	private QuizPlanServiceClient quizPlanServiceClient;


	public QuizUserInfoResponse queryWinPrizeByLoginTime(Long userId, Integer channel){
		String prefix="查询用户当前时间[{}]与上一次登录时间[{}]之间中奖信息_";
		Date time= DateUtil.getCurrentDayFirstTime().getTime();
		QuizUserInfoResponse quizUserInfoResponse = new QuizUserInfoResponse();
		quizUserInfoResponse.setExistWinPrize(Boolean.FALSE);
		try {
            ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient.queryConsumerById(userId, null);
            if (!userConsumerModelResult.isSuccess()){
                logger.info(prefix+"获取用户信息失败[{}]", userConsumerModelResult.getErrorMsg());
            }
            UserConsumer userConsumer = userConsumerModelResult.getModel();
			Long loginTime = userConsumer.getFeature(FeatureKey.QUIZ_LAST_LOGIN_TIME,Long.class);
			if (null!=loginTime){
				time=new Date(loginTime);
			}
			prefix= StrUtil.format(prefix, DateUtil.getTime(),DateUtil.formatDate(time));
			logger.info(prefix + "用户id【{}】", userConsumer.getId());
			QuizOrderQueryVo param = new QuizOrderQueryVo();
			param.setUserId(userConsumer.getId());
			param.setChannelNo(channel);
			param.setStartOpenAwardTime(time);
			ArrayList<Integer> al = new ArrayList();
			al.add(QuizOrderStatus.WIN.getIndex());
			al.add(QuizOrderStatus.AWARDED.getIndex());
			al.add(QuizOrderStatus.NEW_AWARDED.getIndex());
			param.setStatuss(al);
			ModelResult<List<QuizOrder>> listModelResult = quizOrderServiceClient.queryList(param);
			List<QuizOrder> model = listModelResult.getModel();
			if (!listModelResult.isSuccess() || null==model){
				logger.info(prefix+"订单异常[{}] param[userId:{},channel:{}]", listModelResult.getErrorMsg(),userConsumer.getId(),channel);
				return quizUserInfoResponse;
			}
			if (CollectionUtil.isNotEmpty(model)){
				List<Long> ids = model.stream().map(QuizOrder::getId).collect(Collectors.toList());
				ModelResult<List<QuizPlan>> listModelResult1 = quizPlanServiceClient.queryByOrderIds(ids);
				List<QuizPlan> plans = listModelResult1.getModel();
				if (!listModelResult1.isSuccess() || null==plans){
					logger.info(prefix+"方案异常[{}] param[userId:{},channel:{}]", listModelResult.getErrorMsg(),userConsumer.getId(),channel);
					return quizUserInfoResponse;
				}
				if (CollectionUtil.isNotEmpty(plans)){
					BigDecimal bigDecimal = plans.stream().filter(a -> a.getWinStatus().intValue()== QuizPlanWinStatus.WIN.getIndex()).map(QuizPlan::getPrize).reduce(BigDecimal::add).get();
					quizUserInfoResponse.setExistWinPrize(Boolean.TRUE);
					quizUserInfoResponse.setWinPrize(bigDecimal);
				}
			}
			userConsumer.setupFeature(FeatureKey.QUIZ_LAST_LOGIN_TIME, new Date());
			userConsumerServiceClient.updateConsumerFeatures(userConsumer.getFeatures(), null,userConsumer.getId());
		} catch (Exception e) {
			logger.info(prefix+"发生异常[{}]", e.getMessage(),e);
		}
		return quizUserInfoResponse;
	}

	
	public UserThirdLogin getBind(Long userid, ThirdType thirdType) {
		UserThirdLogin result = null;
		ModelResult<List<UserThirdLogin>> modelResult = userThirdLoginServiceClient.queryThirdLoginByUserId(userid);
		if (modelResult.isSuccess() && modelResult.getModel()!=null && modelResult.getModel().size()>0) {
			List<UserThirdLogin> list = modelResult.getModel();
			for (UserThirdLogin userThirdLogin : list) {
				if (userThirdLogin.getThirdType().intValue() == thirdType.getIndex()) {
					result = userThirdLogin;
					break;
				}
			}
		}
		return result;
	}
	
	public UserThirdLogin getUserThirdLogin(String thirdId, Integer thirdType) {
		String logPrefix = "根据第三方id和第三方类型查询绑定关系_";
		try {
			ModelResult<UserThirdLogin> modelResult = userThirdLoginServiceClient.queryByThirdIdAndThirdType(thirdId, thirdType);
			if (!modelResult.isSuccess()) {
				logger.info(logPrefix + "接口返回错误，errorMsg={}", modelResult.getErrorMsg());
				return null;
			}
			return modelResult.getModel();
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常，thirdId={}， thirdType={}， exception={}", thirdId, thirdType, e.getMessage(), e);
			return null;
		}
	}
	
	public UserConsumer getUserConsumer(String thirdId, Integer thirdType) {
		String logPrefix = "根据第三方id和第三方类型查询用户_";
		try {
			ModelResult<Long> modelResult = userThirdLoginServiceClient.queryConsumerId(thirdId, thirdType);
			if (!modelResult.isSuccess()) {
				return null;
			}
			Long userConsumerId = modelResult.getModel();
			if (userConsumerId == null) {
				return null;
			}
			ModelResult<UserConsumer> modelResult1 = userConsumerServiceClient.queryConsumerById(userConsumerId, null);
			if (!modelResult1.isSuccess()) {
				return null;
			}
			return modelResult1.getModel();
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常，thirdId={}， thirdType={}， exception={}", thirdId, thirdType, e.getMessage(), e);
			return null;
		}
	}

	public UserInfoResponseVo getUserInfoByDisplay(UserConsumer userConsumer){
		UserInfoResponseVo userInfoResponseVo = new UserInfoResponseVo();
		userInfoResponseVo.setUserId(userConsumer.getId());
		userInfoResponseVo.setIcon(userConsumer.getIcon());
		userInfoResponseVo.setNickName(userConsumer.getNickName());
		userInfoResponseVo.setPhone(userConsumer.getPhone());
		return userInfoResponseVo;
	}
	
	
}
