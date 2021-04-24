package com.esportzoo.esport.controller.ws.server.scene;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.cms.CmsBbsServiceClient;
import com.esportzoo.esport.client.service.cms.CmsFilterKeywordServiceClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.constant.CachedKeyAndTimeLong;
import com.esportzoo.esport.constants.UserType;
import com.esportzoo.esport.controller.ws.constants.CommonMsg;
import com.esportzoo.esport.controller.ws.constants.ExecResult;
import com.esportzoo.esport.controller.ws.constants.NettyExecType;
import com.esportzoo.esport.controller.ws.constants.SceneType;
import com.esportzoo.esport.controller.ws.jms.WsMsgLPushPublisher;
import com.esportzoo.esport.domain.CmsBbs;
import com.esportzoo.esport.util.SensitiveWordUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.*;

/**
 *  房间场景(聊天)数据收发处理类
 * @author huanwei.li
 *
 */
//@Component("roomSceneHandler")
public class RoomSceneHandler extends CommonSceneHandler{

	private static final Logger logger = LoggerFactory.getLogger(RoomSceneHandler.class);
	
	/*@Autowired
	private MemcachedClient memcachedClient ;*/
	
	@Autowired
	private RedisClient redisClient;
	
    private static final String FORMAT_CHAT_MSG = "%s：%s";
    
    private static final String CHAT_CONTENT_COPY_PREFIX = "esport_room_chat_content_";

    @Autowired
    private WsMsgLPushPublisher wsMsgLPushPublisher;
    
    @Autowired
    private UserConsumerServiceClient usrConsumerServiceClient;
   
    @Autowired
    private CmsBbsServiceClient cmsBbsService;

    @Autowired
    private CmsFilterKeywordServiceClient cmsFilterKeywordService;
    
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;
    
    @Autowired
    private TaskExecutor taskExecutor;
    
    private static final String room_scene_usrid_map_cache ="app_sceneId_usrids_cache";
         
    /**
     * 用户进入房间场景
     */
	@Override
	public void inMap(String scenekey, String usrId, JSONObject msgObj) {
		int execType = msgObj.getIntValue("execType");
		String playload = msgObj.getString("playload");
		JSONObject bodyObj = JSONObject.parseObject(playload);
		if(NettyExecType.USR_REGIST_ROOM.getIndex() == execType) {
			String roomId = bodyObj.getString("sceneId");
			//用户与单个房间关联
			if(CollectionUtils.isEmpty(sceneAndUsrIdMap.get(roomId))){
				CopyOnWriteArraySet<String> usrIdSet = new CopyOnWriteArraySet<>();
				usrIdSet.add(usrId);
				sceneAndUsrIdMap.put(roomId, usrIdSet);
			} else {
				CopyOnWriteArraySet<String> usrIdSet = sceneAndUsrIdMap.get(roomId);
				usrIdSet.add(usrId);
			}
			
			logger.info("用户【usrId:{}】进入【房间场景】，sceneAndUsrIdMap===>{},usrIdAndChanIdMap==>{}", usrId, sceneAndUsrIdMap, usrIdAndChanIdMap);
			//对于部署了多台netty服务，则将本地的场景中对应的usrId们汇总到Memcache中。
			putSceneAndUsrIdToMemcache(roomId);
			//聊天信息发送
			taskExecutor.execute(new Runnable(){
				@Override
				public void run() {
					//将房间内之前的聊天内容发送给当前用户
					executeSendMsgToInitUsrInRoom(bodyObj, usrId, roomId);
				}
			});
		}
	}
	
	
	@Override
	public ExecResult executeData(String msgJson) {
		JSONObject msgObj = JSONObject.parseObject(msgJson);
		int sceneType = msgObj.getIntValue("sceneType");
		int execType = msgObj.getIntValue("execType");
		if (sceneType == SceneType.ROOM.getIndex()) {
			if (execType == NettyExecType.USR_CHAT.getIndex()) {
				//暂时关闭房间聊天发送
//				logger.info("---------doChat-暂时关闭房间聊天发送---------");
//				ExecResult resp = new ExecResult("9999", "暂时关闭房间聊天发送");
//				return resp;
				if (doChat(msgObj)) {
					logger.info("doChat-发送聊天消息成功success,推送内容为【{}】", msgJson);
					ExecResult resp = new ExecResult("0000", "消息推送成功");
					return resp;
				} else {
					logger.info("doChat-发送聊天消息失败fail,推送内容为【{}】", msgJson);
					ExecResult resp = new ExecResult("9999", "消息推送失败");
					return resp;
				}
			} 
		}else{
			logger.error("RoomSceneHandler.executeDate执行异常，推送的当前场景【{}】非ROOM场景",sceneType);
		}
		ExecResult resp = new ExecResult("9999","消息推送失败");
		return resp;
	}
	
