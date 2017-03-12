package com.sun.us.jms.lookup;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LookupProducer {

    private static final Log log = LogFactory.getLog(LookupConsumer.class);

    private Context jndiContext;
    private Connection connection;

    public static void main( String[] args )
    {
        LookupProducer lookupProducer = new LookupProducer();
        lookupProducer.sendMessage();

    }

    private void sendMessage()
    {

        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://192.168.0.3:61616");//8161
        props.setProperty("queue.MyQueue", "learning.queue");

        try {
            jndiContext = new InitialContext(props);
        } catch (NamingException e) {
            log.info("Could not create JNDI API context: " + e.toString());
            System.exit(1);
        }

        try
        {

            ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) jndiContext.lookup("MyQueue");
            MessageProducer producer = session.createProducer(destination);

            TextMessage message = session.createTextMessage();

            message.setText("This is message from Udyan");
            log.info("Sending message: " + message.getText());
            producer.send(message);
        }
        catch (Exception e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ignored) {
                }
            }
        }
    }
}
