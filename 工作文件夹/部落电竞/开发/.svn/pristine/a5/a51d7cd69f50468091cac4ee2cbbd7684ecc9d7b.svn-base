package com.esportzoo.esport.manager;

import cn.hutool.core.bean.BeanUtil;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.cms.CmsCommentServiceClient;
import com.esportzoo.esport.client.service.cms.CmsContentServiceClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertApplyServiceClient;
import com.esportzoo.esport.constants.AuditStatus;
import com.esportzoo.esport.constants.user.UserMsgCacheConstant;
import com.esportzoo.esport.domain.RecExpertApply;
import com.esportzoo.esport.domain.UserCenterInfoVo;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-20 09:32
 **/
@Component
public class UserCenterManager {


	private transient static final Logger logger = LoggerFactory.getLogger(UserCenterManager.class);


	@Autowired
	@Qualifier("userConsumerServiceClient")
	private UserConsumerServiceClient userConsumerServiceClient;

	@Autowired
	@Qualifier("cmsContentServiceClient")
	private CmsContentServiceClient cmsContentServiceClient;

	@Autowired
	@Qualifier("cmsCommentServiceClient")
	private CmsCommentServiceClient cmsCommentServiceClient;

	@Autowired
	@Qualifier("recExpertApplyServiceClient")
	private RecExpertApplyServiceClient recExpertApplyServiceClient;

	@Autowired
	@Qualifier("redisClusterClient")
	RedisClient jedisClusterClient;

	public static final String USER_UP_SUM = "user_up_sum_";

	public static final String MESSAGE_NUM = "user_message_num_";

	public UserCenterInfoVo getUserInfo(Long userId) {

		UserCenterInfoVo centerInfoVo = new UserCenterInfoVo();
		/*初始化数据*/
		centerInfoVo.setUpSum("0");
		centerInfoVo.setFans(0);
		centerInfoVo.setFollowers(0);
		try {
			UserConsumerQueryOption queryOption = new UserConsumerQueryOption();
			ModelResult<UserConsumer> modelResult = userConsumerServiceClient.queryConsumerById(userId, queryOption);
			if (!modelResult.isSuccess()) {
				logger.error("查询用户=「{}」出现异常,异常信息：{}", userId, modelResult.getErrorMsg());
				return centerInfoVo;
			}
			UserConsumer userConsumer = modelResult.getModel();
			/*String phone = userConsumer.getPhone();
			String certNo = userConsumer.getCertNo();
			if (StrUtil.isNotBlank(phone)){
				*//*userConsumer.setPhone(DesUtil.decryptStr(phone).replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));*//*
				userConsumer.setPhone(DesUtil.decryptStr(phone));
			}
			if (StrUtil.isNotBlank(certNo)){
				*//*userConsumer.setCertNo(DesUtil.decryptStr(certNo).replaceAll("(\\d{4})\\d{10}(\\d{4})","$1****$2"));*//*
				userConsumer.setCertNo(DesUtil.decryptStr(certNo));
			}*/
			BeanUtil.copyProperties(userConsumer, centerInfoVo);
			centerInfoVo.setUserId(userConsumer.getId());

			/*是否提交专家申请*/
			ModelResult<List<RecExpertApply>> expertApplyResult = recExpertApplyServiceClient.queryByUserId(userId);
			if (expertApplyResult.isSuccess() && expertApplyResult.getModel() != null && expertApplyResult.getModel().size() > 0) {
				/*审核中、审核通过，不显示入口*/
				RecExpertApply apply = expertApplyResult.getModel().get(0);
				if (apply.getStatus() == AuditStatus.AUDITING.getIndex() || apply.getStatus() == AuditStatus.AUDIT_SECC.getIndex()) {
					centerInfoVo.setHasExpertApply(false);
				}
			}

			/*统计用户点赞总数*/
			String upSum = jedisClusterClient.get(USER_UP_SUM + userConsumer.getId());

			if (StringUtils.isEmpty(upSum)) {
				int sum = 0;
			/*	ModelResult<Long> sumUpsResult = userConsumerServiceClient.querySumUpsByUserIdAndTime(userId, null, null);*/
				ModelResult<Integer> sumUpsResult = cmsContentServiceClient.queryCountByUserUp(userId);
				if (!sumUpsResult.isSuccess()) {
					logger.error("查询用户=「{}」点赞数出现异常,异常信息：{}", userId, sumUpsResult.getErrorMsg());
				}
				sum = sumUpsResult.getModel();
				upSum = String.valueOf(sum);
				jedisClusterClient.set(USER_UP_SUM + userConsumer.getId(), upSum, 30);
			}
			centerInfoVo.setUpSum(upSum);

			/** 设置未读消息数 */
//			String messageNum = jedisClusterClient.get(MESSAGE_NUM + userConsumer.getId());
//			if (StringUtils.isEmpty(messageNum)){
//				ModelResult<Integer> result = cmsCommentServiceClient.queryMsgCountByUserId(userConsumer.getId());
//				if (result!=null && result.isSuccess() && result.getModel()!=null) {
//					Integer num = result.getModel();
//					/*缓存5分钟*/
//					messageNum = String.valueOf(num);
//					jedisClusterClient.set(MESSAGE_NUM + userConsumer.getId(), messageNum, 30);
//				}
//			}
			String messageNum = "0";
			String unreadNum = jedisClusterClient.get(UserMsgCacheConstant.USER_UNREAD_MESSAGE + userConsumer.getId());
			if (StringUtils.isNotEmpty(unreadNum)){
				messageNum =unreadNum;
			}
			centerInfoVo.setMessageNum(messageNum);

			if (centerInfoVo.getNickName().matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")) {
				centerInfoVo.setNickName(centerInfoVo.getNickName().substring(0, 7) + "XXXX");
			}
			
		} catch (Exception e) {
			logger.error("查询用户中心信息，出现异常：{}", e);
		}

		return centerInfoVo;

	}

}
