package com.sun.us.jms;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

public class App
{
    public static String brokerURL = "tcp://localhost:61616";

    public static void main( String[] args ) throws Exception
    {
        // setup the connection to ActiveMQ
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

        Producer producer = new Producer(factory, "rinkeshQueue");
        producer.run();
        producer.close();
    }
}