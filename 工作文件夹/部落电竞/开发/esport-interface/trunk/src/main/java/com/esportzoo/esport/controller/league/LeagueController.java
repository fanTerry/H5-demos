package com.esportzoo.esport.controller.league;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.cms.UserFollowServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.MatchRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.league.LeagueObjectVo;
import com.esportzoo.esport.connect.response.league.MatchDetailPageResponse;
import com.esportzoo.esport.connect.response.league.SerieResultVo;
import com.esportzoo.esport.connect.response.matchtool.MatchToolRoomResponse;
import com.esportzoo.esport.constants.cms.FollowObjectType;
import com.esportzoo.esport.constants.cms.FollowStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.controller.ws.client.MatchingDataWebSocketClient;
import com.esportzoo.esport.controller.ws.server.UsrConsumerCacheManager;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserFollow;
import com.esportzoo.esport.manager.LeagueManager;
import com.esportzoo.esport.vo.MemberSession;
import com.esportzoo.esport.vo.cms.UserFollowQueryVo;
import com.esportzoo.leaguelib.common.result.MatchDataInfoResul;
import com.esportzoo.leaguelib.common.result.MatchDataResult;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.MATCHID_GAMEID_CACHEKEY;

/**
 * 获取赛事相关数据
 * 
 * @author haitao.li
 */
