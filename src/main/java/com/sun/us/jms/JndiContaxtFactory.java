package com.sun.us.jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Created by udyan.shardhar on 3/13/17.
 */
@Component
public class JndiContaxtFactory {

    private static final Log log = LogFactory.getLog(JndiContaxtFactory.class);

    protected Context jndiContext;

    public JndiContaxtFactory(ChatType chatType, String topicQueue) {
        // Watch the Queue at http://localhost:8161/admin/
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        props.setProperty(chatType.getValue(), topicQueue);

        try {
            this.jndiContext = new InitialContext(props);
        } catch (NamingException e) {
            log.info("Could not create JNDI API context: " + e.toString());
            System.exit(1);
        }
    }

    protected void startConsumer(ChatType type){
        try
        {
            ConnectionFactory factory = (ConnectionFactory) this.jndiContext.lookup("ConnectionFactory");
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            String lookUpType = type.equals(ChatType.QUEUE) ? "MyQueue" : "MyTopic";
            Destination destination = (Destination) jndiContext.lookup(lookUpType);
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new ChatListener());
        }
        catch (Exception e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }
}
