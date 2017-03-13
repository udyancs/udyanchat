package com.sun.us.jms.queue.lookup;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LookupConsumer implements MessageListener {

    private static final Log log = LogFactory.getLog(LookupConsumer.class);


    //public static String brokerURL = "tcp://192.168.0.3:61616";

    private Context jndiContext;

    public static void main( String[] args )
    {
        LookupConsumer lookupConsumer = new LookupConsumer();
        lookupConsumer.setup();
    }

    private void setup()
    {

        // Watch the Queue at http://localhost:8161/admin/
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        props.setProperty("queue.MyQueue", "rinkesh");

        try {
            jndiContext = new InitialContext(props);
        } catch (NamingException e) {
            log.info("Could not create JNDI API context: " + e.toString());
            System.exit(1);
        }

        try
        {

            ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) jndiContext.lookup("MyQueue");
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
        }
        catch (Exception e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }

    public void onMessage(Message message)
    {
        try
        {
            if (message instanceof TextMessage)
            {
                TextMessage txtMessage = (TextMessage)message;
                System.out.println("Message received: " + txtMessage.getText());
            }
            else
            {
                System.out.println("Invalid message received.");
            }
        }
        catch (JMSException e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }
}
