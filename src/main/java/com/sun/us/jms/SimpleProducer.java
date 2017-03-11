package com.sun.us.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleProducer {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleProducer.class);

    /**
     * @param args the destination name to send to and optionally, the number of
     *                messages to send
     */
    public static void main(String[] args) {
        Connection connection = null;
        Session session;
        MessageProducer producer;

        try {
            final MyConnection myConnection =  MyConnectionFactory.createConnectionFactory();
            connection = myConnection.getConnectionFactory().createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(myConnection.getDestination());
            TextMessage message = session.createTextMessage();

                message.setText("This is message from Udyan");
                LOG.info("Sending message: " + message.getText());
                producer.send(message);


            /*
             * Send a non-text control message indicating end of messages.
             */
            producer.send(session.createMessage());
        } catch (JMSException | NamingException e) {
            LOG.info("Exception occurred: " + e);
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