package com.sun.us.jms.queue.lookup;

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

import com.sun.us.jms.JndiContaxtFactory;
import com.sun.us.jms.ChatType;

public class LookupProducer extends JndiContaxtFactory{

    private Session session;
    private MessageProducer producer;

    public static void main( String[] args )
    {
        BufferedReader br = null;
        final List<String> terminateCommand = Arrays.asList("finish", "end", "exit", "bye");


        try {

            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("With whom you want to talk ?");
            String name = br.readLine();
            LookupProducer lookupProducer = new LookupProducer(ChatType.QUEUE, name);
            System.out.println("Write message and click enter, to finish write finish");
            while (true) {

                String input = br.readLine();

                if (terminateCommand.contains(input)) {
                    lookupProducer.sendMessage("You take care bye");
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
            this.producer.send(message);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LookupProducer(ChatType type, String person)
    {
        super(type, person);
        try
        {

            ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            Connection connection = factory.createConnection();
            connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            String lookUpType = type.equals(ChatType.QUEUE) ? "MyQueue" : "MyTopic";
            Destination destination = (Destination) jndiContext.lookup(lookUpType);
            this.producer = session.createProducer(destination);

        }
        catch (Exception e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }
}
