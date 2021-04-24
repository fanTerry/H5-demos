package com.esportzoo.esport.controller.ws.jms;
import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.fastjson.JSON;

/**
 * 通用的消息发送类
 */
public class MessageProducer {
    protected transient final Logger logger = LoggerFactory.getLogger("jmsMessage");
    protected JmsTemplate jmsTemplate;

    /**
     * 使用jmsTemplate的send/MessageCreator()发送ObjectMessage类型的消息
     */
    public void sendMessage(final Serializable obj, Destination destination) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(obj);
                return message;
            }
        });
    }

    public void sendTxtJSONMessage(final Serializable obj, Destination destination) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();
                message.setText(JSON.toJSONString(obj));
                return message;
            }
        });
    }

    public void sendTxtMessage(final String text, Destination destination) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();
                message.setText(text);
                return message;
            }
        });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

}