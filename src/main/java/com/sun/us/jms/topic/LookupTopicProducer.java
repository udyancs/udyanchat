package com.sun.us.jms.topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.sun.us.jms.ChatType;
import com.sun.us.jms.JndiContaxtFactory;

public class LookupTopicProducer  extends JndiContaxtFactory{

    private Session session;
    private MessageProducer producer;

    private LookupTopicProducer(ChatType chatType, String queueOrTopic)
    {
        super(chatType, queueOrTopic);
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

    public static void main( String[] args )
    {
        BufferedReader br = null;
        final List<String> terminateCommand = Arrays.asList("finish", "end", "exit", "bye");

        try {

            br = new BufferedReader(new InputStreamReader(System.in));
            final String topic = "topic.udyan";
            LookupTopicProducer lookupProducer = new LookupTopicProducer(ChatType.TOPIC, topic);
            System.out.println("Write message and click enter, to finish write finish");
            while (true) {

                //System.out.print("Write message : ");
                String input = br.readLine();

                if (terminateCommand.contains(input)) {
                    lookupProducer.sendMessage("You take care bye");
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
}
