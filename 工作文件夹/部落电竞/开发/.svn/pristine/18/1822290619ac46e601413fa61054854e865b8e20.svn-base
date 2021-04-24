package com.esportzoo.esport.controller.matchtool;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.tool.ToolGameServiceClient;
import com.esportzoo.esport.client.service.tool.ToolMatchRoundServiceClient;
import com.esportzoo.esport.client.service.tool.ToolMatchServiceClient;
import com.esportzoo.esport.client.service.tool.ToolScheduleServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.matchtool.MatchToolCreateRequest;
import com.esportzoo.esport.connect.request.matchtool.MatchToolUpdateRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.UserInfoResponseVo;
import com.esportzoo.esport.connect.response.matchtool.MatchToolCreateDataResponse;
import com.esportzoo.esport.connect.response.matchtool.MatchToolInfoResponse;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.tool.ToolGameStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.tool.ToolGame;
import com.esportzoo.esport.domain.tool.ToolMatch;
import com.esportzoo.esport.domain.tool.ToolMatchRound;
import com.esportzoo.esport.domain.tool.ToolSchedule;
import com.esportzoo.esport.manager.UserManager;
import com.esportzoo.esport.manager.matchtool.MatchToolManager;
import com.esportzoo.esport.service.exception.errorcode.ToolMatchErrorTable;
import com.esportzoo.esport.vo.tool.ToolMatchRequestVo;
import com.esportzoo.esport.vo.tool.ToolMatchRoomResponseVo;
import com.esportzoo.esport.vo.tool.ToolMatchRoundQueryVo;
import com.esportzoo.esport.vo.tool.ToolMatchUpdateRequestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 赛事工具赛事信息相关controller
 * 
 * @author jing.wu
 * @version 创建时间：2019年11月18日 下午2:48:31
 */
