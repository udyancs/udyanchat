package com.sun.us.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by udyan.shardhar on 3/10/17.
 */
public class SimpleConsumer implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleConsumer.class);

    public static void main(String[] args) {
        new SimpleConsumer().run();
    }
    public void run(){
        Connection connection;
        Session session;
        MessageConsumer consumer;

        try {
            final MyConnection myConnection =  MyConnectionFactory.createConnectionFactory();
            connection = myConnection.getConnectionFactory().createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(myConnection.getDestination());
            consumer.setMessageListener(this);
        } catch (JMSException | NamingException e) {
            LOG.info("Exception occurred: " + e);
        }

    }

    @Override
    public void onMessage(Message message) {
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
