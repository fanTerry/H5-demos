package com.esportzoo.esport.controller.ws.client;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.controller.ws.constants.CommonMsg;
import com.esportzoo.esport.controller.ws.constants.NettyExecType;
import com.esportzoo.esport.controller.ws.constants.SceneType;
import com.esportzoo.leaguelib.client.service.prematch.MatchServiceClient;
import com.esportzoo.leaguelib.common.constants.MatchStatus;
import com.esportzoo.leaguelib.common.domain.Match;
import com.esportzoo.leaguelib.common.vo.postmatch.MatchQueryVo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.MATCHID_GAMEID_CACHEKEY;

/**
 * 获取雷达赛中实时数据 - socketclient
 * @author huanweili
 *
 */
public class MatchingDataWebSocketClient extends WebSocketClient implements ApplicationListener<ContextRefreshedEvent> {

	protected transient final Logger logger = LoggerFactory.getLogger("match");

	private RedisClient redisClient;

	private MatchServiceClient matchServiceClient;

	//private WsMsgLPushPublisher wsMsgLPushPublisher;

	private static ExecutorService executor = Executors.newFixedThreadPool(30);
	private final static String cacheKey = "cms:wsclent:matchskey02";
	public static final String EVENT_FILENAME_PREFIX = "event_live_";
	public static final String MATCH_FILENAME_PREFIX = "match_live_";
	public static final String FILEPATH_PREFIX = "/mfs/ShareFile/res/esport/upload/interface/matchdata/";
	private static final String SPLIT_TAG = "_&_";
	private static final String SCENEDATACACHE_PREFIX = "scene_";
	private static final int HISTORY_DATA_CACHETIME = 24 * 3600;
	private static final String LOCK_KEY = "match_";
	private static final int LOCK_TIME = 1000;

	public MatchingDataWebSocketClient(URI serverUri) {
		super(serverUri);
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		logger.info("------ MatchingDataWebSocket onOpen 建立连接------");
		//spring初始化时创建对象不完整(jmsTemplate为空)，故在此创建
		try {
			ServletContext sc = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
			/*if(wsMsgLPushPublisher == null) {
				wsMsgLPushPublisher = ac.getBean("wsMsgLPushPublisher", WsMsgLPushPublisher.class);
			}*/
			if(matchServiceClient == null) {
				matchServiceClient = ac.getBean("matchServiceClient", MatchServiceClient.class);
			}
		}catch (Exception e){
			logger.error("初始化wsMsgLPushPublisher和matchServiceClient对象出错，",e);
		}
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		logger.info("------ MatchingDataWebSocket onClose ------");
	}

	@Override
	public void onError(Exception ex) {
		logger.error("------ MatchingDataWebSocket onError ------", ex);
	}

