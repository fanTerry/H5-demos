package com.esportzoo.esport.manager.hd;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.annotation.EsportMethodLog;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.ShareCodeUtils;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.hd.Hd104Request;
import com.esportzoo.esport.constant.CachedKeyAndTimeLong;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.hd.constants.HdCode;
import com.esportzoo.esport.hd.constants.HdUserShareType;
import com.esportzoo.esport.hd.constants.ShareLinkStatus;
import com.esportzoo.esport.hd.entity.HdInfo;
import com.esportzoo.esport.hd.entity.HdUserShare;
import com.esportzoo.esport.hd.entity.HdUserShareLog;
import com.esportzoo.esport.hd.gift.HdUserLogServiceClient;
import com.esportzoo.esport.hd.info.HdInfoServiceClient;
import com.esportzoo.esport.hd.share.HdUserShareLogServiceClient;
import com.esportzoo.esport.hd.share.HdUserShareServiceClient;
import com.esportzoo.esport.hd.vo.HdUserShareLogVo;
import com.esportzoo.esport.hd.vo.HdUserShareVo;
import com.esportzoo.esport.hd.vo.JoinActParam;
import com.esportzoo.esport.hd.vo.JoinActResult;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 分享助力获取星星活动相关
 *
 * @author: Haitao.Li
 *
 * @create: 2020-03-19 11:00
 **/

@Component
public class Hd104Manager {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String logPrefix = "104分享活动相关接口-";

	@Autowired
	private HdInfoServiceClient hdInfoServiceClient;

	@Autowired
	private HdUserShareServiceClient hdUserShareServiceClient;

	@Autowired
	private HdUserShareLogServiceClient hdUserShareLogServiceClient;

	@Autowired
	private HdUserLogServiceClient hdUserLogServiceClient;

	@Autowired
	private RedisClient redisClient;

	public static final String SHARE_CODE_KEY = "esport_share_code_key_";