@Controller
@RequestMapping("/matchtool")
@Api(value = "赛事工具赛事信息相关接口", tags = { "赛事相关接口" })
public class MatchToolInfoController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static String loggerPre = "赛事工具赛事信息controller_";
	@Autowired
	private MatchToolManager matchToolManager;

	@Autowired
	private ToolMatchServiceClient toolMatchServiceClient;

	@Autowired
	private ToolMatchRoundServiceClient toolMatchRoundServiceClient;

	@Autowired
	private ToolGameServiceClient toolGameServiceClient;

	@Autowired
	@Qualifier("toolScheduleServiceClient")
	private ToolScheduleServiceClient toolScheduleServiceClient;

	@Autowired
	UserManager userManager;

	@ApiOperation(value = "创建赛事信息查询接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/createMatch/info", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<MatchToolCreateDataResponse> infoForCreateMatch(Long matchId, BaseRequest baseRequest, HttpServletRequest request) {
		MatchToolCreateDataResponse resp = new MatchToolCreateDataResponse();
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}创建赛事信息查询接口,未获取到登录用户信息", loggerPre);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			// 1.当前用户是否可以创建赛事信息/判断是否可以修改当前赛事
			Boolean createFlag = false;
			if (null != matchId) {//判断修改权限
				createFlag = matchToolManager.judgeCanUpdateMatch(userConsumer.getId(), matchId);
			} else {//判断创建权限
				createFlag = matchToolManager.judgeCanCreateMatch(userConsumer.getId());
			}
			if (!createFlag) {
				logger.info("{}创建赛事信息查询接口,当前用户:{}暂无可创建/修改赛事权限", loggerPre, userConsumer.getId());
				return CommonResponse.withErrorResp("您暂无创建/修改赛事权限~");
			}
			resp.setCanCreate(createFlag);
			// 2.有效状态下的游戏列表
			List<ToolGame> gameList = matchToolManager.getToolGameList(ToolGameStatus.VALID.getIndex());
			if (gameList.isEmpty()) {
				logger.info("{}创建赛事信息查询接口,当前用户:{}暂无可选择的游戏", loggerPre, userConsumer.getId());
				return CommonResponse.withErrorResp("暂无可选择的游戏~");
			}
			resp.setAllGames(gameList);
			// 3.后台配置的可选队伍数
			SysConfigProperty property = getSysConfigByKey(SysConfigPropertyKey.MATCH_TOOL_TEAMS_LIST, baseRequest.getClientType(),baseRequest.getAgentId());
			if (null != property) {
				String[] values = property.getValue().split(",");
				List<Integer> teams = Arrays.stream(values).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
				resp.setTeamsList(teams);
			} else {
				logger.info("{}创建赛事信息查询接口,当前用户:{}暂无可选择的队伍列表", loggerPre, userConsumer.getId());
				return CommonResponse.withErrorResp("暂无可选择的队伍列表~");
			}

		} catch (Exception e) {
			logger.info("{}创建赛事信息查询接口,发生异常,exception={}", loggerPre, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
		return CommonResponse.withSuccessResp(resp);
	}

	@ApiOperation(value = "生成赛事接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/createMatch/submit", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Long> submitCreateMatch(MatchToolCreateRequest createRequest, HttpServletRequest request) {
		logger.info("{}生成赛事接口,当前请求接口参数:{}", loggerPre, JSONObject.toJSONString(createRequest));
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}生成赛事接口,未获取到登录用户信息", loggerPre);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			// 参数验证
			SysConfigProperty property = getSysConfigByKey(SysConfigPropertyKey.MATCH_TOOL_TEAMS_LIST, createRequest.getClientType(),createRequest.getAgentId());
			boolean gameFlag = true;
			ModelResult<ToolGame> toolGameModelResult = toolGameServiceClient.queryById(createRequest.getGameId().longValue());
			ToolGame toolGame = toolGameModelResult.getModel();
			if (!toolGameModelResult.isSuccess() || toolGame == null) {
				gameFlag = false;
			} else {
				gameFlag = ToolGameStatus.VALID.getIndex() == toolGame.getStatus().intValue();
			}
			if (!gameFlag) {
				logger.info("{}创建赛事接口,当前游戏:{}，无效的游戏类型", loggerPre, createRequest.getGameId());
				return CommonResponse.withErrorResp("无效的游戏类型~");
			}
			// 其他验证
			CommonResponse<Long> result = matchToolManager.verifyCreate(createRequest, userConsumer, property);
			if (result != null) {
				logger.info("校验不通过");
				return result;
			}
			logger.info("校验通过，准备创建赛事");
			// 创建赛事
			ToolMatchRequestVo requestVo = new ToolMatchRequestVo();
			BeanUtil.copyProperties(createRequest, requestVo);
			requestVo.setUserId(userConsumer.getId());
			requestVo.setNickName(userConsumer.getNickName());
			requestVo.setStartTime(DateUtil.parseToDate(createRequest.getStartTime(), "yyyy-MM-dd HH:mm"));
			requestVo.setDeadline(DateUtil.parseToDate(createRequest.getDeadline(), "yyyy-MM-dd HH:mm"));
			// 默认游戏的logo与背景
			requestVo.setLogoUrl(toolGame.getLogoUrl());
			requestVo.setImageUrl(toolGame.getImageUrl());
			logger.info("创建赛事requestVo:{}", JSON.toJSONString(requestVo));
			ModelResult<Long> createResult = toolMatchServiceClient.createMatch(requestVo);
			// 返回赛事ID
			if (createResult.getModel() != null) {
				return CommonResponse.withSuccessResp(createResult.getModel());
			} else {
				if (ToolMatchErrorTable.CREATE_MATCH_REPETITION.code.equals(createResult.getErrorCode())){
					return CommonResponse.withErrorResp(createResult.getErrorMsg());
				}
				return CommonResponse.withErrorResp("服务繁忙~");
			}
		} catch (Exception e) {
			logger.info("{}生成赛事接口,发生异常,exception={}", loggerPre, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@ApiOperation(value = "获取赛事信息(包括轮次)", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/getMatchInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<MatchToolInfoResponse> getMatchInfoById(@RequestParam(required = true) Long matchId, HttpServletRequest request) {
		logger.info("{}获取赛事接口,当前请求接口参数:{}", loggerPre, JSONObject.toJSONString(matchId));
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}获取赛事接口,未获取到登录用户信息", loggerPre);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			// 获取赛事信息
			ModelResult<ToolMatch> toolMatchModelResult = toolMatchServiceClient.queryById(matchId);
			ToolMatch toolMatch = toolMatchModelResult.getModel();
			if (toolMatch == null) {
				logger.info("{}获取赛事接口,不存在的赛事Id：{}，msg：{}", loggerPre, matchId, toolMatchModelResult.getErrorMsg());
				return CommonResponse.withErrorResp("不存在的赛事Id");
			}
			MatchToolInfoResponse response = new MatchToolInfoResponse();

			// 获取赛事所属游戏名字
			ModelResult<ToolGame> toolGameModelResult = toolGameServiceClient.queryById(toolMatch.getGameId().longValue());
			if (toolGameModelResult.getModel() != null) {
				response.setGameName(toolGameModelResult.getModel().getName());
				response.setGameLogo(toolGameModelResult.getModel().getLogoUrl());
				response.setGameImage(toolGameModelResult.getModel().getImageUrl());
			}
			// 获取轮次的开始时间
			ToolMatchRoundQueryVo roundQueryVo = new ToolMatchRoundQueryVo();
			roundQueryVo.setMatchId(matchId.intValue());
			ModelResult<List<ToolMatchRound>> listModelResult = toolMatchRoundServiceClient.queryList(roundQueryVo);
			List<ToolMatchRound> roundList = listModelResult.getModel();
			String[] roundTimes = roundList.stream().sorted(Comparator.comparing(ToolMatchRound::getStartTime)).map(r -> DateUtil.dateToString(r.getStartTime(), "yyyy-MM-dd HH:mm")).toArray(str -> new String[roundList.size()]);
			logger.info("{},根据赛事id查询的轮次开始时间：{}", loggerPre, JSON.toJSONString(roundTimes));
			// 组合结果
			BeanUtil.copyProperties(toolMatch, response);
			response.setMatchId(matchId);
			response.setStartTime(DateUtil.dateToString(toolMatch.getStartTime(), "yyyy-MM-dd HH:mm"));
			response.setDeadline(DateUtil.dateToString(toolMatch.getDeadline(), "yyyy-MM-dd HH:mm"));
			response.setRoundTimes(roundTimes);
			return CommonResponse.withSuccessResp(response);
		} catch (Exception e) {
			logger.info("{}获取赛事接口,发生异常,exception={}", loggerPre, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@ApiOperation(value = "修改赛事接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/updateMatch", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Long> updateMatch(MatchToolUpdateRequest updateRequest, HttpServletRequest request) {
		logger.info("{}修改赛事接口,当前请求接口参数:{}", loggerPre, JSONObject.toJSONString(updateRequest));
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}修改赛事接口,未获取到登录用户信息", loggerPre);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			// 参数验证
			CommonResponse<Long> result = matchToolManager.verifyUpdate(updateRequest, userConsumer.getId());
			if (result != null) {
				logger.info("校验不通过");
				return result;
			}
			ToolMatchUpdateRequestVo updateRequestVo = new ToolMatchUpdateRequestVo();
			BeanUtil.copyProperties(updateRequest, updateRequestVo);
			// 待完善
			ModelResult<Long> updateResult = toolMatchServiceClient.updateMatch(updateRequestVo);
			if (updateResult.getModel() != null) {
				return CommonResponse.withSuccessResp(updateResult.getModel());
			} else {
				return CommonResponse.withErrorResp("服务繁忙~");
			}
		} catch (Exception e) {
			logger.info("{}修改赛事接口,发生异常,exception={}", loggerPre, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@ApiOperation(value = "赛事房间首页", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/matchRoom/{matchId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<JSONObject> getMatchRoomInfo(@ApiParam(required = true, name = "赛事房间ID") @PathVariable("matchId") Long matchId,Integer pageType,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		ModelResult<ToolMatchRoomResponseVo> modelResult =new ModelResult<>();
		ToolMatchRoomResponseVo matchRoomResponseVo = new ToolMatchRoomResponseVo();
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}修改赛事接口,未获取到登录用户信息", loggerPre);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			/** 返回登录用户信息 */
			UserInfoResponseVo userInfoResponseVo = userManager.getUserInfoByDisplay(userConsumer);
			jsonObject.put("userInfo",userInfoResponseVo);


			logger.info("开始查询赛事房间首页信息,matchId={}", matchId);
			TimeInterval interval = new TimeInterval();
			modelResult = toolMatchServiceClient.queryMatchRoomInfo(matchId, userConsumer.getId());
			if (!modelResult.isSuccess() || modelResult.getModel() == null) {
				logger.error("根据matchId,查询赛事房间信息：matchId=【{}】，用户ID 【{}】，错误信息:【{}】", matchId, userConsumer.getId(), modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("查询赛事房间信息出错");
			}
			matchRoomResponseVo = modelResult.getModel();

			if (pageType != 1) {
				ModelResult<ToolMatchRoomResponseVo> modelResultBySchedule = toolScheduleServiceClient
						.toolScheduleList(matchId, userConsumer.getId());
				if (!modelResultBySchedule.isSuccess() || modelResultBySchedule.getModel() == null) {
					logger.error("查询赛事赛程：matchId=【{}】，用户ID 【{}】，错误信息:【{}】", matchId, userConsumer.getId(), modelResult.getErrorMsg());
					return CommonResponse.withErrorResp("查询赛事赛程出错");
				}
				ToolMatchRoomResponseVo responseSchedule = modelResultBySchedule.getModel();
				matchRoomResponseVo.setScheduleList(responseSchedule.getScheduleList());
				matchRoomResponseVo.setRounds(responseSchedule.getRounds());
			}
			jsonObject.put("teamRoomInfo", matchRoomResponseVo);
			logger.info("返回赛事房间首页信息,matchId={}，耗时：{}毫秒", matchId, interval.intervalMs());
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.error("查询赛事房间信息：matchId=【{}】，错误信息 【{}】", matchId, e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询赛事房间信息异常");
		}

	}

	@ApiOperation(value = "更新队伍比赛赛果", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/teamInfo/updateResult", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Boolean> updateResult(HttpServletRequest request, Long matchId, Long scheduleId, Long winnerId) {

		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info(loggerPre+"修改赛事接口,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}

			if (matchId == null) {
				logger.error("更新队伍比赛赛果信息，matchId.is.null");
				return CommonResponse.withErrorResp("matchId.is.null");
			}

			if (scheduleId == null) {
				logger.error("更新队伍比赛赛果信息，scheduleId.is.null");
				return CommonResponse.withErrorResp("scheduleId.is.null");
			}

			if (winnerId == null) {
				logger.error("更新队伍比赛赛果信息，winnerId.is.null");
				return CommonResponse.withResp("4444","winnerId.is.null");
			}

			logger.info("更新队伍比赛赛果信息,matchId={},scheduleId={},winnerId={}", matchId,scheduleId,winnerId);
			/** 判断是否为举办方 */
			ModelResult<ToolMatch> toolMatchModelResult = toolMatchServiceClient.queryById(matchId);
			if (!toolMatchModelResult.isSuccess() || toolMatchModelResult.getModel() == null) {
				logger.error(loggerPre + "申请退赛,查询赛事信息失败：matchId=【{}】，用户ID 【{}】，错误信息:【{}】", matchId, userConsumer.getId(),
						toolMatchModelResult.getErrorMsg());
				return CommonResponse.withErrorResp("查询赛事信息失败");
			}
			ToolMatch toolMatch = toolMatchModelResult.getModel();
			if (userConsumer.getId().longValue() != toolMatch.getCreateUserId().longValue()) {
				logger.error(loggerPre + "该用户不是举办方，不可修改赛事结果");
				return CommonResponse.withErrorResp("该用户不是举办方，不可修改赛事结果");
			}

			ModelResult<ToolSchedule> scheduleModelResult = toolScheduleServiceClient.queryById(scheduleId);
			if (!scheduleModelResult.isSuccess() || scheduleModelResult.getModel() == null) {
				logger.error("没有查询到对应的赛程信息，scheduleId=【{}】", scheduleId);
				return CommonResponse.withErrorResp("没有查询到对应的赛程信息");
			}
			ModelResult<Boolean> resultHandle = toolScheduleServiceClient.gameOverResultHandle(scheduleId, winnerId);

			if (!resultHandle.isSuccess() || resultHandle.getModel() == null) {
				logger.error("更新赛程信息失败，scheduleId=【{}】winnerId=【{}】,失败信息：{}", scheduleId, winnerId, resultHandle.getErrorMsg());
				return CommonResponse.withErrorResp(resultHandle.getErrorMsg());
			}

			return  CommonResponse.withSuccessResp(resultHandle.getModel());

		} catch (Exception e) {
			logger.error(loggerPre + "更新赛程结果异常，用户：【{}】，异常信息：【{}】", e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("更新赛程赛果异常");

		}


	}



}
