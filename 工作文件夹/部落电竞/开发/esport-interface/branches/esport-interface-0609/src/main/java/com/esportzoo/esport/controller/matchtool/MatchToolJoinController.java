package com.esportzoo.esport.controller.matchtool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.tool.ToolMatchServiceClient;
import com.esportzoo.esport.client.service.tool.ToolPlayerServiceClient;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.tool.ToolPlayerStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.tool.ToolMatch;
import com.esportzoo.esport.vo.tool.ToolPlayerRequestVo;

/**
 * 赛事工具参与或退出相关controller
 * @author jing.wu
 * @version 创建时间：2019年11月18日 下午2:48:31
 */
@Controller
@RequestMapping("/matchtool")
@Api(value = "赛事工具参与或退出相关接口", tags = { "赛事相关接口" })
public class MatchToolJoinController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static String loggerPre = "赛事工具参与或退出controller_";
	public static final String JOINKEY = "join_num_";

	@Autowired
	@Qualifier("toolPlayerServiceClient")
	private ToolPlayerServiceClient toolPlayerServiceClient;
	@Autowired
	@Qualifier("toolMatchServiceClient")
	private ToolMatchServiceClient toolMatchServiceClient;
	@Autowired
	RedisClient redisClient;

	@ApiOperation(value = "申请退赛", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/matchRoom/quiz", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Integer> matchQuizByUser(HttpServletRequest request, Long matchId, Long userApplyId, Long teamId) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info(loggerPre + "申请退赛接口,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			if (teamId == null) {
				logger.info(loggerPre + "用户：【{}】，队伍ID 【{}】", userConsumer.getId(), teamId);
			}
			ModelResult<ToolMatch> toolMatchModelResult = toolMatchServiceClient.queryById(matchId);
			if (!toolMatchModelResult.isSuccess() || toolMatchModelResult.getModel() == null) {
				logger.error(loggerPre + "申请退赛,查询赛事信息失败：matchId=【{}】，用户ID 【{}】，错误信息:【{}】", matchId, userConsumer.getId(),
						toolMatchModelResult.getErrorMsg());
				return CommonResponse.withErrorResp("查询赛事信息失败");
			}
			ToolMatch toolMatch = toolMatchModelResult.getModel();
			// 当登录用户不是主办方，只能申请自己的账号退赛
			if (userConsumer.getId().longValue() != toolMatch.getCreateUserId().longValue()) {
				if (!userConsumer.getId().equals(userApplyId)) {
					logger.error(loggerPre + "申请退赛：matchId=【{}】，登录用户ID 【{}】，申请退赛用户【{}】，当前用户不是主办方，不可申请他人用户退赛", matchId, userConsumer.getId(),
							userApplyId, toolMatchModelResult.getErrorMsg());
					return CommonResponse.withErrorResp("申请参与或退赛出错");
				}
			}
			Integer teamNum;
			ToolPlayerRequestVo requestVo = new ToolPlayerRequestVo();
			requestVo.setMatchId(matchId);
			requestVo.setTeamId(teamId);
			requestVo.setUserId(userApplyId);
			requestVo.setPlayerStatus(ToolPlayerStatus.QUIT.getIndex());
			ModelResult<Integer> modelResult = toolPlayerServiceClient.playerJoinOrQuitTeam(requestVo);
			if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
				teamNum = modelResult.getModel();
			} else {
				logger.error(loggerPre + "用户申请退赛：，申请退赛用户【{}】，参数param 【{}，错误信息：{}", userApplyId, JSON.toJSONString(requestVo),
						modelResult.getErrorMsg());
				return CommonResponse.withResp("4444", modelResult.getErrorMsg());
			}
			if (teamNum != null) {
				logger.info(loggerPre + "用户：【{}】申请退赛成功，teamId=【{}】，matchId=【{}】", userApplyId, teamId, matchId);
				return CommonResponse.withSuccessResp(teamNum);
			}
			logger.warn(loggerPre + "用户：【{}】返回的队伍人数:teamNum.is.null，matchId=【{}】", userApplyId, matchId);
			return CommonResponse.withResp("4444", modelResult.getErrorMsg());
		} catch (Exception e) {
			logger.error("申请退赛异常：matchId=【{}】，错误信息 【{}】", matchId, e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("申请退赛异常");
		}
	}

	@ApiOperation(value = "申请参与比赛", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/matchRoom/join", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Integer> matchJoinByUser(HttpServletRequest request, Long matchId, Long teamId, Integer position) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info(loggerPre + "修改赛事接口,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			/** 防止重复多次报名，一分钟限制3次报名 */
			String joinNum = redisClient.get(JOINKEY + matchId + "_" + userConsumer.getId());
			if (StringUtils.isEmpty(joinNum)) {
				joinNum = "0";
			} else {
				if (Integer.valueOf(joinNum) >= 3) {
					return CommonResponse.withResp("5555", "请不要频繁报名");
				}
			}

			ModelResult<ToolMatch> toolMatchModelResult = toolMatchServiceClient.queryById(matchId);
			if (!toolMatchModelResult.isSuccess() || toolMatchModelResult.getModel() == null) {
				logger.error(loggerPre + "申请参赛：根据matchId查询当前举办赛事出错，matchId=【{}】，teamId={},用户ID 【{}】，错误信息:【{}】", matchId, teamId, userConsumer.getId(),
						toolMatchModelResult.getErrorMsg());
				return CommonResponse.withErrorResp("参赛报名异常");
			}

			ToolPlayerRequestVo requestVo = new ToolPlayerRequestVo();
			requestVo.setMatchId(matchId);
			requestVo.setTeamId(teamId);
			requestVo.setUserId(userConsumer.getId());
			requestVo.setIndex(position);
			requestVo.setPlayerStatus(ToolPlayerStatus.JOIN.getIndex());
			ModelResult<Integer> modelResult = toolPlayerServiceClient.playerJoinOrQuitTeam(requestVo);
			if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
				Integer teamNum = modelResult.getModel();
				if (teamNum != null) {
					logger.info(loggerPre + "用户：【{}】报名成功，参数param 【{}】", userConsumer.getId(), JSON.toJSONString(requestVo));
					redisClient.set(JOINKEY + matchId + "_" + userConsumer.getId(), String.valueOf(Integer.valueOf(joinNum) + 1), 60);
					return CommonResponse.withSuccessResp(teamNum);
				} else {
					logger.error(loggerPre + "报名成功出错：，申请退赛用户【{}】，返回的队伍人数:teamNum.is.null，参数param 【{}】", userConsumer.getId(), JSON.toJSONString(requestVo));
					return CommonResponse.withResp("4444", "报名失败");
				}
			}
			logger.error(loggerPre + "参赛报名出错：matchId=【{}】，teamId={},报名用户【{}】，出错信息:【{}】", matchId, teamId, userConsumer.getId(),
					modelResult.getErrorMsg());
			return CommonResponse.withResp("4444", modelResult.getErrorMsg());
		} catch (Exception e) {
			logger.error("申请参与比赛异常：matchId=【{}】, teamId=【{}】，错误信息 【{}】", matchId, teamId, e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("参赛异常");
		}
	}

}
