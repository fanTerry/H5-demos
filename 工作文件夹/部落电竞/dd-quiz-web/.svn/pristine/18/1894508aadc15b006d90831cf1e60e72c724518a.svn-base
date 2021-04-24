package com.esportzoo.esport.controller.ws.constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;

public final class NettyChannelRelatedMaps {

	public static final String MATCHID_GAMEID_CACHEKEY = "m_g_";

	/** 用户和通道id关联 */
	public static Map<String, ChannelId> usrIdAndChanIdMap = new ConcurrentHashMap<>();

	/** 通道id与通道关联 */
	public static Map<ChannelId, SocketChannel> channelMap = new ConcurrentHashMap<>();

	/**
	 * 场景和用户相关联 key为场景： 1、聊天场景下，key为"R_matchId" 2、赛事直播场景，key为"M_matchId"
	 * 3、赛事事件直播场景，key为"E_matchId" value 是用户id集合
	 */
	public static Map<String, CopyOnWriteArraySet<String>> sceneAndUsrIdMap = new ConcurrentHashMap<>();

}
