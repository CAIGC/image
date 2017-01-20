package com.qywenji.image.commons.utils;

import org.apache.activemq.command.ActiveMQDestination;
import org.springframework.jms.core.JmsTemplate;

/**
 * 接受者
 */
public class MessageContainer {
    private static JmsTemplate jmsTemplate;
    private static ActiveMQDestination destination;

    public static void setDestination(ActiveMQDestination destination) {
        MessageContainer.destination = destination;
    }

    public static void setJmsTemplate(JmsTemplate jmsTemplate) {
        MessageContainer.jmsTemplate = jmsTemplate;
    }

}

