package com.sun.us.jms.topic;

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

import com.sun.us.jms.queue.lookup.LookupConsumer;

public class RinkeshConsumer implements MessageListener {

    private static final Log log = LogFactory.getLog(LookupConsumer.class);

    private Context jndiContext;

    public static void main( String[] args )
    {
        RinkeshConsumer lookupConsumer = new RinkeshConsumer();
        lookupConsumer.setup();
    }

    private void setup()
    {

        // Watch the Queue at http://localhost:8161/admin/
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        props.setProperty("topic.MyTopic", "topic.udyan");

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
            Destination destination = (Destination) jndiContext.lookup("MyTopic");
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
        }
        catch (Exception e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }

    @Override
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
