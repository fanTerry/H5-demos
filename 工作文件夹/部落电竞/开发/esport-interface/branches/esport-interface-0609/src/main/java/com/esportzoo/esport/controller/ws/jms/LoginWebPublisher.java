package com.esportzoo.esport.controller.ws.jms;

import org.springframework.jms.core.MessageCreator;

import javax.jms.*;


public class LoginWebPublisher extends MessageProducer {

	private Destination queue;

	public void sendMessage(String sceneStr, String userConsumer) {
		logger.info("loginMQ发送的内容：{},{}", sceneStr, userConsumer);
		try {
			jmsTemplate.send(queue, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message = null;
					try {
						message = session.createMessage();
						message.setStringProperty("sceneStr", sceneStr);
						message.setStringProperty("userConsumer", userConsumer);
					} catch (Exception e) {
						logger.error("loginMQ发消息异常", e);
					}
					return message;
				}
			});
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

	public void setQueue(Destination queue) {
		this.queue = queue;
	}
}