	/**
	 *
	 * @param userIdHelp 点击链接助力的用户
	 * @param shareCode 分享码
	 * @return
	 */
	@EsportMethodLog(paramLog=false)
	public boolean sendUserHdGift(Long userIdHelp, String shareCode, BaseRequest request) {

		try {
			ModelResult<HdInfo> modelResult = hdInfoServiceClient.queryByCode(HdCode.HD_104.getIndex());
			if (!modelResult.isSuccess() || modelResult.getModel() == null) {
				return false;
			}
			HdInfo hdInfo = modelResult.getModel();
			//参与助力资格校验
			boolean isCanSendGift = false;
			HdUserShareLog hdUserShareLog = null;
			HdUserShareLogVo shareLogVo = new HdUserShareLogVo();
			shareLogVo.setRespondUserId(userIdHelp.intValue());
			shareLogVo.setHdId(hdInfo.getId());
			shareLogVo.setShareCode(shareCode);
			logger.info(logPrefix+"助力用户：【{}】，查询参数参数param 【{}】",userIdHelp.intValue(),JSON.toJSONString(shareLogVo));
			ModelResult<List<HdUserShareLog>> result = hdUserShareLogServiceClient.queryByCondition(shareLogVo, null);
			if (result != null && result.isSuccess() && result.getModel() != null && result.getModel().size() > 0) {
				//用户有点击分享链接的记录
				List<HdUserShareLog> hdUserShareLogList = result.getModel();
				if (hdUserShareLogList.size() > 1) {
					logger.warn(logPrefix + "点击链接用户：【{}】，参数param 【{}】", userIdHelp, JSON.toJSONString(hdUserShareLogList));
				}
				hdUserShareLog = hdUserShareLogList.get(0);
				logger.info(logPrefix + "用户：【{}】，点击链接流水参数 【{}】", userIdHelp, JSON.toJSONString(hdUserShareLog));
				Integer status = hdUserShareLog.getFeature("status", Integer.class);
				//检查链接是否已使用
				if (status == ShareLinkStatus.NO_USE.getIndex()) {
					isCanSendGift = true;
				}
			}else {
				logger.info(logPrefix+"用户：【{}】，不存在助力点击记录 【{}】");
			}

			if (isCanSendGift) {
				if (shareLogVo != null) {
					//要赠予星星的用户（分享的用户）
					Integer userIdAccept = hdUserShareLog.getUserId();

					JoinActParam joinActParam = new JoinActParam();
					joinActParam.setSellClient(request.getClientType());
					joinActParam.setSellChannel(request.getAgentId());
					joinActParam.setHdCode(Long.valueOf(HdCode.HD_104.getIndex()));
					//获得赠送的用户
					joinActParam.setCustomerId(userIdAccept.longValue());
					joinActParam.setBizSystem(request.getBiz());
					HashMap<String, Object> map = Maps.newHashMap();
					//助力的用户
					map.put("userIdHelp", userIdHelp);

					map.put("shareCode", shareCode);
					joinActParam.setExtParamMap(map);
					JoinActResult joinActResult = hdUserLogServiceClient.joinActivity(joinActParam);

					if (joinActResult != null && joinActResult.isSuccess()) {
						logger.info(logPrefix + "用户：【{}】助力星星成功，赠送流水 【{}】", userIdHelp, JSON.toJSONString(joinActResult.getHdGiftLog()));
						return true;
					} else {
						logger.error(logPrefix + "用户：【{}】，助力星星失败，失败信息： 【{}】", userIdHelp, joinActResult.getErrorMsg());
					}
				}
			}

		} catch (Exception e) {
			logger.error(logPrefix + "助力用户【{}】参与助力赠送星星活动，发生异常，异常信息： ", userIdHelp, e);
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 添加点击分享链接流水
	 * @param hdRequest
	 * @param userConsumer
	 */
	public void addClickShareLinkLog(Hd104Request hdRequest, UserConsumer userConsumer) {
		//如果是通过携带分享码的链接进来的,插入链接响应者流水

		String linkCode = hdRequest.getShareCode();
		ModelResult<HdUserShare> result = hdUserShareServiceClient.queryByShareCode(linkCode);
		if (result != null && result.isSuccess() && result.getModel() != null) {
			HdUserShare linkUserShare = result.getModel();
			//增添点击分享链接流水，排除自己点击
			if (!linkUserShare.getUserId().equals(userConsumer.getId())) {
				HdUserShareLogVo shareLogVo = new HdUserShareLogVo();
				shareLogVo.setShareCode(linkUserShare.getShareCode());
				shareLogVo.setRespondUserId(userConsumer.getId().intValue());

				ModelResult<List<HdUserShareLog>> modelResult = hdUserShareLogServiceClient.queryByCondition(shareLogVo, null);
				//不需要重复添加，一个用户产生一条记录
				if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null && modelResult.getModel().size() == 0) {
					//插入分享响应者流水
					HdUserShareLog hdUserShareLog = new HdUserShareLog();
					hdUserShareLog.setShareCode(linkUserShare.getShareCode());
					hdUserShareLog.setRespondUserId(userConsumer.getId().intValue());
					hdUserShareLog.setChannelNo(hdRequest.getAgentId().intValue());
					hdUserShareLog.setClientType(hdRequest.getClientType());
					hdUserShareLog.setHdId(hdRequest.getHdId());
					hdUserShareLog.setShareTime(linkUserShare.getCreateTime());
					hdUserShareLog.setShareType(linkUserShare.getShareType());
					hdUserShareLog.setUserId(linkUserShare.getUserId());
					hdUserShareLog.setShareUrl(linkUserShare.getShareUrl());
					hdUserShareLog.setCreateTime(new Date());
					hdUserShareLog.setShareUrl("");
					//代表记录是否已赠送过，有效1，无效0
					hdUserShareLog.setupFeature("status", ShareLinkStatus.NO_USE.getIndex());
					ModelResult<Integer> add = hdUserShareLogServiceClient.add(hdUserShareLog);


				}
			}


		}

	}

	/**
	 * 获取用户分享码
	 * @param userConsumer
	 * @param hdRequest
	 * @return
	 */
	@EsportMethodLog(paramLog=false)
	public String getShareCode(UserConsumer userConsumer, Hd104Request hdRequest) {

		String shareCode = "";
		if (StringUtils.isNotEmpty(redisClient.get(SHARE_CODE_KEY + userConsumer.getId()))) {
			shareCode = redisClient.get(SHARE_CODE_KEY + userConsumer.getId());
			return shareCode;
		}

		/** 活动有效，获取活动分享码 */
		HdUserShareVo shareVo = new HdUserShareVo();
		shareVo.setUserId(userConsumer.getId().intValue());
		shareVo.setHdId(hdRequest.getHdId());
		ModelResult<List<HdUserShare>> result = hdUserShareServiceClient.queryByCondition(shareVo, null);
		if (result != null && result.isSuccess() && result.getModel() != null && result.getModel().size() > 0) {
			//已有分享码
			List<HdUserShare> userShares = result.getModel();
			if (userShares.size() > 1) {
				logger.warn("活动104，用户{},分享码有多个", userConsumer.getId());
			}
			HdUserShare hdUserShare = userShares.get(0);
			if (StringUtils.isNotEmpty(hdUserShare.getShareCode())) {
				shareCode = hdUserShare.getShareCode();
				//分享码根据用户ID，是唯一的，缓存2小时
				redisClient.set(SHARE_CODE_KEY + userConsumer.getId(), shareCode, CachedKeyAndTimeLong.setHour(2));
				return shareCode;
			}
		}

		if (result != null && result.isSuccess() && result.getModel() != null && result.getModel().size() == 0) {
			//未有分享码
			if (StringUtils.isEmpty(shareCode)) {
				/** 首次参与活动，生成分享码 */
				HdUserShare hdUserShare = new HdUserShare();
				hdUserShare.setChannelNo(hdRequest.getAgentId().intValue());
				hdUserShare.setClientType(hdRequest.getClientType());
				hdUserShare.setHdId(hdRequest.getHdId());
				hdUserShare.setUserId(userConsumer.getId().intValue());
				hdUserShare.setShareType(HdUserShareType.WEIXIN.getIndex());
				hdUserShare.setBizSystem(hdRequest.getBiz());
				hdUserShare.setCreateTime(new Date());
				hdUserShare.setShareCode(ShareCodeUtils.toSerialCode(userConsumer.getId()).toLowerCase());
				ModelResult<Integer> add = hdUserShareServiceClient.add(hdUserShare);
				logger.info(logPrefix + "用户：【{}】，生成的分享码 【{}】", userConsumer.getId(), hdUserShare.getShareCode());
				if (add != null && add.isSuccess() && add.getModel() != null) {
					shareCode = hdUserShare.getShareCode();
					redisClient.set(SHARE_CODE_KEY + userConsumer.getId(), shareCode, CachedKeyAndTimeLong.setHour(2));
					return shareCode;
				}
			}
		}
		logger.info(logPrefix + "用户：【{}】，获取分享码失败,查询信息:{}", userConsumer.getId(), result.getErrorMsg());


		return shareCode;
	}









}
