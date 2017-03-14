package com.sun.us.jms.topic;

import com.sun.us.jms.ChatType;
import com.sun.us.jms.JndiContaxtFactory;

public class RinkeshConsumer extends JndiContaxtFactory {


    public RinkeshConsumer(ChatType chatType, String topic){
        super(chatType, topic);
    }

    public static void main( String[] args )
    {
        RinkeshConsumer lookupConsumer = new RinkeshConsumer(ChatType.TOPIC, "topic.udyan");
        lookupConsumer.startConsumer(ChatType.TOPIC);
    }
}
