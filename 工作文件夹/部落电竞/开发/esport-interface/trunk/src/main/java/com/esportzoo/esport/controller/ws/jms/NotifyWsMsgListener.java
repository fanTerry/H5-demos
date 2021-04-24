package com.esportzoo.esport.controller.ws.jms;

///已迁移到msgcenter
//@Service
public class NotifyWsMsgListener  { //implements MessageListener
	/*private Logger logger = LoggerFactory.getLogger("jmsMessage");

	@Autowired
	private RoomSceneHandler roomSceneHandler;

	@Autowired
	private CommonSceneHandler commonSceneHandler;

	@Override
	public void onMessage(Message message) {
		CommonMsg comMsg = null;
		if (message instanceof ObjectMessage) {
			try {
				comMsg = (CommonMsg) ((ObjectMessage) message).getObject();
			} catch (JMSException e) {
				logger.info("NotifyWsMsgListener-收到MQ推送消息通知，onMessage获得jms消息出错!", e);
			}
		}
		if (comMsg == null) {
			logger.info("NotifyWsMsgListener-收到MQ推送消息通知，onMessage获得jms消息转换后 CommonMsg为空!");
			return;
		}

		Integer execType = comMsg.getExecType();
		if (execType != null){
			*//** 处理聊天 *//*
			if (execType.equals(NettyExecType.USR_CHAT.getIndex())){
				logger.info("NotifyWsMsgListener-收到MQ推送消息通知,消息类型：【{}】,推送信息为：{}", NettyExecType.valueOf(execType).getDescription(), comMsg);
				Integer sceneType = comMsg.getSceneType();
				String usrId = comMsg.getPlayload("usrId", String.class);
				String sceneId = comMsg.getPlayload("sceneId", String.class);
				if (sceneType != null && sceneType == SceneType.ROOM.getIndex()) {
					if (execType == NettyExecType.USR_CHAT.getIndex()) {
						CopyOnWriteArraySet<String> usrIdSet = NettyChannelRelatedMaps.sceneAndUsrIdMap.get(sceneId);
						if (CollectionUtils.isEmpty(usrIdSet) || usrIdSet.contains(usrId)) {
							logger.info("收到MQ推送消息通知未处理，当前房间用户为空，或当前MQ不需处理聊天信息");
							return;
						}
						String nickName = comMsg.getPlayload("nickName", String.class);
						String sendMsg = comMsg.getPlayload("sendMsg", String.class);
						roomSceneHandler.sendChatMsgToUserWithRoom(sceneId, nickName, sendMsg, Long.valueOf(usrId));
					}
				}
			}

			*//** 处理赛事 *//*
			if (execType == NettyExecType.USR_MATCHING.getIndex() || execType == NettyExecType.USR_EVENTING.getIndex()){
				String matchType = comMsg.getMatchType();
				logger.info("MQ接收到赛事数据，赛事类型：{}",matchType);
				String playload = comMsg.getPlayload();
				if (playload != null) {
					logger.info("MQ接收到赛事数据，准备推送赛中数据");
					commonSceneHandler.sendMatchesMsg(matchType, playload);
				}
			}
		}else {
			logger.info("NotifyWsMsgListener-收到MQ推送消息通知，execType为空!");
		}
	}*/
}