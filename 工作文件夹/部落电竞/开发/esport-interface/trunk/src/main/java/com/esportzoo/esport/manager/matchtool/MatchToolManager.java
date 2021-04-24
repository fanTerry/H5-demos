package com.esportzoo.esport.manager.matchtool;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerWhiteServiceClient;
import com.esportzoo.esport.client.service.tool.ToolGameServiceClient;
import com.esportzoo.esport.client.service.tool.ToolMatchServiceClient;
import com.esportzoo.esport.connect.request.matchtool.MatchToolCreateRequest;
import com.esportzoo.esport.connect.request.matchtool.MatchToolUpdateRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.FeatureKey;
import com.esportzoo.esport.constants.consumerwhite.Status;
import com.esportzoo.esport.constants.consumerwhite.WhiteType;
import com.esportzoo.esport.constants.tool.ToolGameStatus;
import com.esportzoo.esport.constants.tool.ToolMatchStatus;
import com.esportzoo.esport.constants.tool.ToolMatchType;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserConsumerWhite;
import com.esportzoo.esport.domain.tool.ToolGame;
import com.esportzoo.esport.domain.tool.ToolMatch;
import com.esportzoo.esport.domain.tool.ToolMatchRound;
import com.esportzoo.esport.service.exception.errorcode.CommonErrorTable;
import com.esportzoo.esport.service.tool.ToolMatchRoundService;
import com.esportzoo.esport.vo.UserConsumerWhiteVo;
import com.esportzoo.esport.vo.tool.ToolGameQueryVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 赛事工具
 * 
 * @author jing.wu
 * @version 创建时间：2019年11月18日 下午2:46:15
 */
@Component
public class MatchToolManager {

	private transient static final Logger logger = LoggerFactory.getLogger(MatchToolManager.class);

	private static String loggerPre = "赛事工具manager_";

	@Autowired
	private ToolGameServiceClient toolGameServiceClient;
	@Autowired
	private ToolMatchServiceClient toolMatchServiceClient;
	@Autowired
	private UserConsumerWhiteServiceClient userConsumerWhiteServiceClient;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private ToolMatchRoundService toolMatchRoundService;

	/** 根据状态查游戏列表 */
	public List<ToolGame> getToolGameList(Integer status) {
		ToolGameQueryVo vo = new ToolGameQueryVo();
		vo.setStatus(status);
		ModelResult<List<ToolGame>> modelResult = toolGameServiceClient.queryList(vo);
		if (null != modelResult && modelResult.isSuccess() && null != modelResult.getModel()) {
			return modelResult.getModel();
		} else {
			return Lists.newArrayList();
		}
	}

	/** 判断游戏是否有效 */
	public Boolean verifyToolGameStatus(Long toolGameId) {
		ModelResult<ToolGame> toolGameModelResult = toolGameServiceClient.queryById(toolGameId);
		ToolGame toolGame = toolGameModelResult.getModel();
		if (!toolGameModelResult.isSuccess() || toolGame == null) {
			return false;
		}
		return ToolGameStatus.VALID.getIndex() == toolGame.getStatus().intValue();
	}