	@Override
	public void onMessage(String data) {
		if (StringUtils.isBlank(data)) {
			logger.info("-------- MatchingDataWebSocket接收到数据为空");
			return;
		}
		JSONObject jsonObj = JSONObject.parseObject(data);
		logger.info("-------- MatchingDataWebSocket接收到数据为：{}", data);

		String type = jsonObj.getString("type");
		// 将赛事ID 去雷达数据方进行绑定，以便能监听到该赛事的实时数据
		if("init".equals(type)) {
			//获取比赛中的赛事ID
			Set<String> matchIdSet = getMatchIdByRunningStatus();
			bindMatchId2LeiDaData(jsonObj, matchIdSet);
			return;
		}
		//获取雷达返回的数据，并进行解析
		Object dataObject = jsonObj.get("data");
		JSONObject jsonM = null; String gameId = null;
		if(dataObject instanceof String) {
			String dataJOSNStr = (String)dataObject;
			JSONObject dataJson = JSON.parseObject(dataJOSNStr);
			jsonM = dataJson.getJSONObject("match");
			JSONObject gameObje = dataJson.getJSONObject("game");
			if(gameObje != null){
				gameId = gameObje.getLong("id") + "";
			}
			Integer currentTimestamp = dataJson.getInteger("current_timestamp");
			if (currentTimestamp != null && currentTimestamp < 0){
				dataJson.put("current_timestamp",0);
			}
			jsonObj.put("data",dataJson.toJSONString());
		}

		if(jsonM == null || gameId == null) {
			logger.info("MatchingDataWebSocket接收到数据，暂无赛事详细数据");
			return;
		}
		Long matchId = jsonM.getLong("id");
		logger.info("MatchingDataWebSocket接收到数据，matchId：{}",matchId);
		String matchType = "events_live".equals(type) ?  "E_" + matchId :  "M_" + matchId;
		if("event".equals(type) && "match end".equals(jsonObj.getString("event"))) { //TODO LOL赛事结束还未处理
			Match matchData = matchServiceClient.queryMatchByMatchId(matchId).getModel();
			String end_at = jsonM.getString("end_at");
			Date endDate = DateUtil.parse(end_at.replaceAll("T|Z", ""));
			if(matchData != null) {
				int updRet = matchServiceClient.updateMatchByStatus(matchData.getId().longValue(), MatchStatus.FINISHED, endDate).getModel();
				if(updRet > 0)
					logger.info("MatchingDataWebSocket接收到服务端数据，赛事matchId【{}】已结束，结束时间：【{}】，更改赛事状态为【已结束】", matchId, endDate);
			}
		}

		//缓存局数ID(gameId) 以便后续前端使用 LeagueController.getGameIdByMatchId(String, HttpServletRequest)接口使用
		HashMap<String, String> map = setGameIdWithCache(matchType, gameId);
		//组装数据，转发给前端，同时将数据写入到静态json文本文件内
		if("events_live".equals(type) || "matches_live".equals(type)) {
			JSONObject dataJSON = new JSONObject();
			int execType = "events_live".equals(type) ? NettyExecType.USR_EVENTING.getIndex():NettyExecType.USR_MATCHING.getIndex();
			logger.info("MatchingDataWebSocket接收到数据，并处理，类型{}，matchID：{}，gameId：{}", type,matchId,gameId);

			/** 组装返回给前端的内容 */
			dataJSON.put("execType", execType);
			dataJSON.put("playload", jsonObj.toJSONString());
			dataJSON.put("gameIds", JSON.toJSONString(map.keySet()));

			/** MQ 通知服务给发送赛事实时数据 */
			JSONObject msgObject = new JSONObject();
			msgObject.put("sceneType", SceneType.MATCHING.getIndex());
			msgObject.put("execType",execType);
			msgObject.put("matchType",matchType);
			msgObject.put("playload",dataJSON);
			CommonMsg comMsg = JSON.parseObject(msgObject.toJSONString(), CommonMsg.class);
			//wsMsgLPushPublisher.sendMessage(comMsg);

			//将数据写入到文件内
			writeData(matchType, dataJSON.toJSONString(), type, matchId, data, gameId);
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//关闭interface的webSocketClient
		/*new Thread(()->{
			try {
				redisClient = event.getApplicationContext().getBean("redisClusterClient", RedisClient.class);
				if(redisClient.tryGetLock(cacheKey,"1",3600)) {
					MatchingDataWebSocketClient client = event.getApplicationContext().getBean("matchingDataWebSocketClient", MatchingDataWebSocketClient.class);
					logger.info("------ MatchingDataWebSocket 开始创建ws-match 客户端 ------");
					client.connect(); //创建客户端连接
					synchronized (this) {
						this.wait();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();*/
	}

	public void resetStartFlag() {
		logger.info("--------删除redis中的ws-match存在信息------");
		redisClient.del(cacheKey);
	}


	//===================================================================================================private method
	/**
	 *  获取比赛中的赛事id
	 * @return
	 */
	private Set<String> getMatchIdByRunningStatus() {
		Set<String> matchIdSet = new HashSet<>();
		MatchQueryVo matchQueryVo = new MatchQueryVo();
		matchQueryVo.setStatus(MatchStatus.RUNNING.getIndex());
		ModelResult<List<Match>> matchListRet = matchServiceClient.queryMatchListByCondition(matchQueryVo);
		if (!matchListRet.isSuccess() || CollectionUtils.isEmpty(matchListRet.getModel())) {
			logger.info("-------- MatchingDataWebSocket接收到服务端数据,暂无比赛中的数据：{}", matchListRet.getModel());
			return matchIdSet;
		}
		List<Match> matchList = matchListRet.getModel();
		if (CollectionUtils.isNotEmpty(matchList)) {
			for (Match match : matchList) {
				matchIdSet.add(match.getMatchId() + "");
			}
		}
		return matchIdSet;
	}
	/**
	 * 将赛事ID 去雷达数据方进行绑定，以便能监听到该赛事的实时数据
	 * result : 1 正常、2 其中有参数为空、3 账号异常、4 比赛未开始
	 * @param jsonObj
	 * @param matchIdSet
	 */
	private void bindMatchId2LeiDaData(JSONObject jsonObj, Set<String> matchIdSet) {
			String client_id = jsonObj.getString("client_id");
			redisClient.set("M_CLIENTID", client_id);
			for (String matchId : matchIdSet) {
					executor.submit(()->{
							String result;
							try {
								String url = "http://egame.leidata.com/nesport/index.php/Api/gateway/bind?client_id="+ client_id +"&match_id=" + matchId + "&key=90a9b91b0a74627118efca9211ad94a3";
								result = HttpUtil.get(url);
								logger.info("MatchingDataWebSocket接收到服务端数据，请求/Api/gateway/bind进行绑定操作，请求url：{}, 返回结果：{}" , url, result);
							} catch (Exception e) {
								e.printStackTrace();
							}
					});
			}
	}

	/**
	 * 将赛事对应的局数gameId 缓存到redis进行记录，以便后续前端需要查询历史数据使用。
	 * @param sceneType
	 * @param gameId
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, String> setGameIdWithCache(String sceneType, String gameId) {
		String mapData = redisClient.get(MATCHID_GAMEID_CACHEKEY + sceneType);
		HashMap<String, String> map;
		if (StringUtils.isBlank(mapData)) {
			map = new HashMap<>();
			map.put(gameId, "1");
			redisClient.set(MATCHID_GAMEID_CACHEKEY + sceneType, JSON.toJSONString(map));
		} else {
			map = JSON.parseObject(mapData, HashMap.class);
			map.put(gameId, "1");
			redisClient.set(MATCHID_GAMEID_CACHEKEY + sceneType, JSON.toJSONString(map));
		}
		return map;
	}

	private void writeData(String matchType, String  dataStr, String type, Long matchId, String data, String gameId) {
		/** 判断数据是否已写入 */
		String lock_value = UUID.randomUUID().toString();
		//标志数据是否已写入
		Boolean flag = false;
		try {
			if (redisClient.tryGetLock(LOCK_KEY + gameId, lock_value, LOCK_TIME)){
				String lruMapStr = redisClient.get(SCENEDATACACHE_PREFIX + matchType);
				if (StringUtils.isNotBlank(lruMapStr)) {
					if (lruMapStr.contains(dataStr)) {
						flag = true;
					}
				}
			}
		}finally {
			redisClient.releaseLock(LOCK_KEY+gameId,lock_value);
		}
		//缓存历史数据（只存最近的50条）
		LinkedHashMap<String, String> lruMap = new LinkedHashMap<String, String>(50, 0.75f, true) {
			private static final long serialVersionUID = 1581412309406311948L;
			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<String, String> eldest) {
				if (size() > 50) {
					return true;
				}
				return false;
			}
		};
		int i = 0; lruMap.put(matchType + (i++), dataStr);
		if(lruMap.size() > 0) {
			redisClient.set(SCENEDATACACHE_PREFIX + matchType, JSON.toJSONString(lruMap.values()), HISTORY_DATA_CACHETIME);
		}

		if (flag){	//如果数据已写入
			logger.info("MatchingDataWebSocket接收数据已持久化：{}，不需写入文件。",type+"_"+matchId);
			return;
		}

		/** 写入文件 */
		try {
			if ("events_live".equals(type)) { // 事件类型文件
				String fileName = EVENT_FILENAME_PREFIX + matchId + "_" + gameId;
				File file = new File(FILEPATH_PREFIX + fileName);
				data = data + SPLIT_TAG;
				FileUtil.appendUtf8String(data, file);
				logger.info("MatchingDataWebSocket接收数据，持久化{}类型，matchId：{}",type,matchId);
			} else { // 赛事类型文件
				String fileName = MATCH_FILENAME_PREFIX + matchId + "_" + gameId;
				File file = new File(FILEPATH_PREFIX + fileName);
				FileUtil.writeUtf8String(data, file);
				logger.info("MatchingDataWebSocket接收数据，持久化{}类型，matchId：{}",type,matchId);
			}
		} catch (Exception ex) {
			logger.error("MatchingDataWebSocket接收数据，解析并进行持久化异常", ex);
		}
	}
	
}