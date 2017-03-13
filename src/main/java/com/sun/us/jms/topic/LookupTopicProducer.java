package com.sun.us.jms.topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
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

import com.sun.us.jms.queue.lookup.LookupConsumer;

public class LookupTopicProducer {

    private static final Log log = LogFactory.getLog(LookupConsumer.class);

    private Context jndiContext;
    private Session session;
    private MessageProducer producer;

    public static void main( String[] args )
    {
        BufferedReader br = null;
        final List<String> terminateCommand = Arrays.asList("finish", "end", "exit", "bye");

        try {

            br = new BufferedReader(new InputStreamReader(System.in));
            LookupTopicProducer lookupProducer = new LookupTopicProducer("topic.udyan");
            System.out.println("Write message and click enter, to finish write finish");
            while (true) {

                //System.out.print("Write message : ");
                String input = br.readLine();

                if (terminateCommand.contains(input)) {
                    lookupProducer.sendMessage("You take care bye");
                    System.out.println("Exit!");
                    System.exit(0);
                }

                if(!input.isEmpty()) {
                    lookupProducer.sendMessage(input);
                    System.out.println("-----------\n");
                }

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

    private LookupTopicProducer(String person)
    {

        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");//8161
        props.setProperty("topic.MyTopic", person);

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
            Destination destination = (Destination) jndiContext.lookup("MyTopic");
            this.producer = session.createProducer(destination);

        }
        catch (Exception e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }
}