	/** 判断是否可以创建比赛 */
	public Boolean judgeCanCreateMatch(Long userId) {
		UserConsumerWhiteVo vo = new UserConsumerWhiteVo();
		vo.setUserId(userId.intValue());
		vo.setWhiteType(WhiteType.TOOL_MATCH_CREATE_WHITE.getIndex());
		vo.setStatus(Status.VALID.getIndex());
		ModelResult<List<UserConsumerWhite>> modelResult = userConsumerWhiteServiceClient.queryByCondition(vo);
		if (null != modelResult && modelResult.isSuccess()
				&& null != modelResult.getModel() && !modelResult.getModel().isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/** 判断是否可以修改比赛 */
	public Boolean judgeCanUpdateMatch(Long userId, Long matchId) {
		ModelResult<ToolMatch> toolMatchModelResult = toolMatchServiceClient.queryById(matchId);
		ToolMatch dataToolMatch = toolMatchModelResult.getModel();
		if (dataToolMatch == null) {
			logger.info("{}判断是否可以修改比赛,查无此赛事:{}", loggerPre, matchId);
			return Boolean.FALSE;
		}
		if (dataToolMatch.getCreateUserId().equals(userId.intValue())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 创建队伍的参数验证
	 * 
	 * @param createRequest
	 * @param userConsumer
	 * @return
	 */
	public CommonResponse<Long> verifyCreate(MatchToolCreateRequest createRequest, UserConsumer userConsumer, SysConfigProperty property) {
		try {
			String catchKey = "create_tool_match_"+userConsumer.getId();
			if (!redisClient.setNX(catchKey, "1", 3)){
				logger.info("创建赛事请求过于频繁：【{}】-【{}】",userConsumer.getId(),createRequest.getName());
				return CommonResponse.withErrorResp("请稍微休息下~");
			}
			if (createRequest.getGameId() == null || createRequest.getTeams() == null
					|| createRequest.getRounds() == null) {
				logger.info("{}创建赛事接口,当前用户:{},请求必要参数为空:{}", loggerPre, userConsumer.getId(), JSON.toJSONString(createRequest));
				return CommonResponse.withErrorResp(CommonErrorTable.paramError.getMsg());
			}
			// 当前用户是否可以创建赛事信息
			Boolean createFlag = judgeCanCreateMatch(userConsumer.getId());
			if (!createFlag) {
				logger.info("{}创建赛事接口,当前用户:{}暂无可创建赛事权限", loggerPre, userConsumer.getId());
				return CommonResponse.withErrorResp("您暂无创建赛事权限~");
			}
			// 游戏类型是否有效
			Boolean gameFlag = verifyToolGameStatus(createRequest.getGameId().longValue());
			if (!gameFlag) {
				logger.info("{}创建赛事接口,当前游戏:{}，无效的游戏类型", loggerPre, createRequest.getGameId());
				return CommonResponse.withErrorResp("无效的游戏类型~");
			}
			// 队伍数
			Integer teamNum = createRequest.getTeams();
			logger.info("队伍数配置：{}", JSON.toJSONString(property.getValue()));
			boolean teamNumFlag = false;
			if (null != property) {
				String[] values = property.getValue().split("\\,");
				for (String value : values) {
					if (teamNum.equals(Integer.valueOf(value))) {
						teamNumFlag = true;
						break;
					}
				}
			}
			if (!teamNumFlag) {
				logger.info("{}创建赛事接口,无效的队伍数：{}", loggerPre, createRequest.getTeams());
				return CommonResponse.withErrorResp("无效的队伍数~");
			}
			// 轮次
			Integer rounds = createRequest.getRounds();
			// 2的轮数次方要等于队伍数
			BigDecimal pow = NumberUtil.pow(new BigDecimal(2), rounds);
			if (pow.intValue() != teamNum) {
				logger.info("{}创建赛事接口,错误的轮次：{}", loggerPre, createRequest.getRounds());
				return CommonResponse.withErrorResp("错误的轮次数~");
			}
			// 轮次时间
			String[] roundTimes = createRequest.getRoundTimes();
			int length = roundTimes.length;
			for (int i = 0; i < length; i++) {
				if (i == 0) {
					continue;
				}
				if (DateUtil.calcCompareDate2(roundTimes[i] + ":00", roundTimes[i - 1] + ":00") < 0) {
					logger.info("{}创建赛事接口,第{}轮与第{}轮的开始时间错误：本轮{}##{}上轮",
							loggerPre, i, i + 1, roundTimes[i], roundTimes[i - 1] + ":00");
					return CommonResponse.withErrorResp("第" + i + "轮与第" + (i + 1) + "轮的开始时间错误~");
				}
			}
			if (length != rounds) {
				logger.info("{}创建赛事接口,轮次与轮次时间有误，轮次：{}，轮次时间:{}", loggerPre, createRequest.getRounds(), JSON.toJSONString(createRequest.getRoundTimes()));
				return CommonResponse.withErrorResp("轮次数与轮次时间数有误~");
			}
			// 队伍人数 暂定5人
			Integer plays = createRequest.getPlays();
			if (plays != 5) {
				logger.info("{}创建赛事接口,当前用户:{},不支持的队伍人数：{}", loggerPre, userConsumer.getId(), createRequest.getPlays());
				return CommonResponse.withErrorResp("不支持的队伍人数~");
			}
			String startTime = createRequest.getStartTime();
			String deadline = createRequest.getDeadline();
			if (StringUtils.isBlank(startTime) || StringUtils.isBlank(deadline)) {
				logger.info("{}创建赛事接口,时间参数包含空值：{}", loggerPre, userConsumer.getId(), createRequest.getPlays());
				return CommonResponse.withErrorResp("时间参数包含空值~");
			}
			if (DateUtil.calcCompareDate2(startTime + ":00", deadline + ":00") < 0) {
				logger.info("{}创建赛事接口,开赛时间不能小于报名截止时间：开{}##{}截", loggerPre, startTime, deadline);
				return CommonResponse.withErrorResp("开赛时间不能小于报名截止时间~");
			}
			// 模式
			ToolMatchType toolMatchType = ToolMatchType.valueOf(createRequest.getMatchType());
			if (toolMatchType == null) {
				logger.info("{}创建赛事接口,无效的赛制模式：{}", loggerPre, createRequest.getMatchType());
				return CommonResponse.withErrorResp("无效的赛制模式~");
			}
			return null;
		} catch (Exception e) {
			logger.info("参数校验异常", e);
			return CommonResponse.withErrorResp("参数校验异常~");
		}
	}

	/**
	 * 修改赛事参数校验
	 * 
	 * @param updateRequest
	 * @return
	 */
	public CommonResponse<Long> verifyUpdate(MatchToolUpdateRequest updateRequest, Long userId) {
		Long matchId = updateRequest.getMatchId();
		if (matchId == null) {
			logger.info("{}修改赛事接口,matchId为空", loggerPre);
			return CommonResponse.withErrorResp("赛事Id不能为空");
		}
		//暂不支持修改状态
		updateRequest.setStatus(null);
		ModelResult<ToolMatch> toolMatchModelResult = toolMatchServiceClient.queryById(updateRequest.getMatchId());
		ToolMatch dataToolMatch = toolMatchModelResult.getModel();
		if (dataToolMatch == null) {
			logger.info("{}修改赛事接口,查无此赛事:{}", loggerPre, updateRequest.getMatchId());
			return CommonResponse.withErrorResp("无此赛事");
		}
		if (dataToolMatch.getStatus().intValue() == ToolMatchStatus.OVER.getIndex()){
			logger.info("{}修改赛事接口,赛事已结束{}", loggerPre, updateRequest.getMatchId());
			return CommonResponse.withErrorResp("赛事已结束");
		}
		if (!dataToolMatch.getCreateUserId().equals(userId.intValue())) {
			logger.info("{}修改赛事接口,{}不是该赛事的创建者：{}", loggerPre, userId, dataToolMatch.getCreateUserId());
			return CommonResponse.withErrorResp("不是自己创建的赛事呦~");
		}
		//报名截止时间
		if (updateRequest.getDeadline() != null
				&& !updateRequest.getDeadline().equals(DateUtil.dateToString(dataToolMatch.getDeadline(),"yyyy-MM-dd HH:mm"))
				&& DateUtil.calcCompareDate3(new Date(), dataToolMatch.getDeadline()) >= 0) {
			logger.info("{}修改赛事接口,报名已经截止：{}", loggerPre, dataToolMatch.getDeadline());
			return CommonResponse.withErrorResp("报名已经截止，不可变更报名时间~");
		}
		//开赛时间
		String startTime = updateRequest.getStartTime();
		if (startTime!=null){
			if (!startTime.equals(DateUtil.dateToString(dataToolMatch.getStartTime(),"yyyy-MM-dd HH:mm"))){
				if (!dataToolMatch.getStatus().equals(ToolMatchStatus.NOTSTART.getIndex())
						&& !dataToolMatch.getStatus().equals(ToolMatchStatus.RECRUITING.getIndex())) {
					logger.info("{}修改赛事接口,当前赛事状态【{}】不允许变更开赛时间", loggerPre, ToolMatchStatus.valueOf(dataToolMatch.getStatus()).getDescription());
					return CommonResponse.withErrorResp(ToolMatchStatus.valueOf(dataToolMatch.getStatus()).getDescription() + "的赛事，不允许变更开赛时间~");
				}
				if (DateUtil.calcCompareDate(dataToolMatch.getDeadline() + ":00", startTime + ":00") < 0){
					logger.info("{}修改赛事接口,开赛时间【{}】不能小于报名截止时间【{}】", loggerPre, startTime,updateRequest.getDeadline());
					return CommonResponse.withErrorResp("开赛时间不能小于报名截止时间~");
				}
			}
		}
		ModelResult<List<ToolMatchRound>> cacheListByMatchId = toolMatchRoundService.queryCacheListByMatchId(updateRequest.getMatchId());
		List<ToolMatchRound> matchRoundList = cacheListByMatchId.getModel();
		CollectionUtil.sort(matchRoundList, Comparator.comparing(ToolMatchRound::getRound));
		//轮次时间
		String[] roundTimes = updateRequest.getRoundTimes();
		if (roundTimes != null) {
			int length = roundTimes.length;
			for (int i = 0; i < length; i++) {
				ToolMatchRound matchRound = matchRoundList.get(i);
				if (matchRound != null ){
					String feature = matchRound.getFeature(FeatureKey.TOOL_ROUND_START);
					if ("start".equals(feature) || "end".equals(feature)){
						logger.info("该轮次状态：{}，不允许修改",feature);
						return CommonResponse.withErrorResp("第"+(i+1)+"轮已经生效，实在是不能变了~");
					}
				}
				if (i == 0){
					if (DateUtil.calcCompareDate2(roundTimes[i] + ":00", startTime+":00") < 0) {
						logger.info("创建赛事接口,第1轮开始时间不能小于开赛时间：本轮{}##{}上轮",
								roundTimes[i],startTime);
						return CommonResponse.withErrorResp("第1轮开始时间不能小于开赛时间~");
					}
					continue;
				}

				if (com.esportzoo.common.util.DateUtil.calcCompareDate2(roundTimes[i] + ":00", roundTimes[i - 1] + ":00") < 0) {
					logger.info("创建赛事接口,第{}轮与第{}轮的开始时间错误：本轮{}##{}上轮",
							i, i + 1, roundTimes[i], roundTimes[i - 1] + ":00");
					return CommonResponse.withErrorResp("第" + (i+1) + "轮时间不能小于第" + i + "轮的时间~");
				}
			}
		}
		return null;
	}
}
