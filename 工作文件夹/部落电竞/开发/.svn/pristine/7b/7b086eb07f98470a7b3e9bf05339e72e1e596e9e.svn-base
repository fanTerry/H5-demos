package com.esportzoo.esport.controller.ws.jms;

import com.esportzoo.esport.controller.ws.client.LoginWebSocketServerHandler;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Objects;

import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.channelMap;
import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.usrIdAndChanIdMap;

@Service("loginWebListener")
public class LoginWebListener implements MessageListener {
	private Logger logger = LoggerFactory.getLogger("jmsMessage");
	@Autowired
	private LoginWebSocketServerHandler loginWebSocketServerHandler;
	
	@Override
	public void onMessage(Message message) {
		String comMsg = null;
		//logger.info("loginMQ接收到的信息：{}", JSON.toJSONString(message));
		try {
			String sceneStr=message.getStringProperty("sceneStr");
			String userConsumer=message.getStringProperty("userConsumer");
			ChannelId channelId = usrIdAndChanIdMap.get(sceneStr);
			if (!Objects.isNull(channelId)){
				SocketChannel socketChannel = channelMap.get(channelId);
				if(Objects.isNull(socketChannel)){
					logger.error("loginMQ拒绝发送扫码登录信息,channelId为:{}",channelId);
				}else {
					loginWebSocketServerHandler.pushSingleBysceneStr(sceneStr, userConsumer);
				}
			}else {
				logger.error("loginMQ拒绝发送扫码登录信息,channelId为:{}",channelId);
			}
		} catch (Exception e) {
			logger.info("loginMQ监听消息异常", e);
		}
	}
}