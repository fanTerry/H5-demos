package com.esportzoo.esport.controller.ws.server.scene;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.controller.ws.constants.ExecResult;
import com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.*;
/**
 * 默认提供 usrId与通道id（ChannelId）的相关管理api实现
 * 后续可根据自行需求进行重写
 * @author huanwei.li
 *
 */
//@Component
public class CommonSceneHandler implements SceneHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(CommonSceneHandler.class);
	
	@Autowired
	private RedisClient redisClient;
	private static final String SCENEDATACACHE_PREFIX = "scene_";
	
	@Override
	public void inMap(String scenekey, String usrId) {
		inMap(scenekey, usrId, null);
	}

	@Override
	public void outMap(String scenekey, String usrId) {
		outMap(scenekey, usrId, null);
	}

	@Override
	public Set<String> getMap(String scenekey) {
		return sceneAndUsrIdMap.get(scenekey);
	}

	@Override
	public SocketChannel getSocketChannel(String usrId) {
		ChannelId channelId = usrIdAndChanIdMap.get(usrId);
		if(!Objects.isNull(channelId)){
			return channelMap.get(channelId);
		}
		return null;
	}
 
	@Override
	public void inMap(String scenekey, String usrId, JSONObject msgObj) {
		CopyOnWriteArraySet<String> usrIdSet = sceneAndUsrIdMap.get(scenekey);
		if(CollectionUtils.isEmpty(usrIdSet)){
			usrIdSet = new CopyOnWriteArraySet<>();
			usrIdSet.add(usrId);
			sceneAndUsrIdMap.put(scenekey, usrIdSet);
		}else{
			usrIdSet.add(usrId);
		}
		logger.info("用户进入场景，当前场景数据：{}", sceneAndUsrIdMap);
		String sceneDataCache = SCENEDATACACHE_PREFIX + scenekey;
		String dataStr = redisClient.get(sceneDataCache);
		if(StringUtils.isBlank(dataStr)) {
			logger.info("推送赛中数据，当前【{}】对应的数据为空：{}", scenekey, dataStr);
			return;
		}
		
		Collection<String> dataList = JSON.parseObject(dataStr, Collection.class);
		ChannelId channelId = usrIdAndChanIdMap.get(usrId);
		if (channelId == null) {
			logger.info("推送赛中数据，当前用户【{}】没用对应的通道channelId", usrId, channelId);
		}
		SocketChannel channel = channelMap.get(channelId);
		for (String data : dataList) {
			channel.writeAndFlush(new TextWebSocketFrame(data));
			logger.info("进入赛事房间-推送历史数据，当前用户【{}】- sceneAndUsrIdMap-{}, 推送数据为：{}", usrId, sceneAndUsrIdMap, data);
		}
	}

	@Override
	public void outMap(String scenekey, String usrId, JSONObject msgObj) {
		CopyOnWriteArraySet<String> usrIdSet = sceneAndUsrIdMap.get(scenekey);
		if(CollectionUtils.isNotEmpty(usrIdSet)){
			usrIdSet.remove(usrId);
		}
	}

	@Override
	public ExecResult executeData(String msgJson) {
		JSONObject msgObj = JSONObject.parseObject(msgJson);
		String playload = msgObj.getString("playload");
		JSONObject bodyObj = JSONObject.parseObject(playload);
		String sceneId = bodyObj.getString("sceneId");
		Integer execType = bodyObj.getInteger("execType");
		if(Objects.isNull(execType)){
			ExecResult ret = new ExecResult("9999","execType.is.null");
            return ret;
		}
		if (sceneAndUsrIdMap.containsKey(sceneId)) {
			CopyOnWriteArraySet<String>  usrIdSet = sceneAndUsrIdMap.get(sceneId);
            for (String usrId : usrIdSet) {
            	if(StringUtils.isBlank(usrId)) continue;
            	ChannelId channelId = usrIdAndChanIdMap.get(usrId);
            	if(Objects.isNull(channelId)){
            		logger.error("用户usrId【{}】,没有找到对应的通道,usrIdAndChanIdMap==>{}", usrId, channelId);
            		ExecResult ret = new ExecResult("9999","推送失败");
            		return ret;
            	}
            	Channel channel =channelMap.get(channelId);
            	if(!Objects.isNull(channel)){
            		channel.writeAndFlush(new TextWebSocketFrame(playload));
            	}
            }
            ExecResult ret = new ExecResult("0000","推送成功");
            return ret;
        }
        logger.error("executeData执行异常：当前连接不包含该场景：【{}】",sceneId);
        ExecResult ret = new ExecResult("9999","推送失败");
        return ret;
	}

	/**
	 * 推送比赛实时数据
	 * @param matchType
	 * @param playload
	 */
	public void sendMatchesMsg(String matchType, String playload) {
		Set<Map.Entry<String, CopyOnWriteArraySet<String>>> kvSet = NettyChannelRelatedMaps.sceneAndUsrIdMap.entrySet();
		if (kvSet == null) {
			logger.info("sceneAndUsrIdMap为空，不进行操作");
			return;
		}
		for (Map.Entry<String, CopyOnWriteArraySet<String>> kv : kvSet) {
			if (ObjectUtil.isNull(kv)) {
				continue;
			}
			String map_key = kv.getKey();
			logger.info("sceneType：{},sceneAndUsrIdMap的key：{}", matchType, map_key);
			if (matchType.equals(map_key)) {
				CopyOnWriteArraySet<String> cowSet = kv.getValue();
				if (CollectionUtils.isEmpty(cowSet)) {
					logger.info("推送赛中数据，当前【{}】对应的usrIdSet 为空", map_key);
					continue;
				}
				for (String usrId : cowSet) {
					ChannelId channelId = NettyChannelRelatedMaps.usrIdAndChanIdMap.get(usrId);
					if (channelId == null) {
						logger.info("推送赛中数据，当前用户【{}】没用对应的通道channelId", usrId, channelId);
						continue;
					}
					SocketChannel channel = NettyChannelRelatedMaps.channelMap.get(channelId);
					if (channel == null) {
						logger.info("channelId：{}，对应的channle为空", channelId);
						continue;
					}
					channel.writeAndFlush(new TextWebSocketFrame(playload));
					logger.info("推送赛中数据，当前用户【{}】- sceneAndUsrIdMap-{}，推送类型：{}", usrId, kv, map_key);
				}
				break;
			}
		}
	}

}
