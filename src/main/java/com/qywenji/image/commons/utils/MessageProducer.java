package com.qywenji.image.commons.utils;

import org.apache.activemq.command.ActiveMQDestination;
import org.springframework.jms.core.JmsTemplate;

/**
 * 消息生产者 
 */
public class MessageProducer {
    private static JmsTemplate jmsTemplate;
    private static ActiveMQDestination destination;

    public static void setDestination(ActiveMQDestination destination) {
        MessageProducer.destination = destination;
    }

    public static void setJmsTemplate(JmsTemplate jmsTemplate) {
        MessageProducer.jmsTemplate = jmsTemplate;
    }

    public static void send(Object object){
        jmsTemplate.convertAndSend(destination, object);
    }

}