	public boolean doChat(JSONObject msgObj) {
		String playload = msgObj.getString("playload");
		JSONObject bodyObj = JSONObject.parseObject(playload);
		String roomId = bodyObj.getString("sceneId");
		String sendMsg = bodyObj.getString("sendMsg");
		if(StringUtils.isNotBlank(sendMsg)){
			sendMsg = filterIllegalMsg(sendMsg);
		}else{
			logger.error("RoomSceneHandler.doChat聊天发送内容为空，chat.sendmsg.is.null");
			return false;
		}
		String usrId = bodyObj.getString("usrId");
		// 敏感词过滤
		sendMsg = sensitiveWordUtil.replaceSensitiveWord(sendMsg, "*");

		String nickName = bodyObj.getString("nickName");
		bodyObj.put("sendMsg", sendMsg);
		msgObj.put("playload", bodyObj);
		CmsBbs cmsBbs = new CmsBbs();
		cmsBbs.setContent(sendMsg);
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				//异步记录聊天数据
				cmsBbs.setCreateTime(new Date());
				cmsBbs.setRelatedId(Long.valueOf(roomId.split("_")[1]));
				cmsBbs.setRelatedType(1);
				cmsBbs.setStatus(1);
				cmsBbs.setUserId(Long.valueOf(usrId));
				cmsBbs.setUserName(nickName);
				cmsBbs.setUserType(UserType.GENERAL_USR.getIndex());
				cmsBbsService.addCmsBbs(cmsBbs);
				
				
				//MQ 通知另一台服务，给当前房间下的所以用户发送该条信息
				CommonMsg comMsg = JSON.parseObject(msgObj.toJSONString(), CommonMsg.class);
				//wsMsgLPushPublisher.sendMessage(comMsg);
				
				// 异步存储用户聊天内容 在cache中
				String key = CHAT_CONTENT_COPY_PREFIX + roomId;
				List<String> msgByRoomArr = redisClient.getObj(key);
				String createTime = DateUtil.formatDate(new Date());
				bodyObj.put("createTime", createTime);
				ChannelId channelId = usrIdAndChanIdMap.get(usrId);
				if(!Objects.isNull(channelId) && !Objects.isNull(channelMap.get(channelId))){
					bodyObj.put("remark", channelMap.get(channelId).remoteAddress());
				}
				if (CollectionUtils.isEmpty(msgByRoomArr)) {
					msgByRoomArr = new ArrayList<>();
					msgByRoomArr.add(bodyObj.toString());
				} else {
					msgByRoomArr.add(bodyObj.toString());
				}
				redisClient.setObj(key, CachedKeyAndTimeLong.setHour(24), msgByRoomArr);
			}
    	});
		return sendChatMsgToUserWithRoom(roomId, nickName, sendMsg, Long.valueOf(usrId));
	}
	
	
	/**
     * @Title: sendMessageToUser
     * @Description: 发送消息给房间下的所有终端
     * @param @param topic 房间id
     * @param @param message 发送的消息
     * @param @return 发送成功返回true，反则返回false
     */
	public Boolean  sendChatMsgToUserWithRoom(String roomId, String nickName, String sendMsg, Long currUsrId) {
		if (sceneAndUsrIdMap.containsKey(roomId)) {
			String retMsgJson = buildReturnMsg(roomId, nickName, sendMsg, currUsrId);
			CopyOnWriteArraySet<String> roomIdset= sceneAndUsrIdMap.get(roomId);
			logger.info("sendChatMsgToUserWithRoom-当前netty服务sceneAndUsrIdMap===>{}",sceneAndUsrIdMap);
            for (String usrId : roomIdset) {
            	if(StringUtils.isBlank(usrId)) continue;
            	ChannelId channelId = usrIdAndChanIdMap.get(usrId);
            	if(Objects.isNull(channelId)){
        			logger.error("sendChatMsgToUserWithRoom-用户id[{}],没有找到对应的channel通道",usrId);
        		}else{
        			logger.info("sendChatMsgToUserWithRoom-发送给用户id[{}],对应的channel通道【{}】,当前netty服务usrIdAndChanIdMap===>{}",usrId,channelId,usrIdAndChanIdMap);
        			Channel channel =channelMap.get(channelId);
                	if(!Objects.isNull(channel)){
                		channel.writeAndFlush(new TextWebSocketFrame(retMsgJson));
                	}
        		}
            }
            return true;
        }
        logger.info("sendChatMsgToUserWithRoom-发送错误：当前环境不包含roomId为：{}的房间,sceneAndUsrIdMap===>{}", roomId, sceneAndUsrIdMap);
        return false;
	}
 
	/**
     * @Title: sendMessageToUser
     * @Description:  发送消息给应用下的所有房间
     * @param message 发送的消息
     */
	public void pushMsgToAll(String message) {
		for (SocketChannel channel : channelMap.values()) {
			if (!Objects.isNull(channel))
				channel.writeAndFlush(new TextWebSocketFrame(message));
		}
	}
	/**
	 * 实时发送房间内相关变化数据
	 * （如：热抓用户，预约人数以及房间弹幕）
	 * @param roomId
	 * @param msgJson
	 */
	public boolean sendRoomInfoWihtRealTime(String roomId, String msgJson, Integer execType){
		String sceneKey = roomId.contains("R_") ? roomId : "R_"+roomId;
		String execTypeDesc = NettyExecType.valueOf(execType).getDescription();
		logger.info("sendRoomInfoWihtRealTime-【{}】房间Id【{}】，发送信息msgJson【{}】，当前netty服务sceneAndUsrIdMap===>{}",execTypeDesc, sceneKey, msgJson, sceneAndUsrIdMap);
		if (sceneAndUsrIdMap.containsKey(sceneKey)) {
			CopyOnWriteArraySet<String> roomIdset= sceneAndUsrIdMap.get(sceneKey);
            for (String usrId : roomIdset) {
            	if(StringUtils.isBlank(usrId)) continue;
            	ChannelId channelId = usrIdAndChanIdMap.get(usrId);
            	if(Objects.isNull(channelId)){
        			logger.error("sendRoomInfoWihtRealTime-用户id[{}],没有找到对应的channel通道",usrId);
        		}else{
        			Channel channel = channelMap.get(channelId);
        			logger.info("sendRoomInfoWihtRealTime-发送给用户id[{}],对应的channel通道【{}】,发送数据msgJson：{},当前netty服务usrIdAndChanIdMap===>{}",usrId,channelId,msgJson,usrIdAndChanIdMap);
                	if(!Objects.isNull(channel)){
                		channel.writeAndFlush(new TextWebSocketFrame(msgJson));
                	}
        		}
            }
            return true;
        }
        logger.info("sendRoomInfoWihtRealTime-发送错误：【当前netty-server服务机器】不包含roomId为：{}的房间,sceneAndUsrIdMap===>{}",sceneKey, sceneAndUsrIdMap);
		return false;
	}
	
	public void putSceneAndUsrIdToMemcache(String roomId){
		Map<String,CopyOnWriteArraySet<String>> cacheMap = redisClient.getObj(room_scene_usrid_map_cache);
		//TODO 并发情况下对围观人家要求实时准确，则要考虑  memcache 锁
		if(cacheMap != null && cacheMap.size() != 0){
			CopyOnWriteArraySet<String> usrIdCacheSet = cacheMap.get(roomId);
			CopyOnWriteArraySet<String> currUsrIdSet = sceneAndUsrIdMap.get(roomId); //当前进程中的usrIdSet数据
			if(CollectionUtils.isEmpty(usrIdCacheSet)){
				cacheMap.put(roomId, currUsrIdSet);
			}else{
				usrIdCacheSet.addAll(currUsrIdSet);
			}
			redisClient.setObj(room_scene_usrid_map_cache, CachedKeyAndTimeLong.setHour(24), cacheMap);
		}else{
			redisClient.setObj(room_scene_usrid_map_cache, CachedKeyAndTimeLong.setHour(24), sceneAndUsrIdMap);
		}
	}
	
	public void removeSceneAndUsrIdToMemcache(String roomId, String delUsrId){
		Map<String,CopyOnWriteArraySet<String>> cacheMap = redisClient.getObj(room_scene_usrid_map_cache);
		if(cacheMap != null && cacheMap.size() != 0){
			CopyOnWriteArraySet<String> usrIdCacheSet = cacheMap.get(roomId);
			if(!CollectionUtils.isEmpty(usrIdCacheSet)){
				usrIdCacheSet.remove(delUsrId);
			}
			redisClient.setObj(room_scene_usrid_map_cache, CachedKeyAndTimeLong.setHour(24), cacheMap);
		}
	}
	
	@PreDestroy
	public void destroyCleanRoomUsridMapCache(){
		logger.info("----netty服务关闭时，清理缓存中关于房间内的围观信息----");
		redisClient.del(room_scene_usrid_map_cache);//netty服务关闭时，清理缓存中关于房间内的围观信息
	}
	
	private String buildReturnMsg(String roomId, String nickName, String sendMsg, Long usrId){
		String formatMsg = String.format(FORMAT_CHAT_MSG, nickName, sendMsg);
		logger.info("给房间id为：{}的所有用户发送消息：{}", roomId, formatMsg);
		//构造返回消息
		JSONObject msgJson = new JSONObject();
		msgJson.put("execType", NettyExecType.USR_CHAT.getIndex());
		JSONObject playload = new JSONObject();
		playload.put("sendMsg", sendMsg);
		playload.put("nickName", nickName);
		playload.put("usrId", usrId);
		msgJson.put("playload", playload);
		return msgJson.toJSONString();
	}

	private void executeSendMsgToInitUsrInRoom(JSONObject bodyObj, String usrId, String roomId) {
		//用户进入房间，触发了连接聊天后，将该房间之前的消息发送给改用户
        String key = CHAT_CONTENT_COPY_PREFIX + roomId;
        List<String> msgByRoomArr = redisClient.getObj(key);
        if(CollectionUtils.isNotEmpty(msgByRoomArr)){
        	int size = msgByRoomArr.size();
            msgByRoomArr = size > 5 ? msgByRoomArr.subList(size-5, size) : msgByRoomArr.subList(1, size);
        	for (String msg : msgByRoomArr) {
        		JSONObject json = JSONObject.parseObject(msg);
        		String retMsgJson = this.buildReturnMsgByCache(json, usrId);
        		ChannelId channelId = usrIdAndChanIdMap.get(usrId);
        		if(Objects.isNull(channelId)){
        			logger.error("executeSendMsgToInitUsrInRoom-用户id[{}],没有找到对应的channel通道",usrId);
        		} else {
        			Channel currChannel = channelMap.get(channelId);
            		currChannel.writeAndFlush(new TextWebSocketFrame(retMsgJson));
        		}
			}
        } else {
        	List<String> metaRoomArr = new ArrayList<>();
        	String metaMsg = this.buildMetaRoomMsg(bodyObj);
        	metaRoomArr.add(metaMsg);
        	redisClient.setObj(key, CachedKeyAndTimeLong.setHour(24), metaRoomArr);
        }
	}
	
	private String buildReturnMsgByCache(JSONObject json, String usrId){
		String nickName = json.getString("nickName");
		String sendMsg = json.getString("sendMsg");
		JSONObject retMsgJson = new JSONObject();
		retMsgJson.put("execType", NettyExecType.USR_CHAT.getIndex());
		JSONObject loadJson = new JSONObject();
		loadJson.put("sendMsg", sendMsg);
		loadJson.put("nickName", nickName);
		loadJson.put("usrId", usrId);
		retMsgJson.put("playload", loadJson);
		return retMsgJson.toJSONString();
	}
	
	private String buildMetaRoomMsg(JSONObject bodyObj){
		String topicId = bodyObj.getString("topicId");
		String no = bodyObj.getString("no");
    	String metaMsg = topicId + "_"+ no;
    	return metaMsg;
	}
	
	public static String filterIllegalMsg(String sendMsg){
		String reg  = "[零,一,二,三,四,五,六,七,八,九,十,壹,贰,叁,肆,伍,陆,柒,捌,玖,拾]{5,6}";
		sendMsg = sendMsg.replaceAll("\\s|\t|\r|\n", "") 
						 .replaceAll("[^\u4e00-\u9fa5]{1,}", "")
						 .replaceAll(reg, "*");
		if("".equals(sendMsg)) sendMsg = "***";
		return sendMsg;
	}

}
