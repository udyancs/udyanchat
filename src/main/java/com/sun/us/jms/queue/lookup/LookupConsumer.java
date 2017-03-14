package com.sun.us.jms.queue.lookup;

import com.sun.us.jms.JndiContaxtFactory;
import com.sun.us.jms.ChatType;

public class LookupConsumer extends JndiContaxtFactory {

    private LookupConsumer(ChatType chatType, String topicQueue) {
        super(chatType, topicQueue);
    }

    public static void main( String[] args )
    {
        LookupConsumer lookupConsumer = new LookupConsumer(ChatType.QUEUE, "rinkesh");
        lookupConsumer.startConsumer(ChatType.QUEUE);
    }

}
