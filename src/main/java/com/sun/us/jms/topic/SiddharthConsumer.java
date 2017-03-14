package com.sun.us.jms.topic;

import com.sun.us.jms.ChatType;
import com.sun.us.jms.JndiContaxtFactory;

public class SiddharthConsumer extends JndiContaxtFactory {

    private SiddharthConsumer(ChatType chatType, String topic) {
        super(chatType, topic);
    }

    public static void main( String[] args )
    {
        SiddharthConsumer lookupConsumer = new SiddharthConsumer(ChatType.TOPIC, "topic.udyan");
        lookupConsumer.startConsumer(ChatType.TOPIC);
    }

}
