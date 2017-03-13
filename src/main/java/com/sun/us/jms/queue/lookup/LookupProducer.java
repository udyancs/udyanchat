package com.sun.us.jms.queue.lookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
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
    private Session session;
    private MessageProducer producer;

    public static void main( String[] args )
    {
        BufferedReader br = null;

        try {

            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("With whom you want to talk ?");
            String name = br.readLine();
            LookupProducer lookupProducer = new LookupProducer(name);
            System.out.println("Write message and click enter, to finish write finish");
            while (true) {

                //System.out.print("Write message : ");
                String input = br.readLine();

                if ("finish".equals(input)) {
                    System.out.println("Exit!");
                    System.exit(0);
                }

                lookupProducer.sendMessage(input);
                System.out.println("-----------\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    private void sendMessage(String text) {

        try {
            TextMessage message = this.session.createTextMessage();
            message.setText(text);
            //log.info("Sending message: " + message.getText());
            this.producer.send(message);
            //log.info("message sent");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LookupProducer(String person)
    {

        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");//8161
        props.setProperty("queue.MyQueue", person);

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
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) jndiContext.lookup("MyQueue");
            this.producer = session.createProducer(destination);

        }
        catch (Exception e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }
}