@Controller
@RequestMapping("league")
@Api(value = "赛事数据相关接口", tags = { "赛事相关接口" })
public class LeagueController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LeagueManager leagueManager;

	@Autowired
	@Qualifier("userFollowServiceClient")
	private UserFollowServiceClient userFollowServiceClient;
	@Autowired
	private RedisClient redisClient;

	@Autowired
	private UsrConsumerCacheManager usrConsumerCacheManager;

	public static final String MATCH_PAGE_DATA_CACHE = "match_page_data_cache_";

	@RequestMapping(value = "/leagueIndexdata", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "赛事首页数据接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "赛事首页数据接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> legueIndexData(MatchRequest matchDetailRequest) {

		CommonResponse<JSONObject> commonResponse = leagueManager.getLeagueIndex(matchDetailRequest);
		return commonResponse;
	}


	@RequestMapping(value = "/leagueTypeList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "赛事类型", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "赛事类型", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<LeagueObjectVo>> leagueTypeList(BaseRequest baseRequest) {
		List<LeagueObjectVo> leagueTypeList = leagueManager.getLeagueTypeList(baseRequest.getClientType(),baseRequest.getAgentId());
		if (CollUtil.isEmpty(leagueTypeList)) {
			leagueTypeList = Lists.newArrayList();
		}
		return CommonResponse.withSuccessResp(leagueTypeList);
	}

	@RequestMapping(value = "/userfollow", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "用户赛事关注接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "用户赛事关注接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Boolean> userFollow(HttpServletRequest request, Long matchId, Integer status) {
		MemberSession memberSession = getMemberSession(request);
		if (memberSession == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		if (status == null) {
			logger.error("关注赛事状态为空");
			return CommonResponse.withErrorResp("关注状态为空");
		}
		UserConsumer userConsumer = memberSession.getMember();

		try {
			UserFollowQueryVo queryVo = new UserFollowQueryVo();
			queryVo.setObjectId(matchId);
			queryVo.setObjectType(FollowObjectType.MATCH.getIndex());
			queryVo.setUserId(userConsumer.getId());
			ModelResult<List<UserFollow>> listModelResult = userFollowServiceClient.queryList(queryVo);
			if (listModelResult.isSuccess() && listModelResult.getModel() != null && listModelResult.getModel().size() > 0) {
				/* 存在关注记录，更新状态 */
				List<UserFollow> followList = listModelResult.getModel();
				UserFollow userFollow = followList.get(0);
				FollowStatus followStatus = FollowStatus.valueOf(status);
				if (followStatus == null) {
					logger.error("关注赛事状态异常：followStatus={}", status);
					return CommonResponse.withErrorResp("关注赛事状态异常");
				}
				userFollow.setStatus(followStatus.getIndex());
				ModelResult<Integer> update = userFollowServiceClient.update(userFollow);
				if (update.isSuccess() && update.getModel() > 0) {
					return CommonResponse.withSuccessResp("" + followStatus.getDescription() + "成功");
				}

			} else {
				/* 新增 */
				UserFollow userFollow = new UserFollow();
				userFollow.setUserId(userConsumer.getId());
				userFollow.setObjectType(FollowObjectType.MATCH.getIndex());
				userFollow.setObjectId(matchId);
				userFollow.setStatus(FollowStatus.FOLLOW.getIndex());
				ModelResult<Long> modelResult = userFollowServiceClient.save(userFollow);
				if (modelResult.isSuccess() && modelResult.getModel() > 0) {
					return CommonResponse.withSuccessResp("关注成功");
				}
			}
		} catch (Exception e) {
			logger.error("用户「{}」关注赛事异常,信息：{}", userConsumer.getId(), e);
			e.printStackTrace();
		}
		return CommonResponse.withErrorResp("关注失败");
	}

	@ApiOperation(value = "获取赛事详情接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/detail/{matchId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<MatchDetailPageResponse> getMatchDetailById(@ApiParam(required = true, name = "赛事id") @PathVariable("matchId") Long matchId, HttpServletRequest request, BaseRequest baseRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据id获取赛事详情,未获取到登录用户信息，matchId={}", matchId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			MatchDetailPageResponse detailResponse = leagueManager.getMatchDetailPageResponse(matchId,baseRequest.getClientType(),baseRequest.getAgentId());
			if (null == detailResponse) {
				logger.info("根据id获取不到赛事详情，matchId={}", matchId);
				return CommonResponse.withErrorResp("未获取到赛事详情");
			}
			String wsURL = usrConsumerCacheManager.buildWSURL();
			detailResponse.setChatSocketUrl(wsURL);
			detailResponse.setUserId(userConsumer.getId());
			detailResponse.setNickName(userConsumer.getNickName());
			logger.info("根据id获取赛事详情,detailResponse={}",JSON.toJSONString(detailResponse));
			return CommonResponse.withSuccessResp(detailResponse);
		} catch (Exception e) {
			logger.info("根据id获取赛事详情，发生异常，matchId={}，exception={}", matchId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@ApiOperation(value = "获取赛事详情tab数据接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/data/{matchId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<MatchDataResult> getMatchDataById(@ApiParam(required = true, name = "赛事id") @PathVariable("matchId") Long matchId, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据id获取赛事数据,未获取到登录用户信息，matchId={}", matchId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			MatchDataResult matchDataResult = leagueManager.queryMatchDataByMatchId(matchId);
			if (null == matchDataResult) {
				logger.info("根据id获取不到赛事数据详情，matchId={}", matchId);
				return CommonResponse.withErrorResp("未获取到赛事详情");
			}
			return CommonResponse.withSuccessResp(matchDataResult);
		} catch (Exception e) {
			logger.info("根据id获取赛事详情，发生异常，matchId={}，exception={}", matchId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@ApiOperation(value = "获取赛事结果接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/result/{matchId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<List<MatchDataInfoResul>> getMatchDataInfoResultById(@ApiParam(required = true, name = "赛事id") @PathVariable("matchId") Long matchId, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据id获取赛事结果,未获取到登录用户信息，matchId={}", matchId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			List<MatchDataInfoResul> list = leagueManager.queryMatchResultInfoByMatchId(matchId);
			if (null == list || list.isEmpty()) {
				logger.info("根据id获取不到赛事数据详情，matchId={}", matchId);
				return CommonResponse.withErrorResp("未获取到赛事详情");
			}
			return CommonResponse.withSuccessResp(list);
		} catch (Exception e) {
			logger.info("根据id获取赛事详情，发生异常，matchId={}，exception={}", matchId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@ApiOperation(value = "获取图文直播数据接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/live/{matchId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> getLiveUrl(@ApiParam(required = true, name = "赛事id") @PathVariable("matchId") Long matchId, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据id获取图文直播数据接口,未获取到登录用户信息，matchId={}", matchId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			String liveUrl = usrConsumerCacheManager.buildWSURL();
			return CommonResponse.withSuccessResp(liveUrl);
		} catch (Exception e) {
			logger.info("根据id获取图文直播数据接口,发生异常，matchId={}，exception={}", matchId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@ApiOperation(value = "获取赛事ID对应的局数ID", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/match/{matchId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<List<String>> getGameIdByMatchId(@ApiParam(required = true, name = "赛事id") @PathVariable("matchId") String matchId, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("获取赛事ID对应的局数ID，matchId={}", matchId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			String mapData = redisClient.get(MATCHID_GAMEID_CACHEKEY + matchId);
			Set<String> gameIdSet = new HashSet<>();
			if (StringUtils.isNotBlank(mapData)){
				try {
					gameIdSet = JSON.parseObject(mapData, HashSet.class);
					logger.info("获取赛事ID对应的局数ID,cacheKey：{},内容set:{}", MATCHID_GAMEID_CACHEKEY + matchId, JSONObject.toJSONString(gameIdSet));
				}catch (Exception e){
					logger.info("-getGameIdByMatchId- 准备更新缓存key：{}",MATCHID_GAMEID_CACHEKEY + matchId);
					gameIdSet=null;
				}
			}
			if (CollectionUtil.isEmpty(gameIdSet) && matchId.indexOf("_") > -1 && matchId.split("_").length > 1) {
				Set<String> resSet = leagueManager.queryGameByMatchId(Long.parseLong(matchId.split("_")[1]));
				redisClient.set(MATCHID_GAMEID_CACHEKEY + matchId,JSON.toJSONString(resSet),60*60*24);
				return CommonResponse.withSuccessResp(new ArrayList<>(resSet));
			}
			List<String> resList = new ArrayList<>(gameIdSet);
			Collections.sort(resList); // 默认升序
			return CommonResponse.withSuccessResp(resList);
		} catch (Exception e) {
			logger.info("获取赛事ID对应的局数ID,发生异常，matchId={}，exception={}", matchId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/getJsonFile", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取赛事json文本", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取赛事json文本", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> getJsonFile(String matchId, String gameId, String type, HttpServletRequest request) {
		try {
			if (StringUtils.isBlank(matchId) || StringUtils.isBlank(gameId)) {
				return CommonResponse.withErrorResp("赛事id或者局数id为空");
			}
			String fileContent = "", fileName = "";
			File file = null;
			if ("events_live".equals(type)) { // 事件类型文件
				fileName = MatchingDataWebSocketClient.EVENT_FILENAME_PREFIX + matchId + "_" + gameId;
				file = new File(MatchingDataWebSocketClient.FILEPATH_PREFIX + fileName);
			} else if ("match_live".equals(type)) { // 赛事类型文件
				fileName = MatchingDataWebSocketClient.MATCH_FILENAME_PREFIX + matchId + "_" + gameId;
				file = new File(MatchingDataWebSocketClient.FILEPATH_PREFIX + fileName);
			}
			fileContent = FileUtil.readUtf8String(file);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("fileContent", fileContent);
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.info("获取赛事json文本,发生异常,matchId={},gameId={},exception={}", matchId, gameId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/leagueDetail", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取联赛赛程详情", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取联赛赛程详情", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<SerieResultVo>> leagueDetail(MatchRequest matchDetailRequest) {
		if (ObjectUtil.isNull(matchDetailRequest) || matchDetailRequest.getLeagueId() == null || matchDetailRequest.getVideogameId() == null){
			logger.info("-leagueDetail-，参数异常：{}", JSON.toJSONString(matchDetailRequest));
			return CommonResponse.withErrorResp("参数异常");
		}
		return leagueManager.getLeagueDetail(matchDetailRequest);
	}

	@ApiOperation(value = "获取聊天室数据接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/room", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<MatchToolRoomResponse> getRoomUrl(HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("获取聊天室数据接口,未获取到登录用户信息，request={}" ,JSON.toJSONString(request));
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			MatchToolRoomResponse matchToolRoomResponse=new MatchToolRoomResponse();
			String roomUrl = usrConsumerCacheManager.buildWSURL();
			matchToolRoomResponse.setChatSocketUrl(roomUrl);
			matchToolRoomResponse.setUserId(userConsumer.getId());
			matchToolRoomResponse.setNickName(userConsumer.getNickName());
			matchToolRoomResponse.setIcon(userConsumer.getIcon());
			return CommonResponse.withSuccessResp(matchToolRoomResponse);
		} catch (Exception e) {
			logger.info("获取聊天室数据接口,发生异常，matchId={}，exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

}
