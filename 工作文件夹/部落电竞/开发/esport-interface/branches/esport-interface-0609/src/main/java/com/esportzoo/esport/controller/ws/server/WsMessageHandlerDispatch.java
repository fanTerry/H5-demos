package com.esportzoo.esport.controller.ws.server;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.constant.CachedKeyAndTimeLong;
import com.esportzoo.esport.controller.ws.constants.CmdType;
import com.esportzoo.esport.controller.ws.constants.SceneType;
import com.esportzoo.esport.controller.ws.server.scene.CommonSceneHandler;
import com.esportzoo.esport.controller.ws.server.scene.RoomSceneHandler;
import com.esportzoo.esport.controller.ws.server.scene.SceneHandler;
import com.esportzoo.esport.service.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import java.util.Map;
import java.util.Objects;
/**
 * 
 * @author huanwei.li
 *
 */
//@Component
public class WsMessageHandlerDispatch {
	
	private static final Logger logger = LoggerFactory.getLogger(WsMessageHandlerDispatch.class);
	
	//@Resource
	private Map<Integer, CommonSceneHandler> sceneSelector;
	
	/*@Autowired 
	private MemcachedClient memcachedClient;*/
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired 
	private TaskExecutor taskExecutor;
	
	@Autowired 
	private RoomSceneHandler roomSceneHandler;
	
	private final static String PRESCENEKEY_PREFIX = "prescene_";

	public SceneHandler changeSceneHanderDispatch(String msgJson) {
		JSONObject msgObj = JSONObject.parseObject(msgJson);
		int sceneType = msgObj.getIntValue("sceneType");
		Integer cmd = msgObj.getInteger("cmdType");
		//根据执行类型，找到对应hanler处理类
		SceneHandler sceneHandler = sceneSelector.get(sceneType);
		logger.info("changeSceneHanderDispatch-begin-->接收到发送信息，msgJson：{}",msgJson);
		if(Objects.isNull(cmd)) return sceneHandler;
		String playload = msgObj.getString("playload");
		JSONObject bodyObj = JSONObject.parseObject(playload);
		String usrId = bodyObj.getString("usrId");
		String sceneId = bodyObj.getString("sceneId");
		if(sceneHandler == null){
			logger.error("根据sceneType【{}】没用找到对应的sceneHandler,【{}】场景", 
					sceneType, SceneType.valueOf(sceneType).getDescription());
			throw new BusinessException("没用找到对应的sceneHandler");
		}
		if(StringUtils.isBlank(usrId)){
			logger.error("changeSceneHanderDispatch-与用户进行场景关联时，usrId不能为空，接收参数msgJson:{}",msgJson);
			throw new BusinessException("usrId为空，无法进行场景关联");
		}
		logger.info("changeSceneHanderDispatch-begin-->接收到发送信息，msgJson：{}",msgJson);
		if(!Objects.isNull(cmd)){//对应场景的指令动作可根据后续业务执行扩展 cmdType
			if(cmd == CmdType.in.getIndex()) {
				sceneHandler.inMap(sceneId, usrId, msgObj);
				//每进入一个场景，移除用户之前的场景
				String preSceneId = redisClient.getObj(PRESCENEKEY_PREFIX + usrId);
				logger.info("changeSceneHanderDispatch-用户【{}】已进入【{}】场景,上一个场景是：【{}】，msgJson[{}]",usrId, sceneId, preSceneId, msgJson);
				if(StringUtils.isNotBlank(preSceneId) && !preSceneId.equals(sceneId)){
					sceneHandler.outMap(preSceneId, usrId);
					roomSceneHandler.removeSceneAndUsrIdToMemcache(preSceneId, usrId);
				}
				redisClient.setObj(PRESCENEKEY_PREFIX + usrId, CachedKeyAndTimeLong.setHour(24), sceneId);
			}
			//if(cmd == CmdType.out.getIndex()) {
			//	sceneHandler.outMap(buildSceneKey(sceneType, sceneId), usrId);
			//}
		}
		return sceneHandler;
		//TODO 后续可以扩展一些其他场景动作
	}
	
	public Integer convertSceneTypeBySceneId(String sceneId){
		String flag = sceneId.split("_")[0];
		if("R".equals(flag)){
			return SceneType.ROOM.getIndex();
		}
		if("H".equals(flag)){
			return SceneType.HOME.getIndex();
		}
		return null;
	}
	
	
	
 
}
