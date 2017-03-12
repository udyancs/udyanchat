package com.sun.us.jms.lookup;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyConnectionFactory {

    private static final Logger log = LoggerFactory.getLogger(MyConnectionFactory.class);

    public static MyConnection createConnectionFactory() throws NamingException {
        Context jndiContext = null;
        MyConnection myConnection = null;

        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://192.168.0.3:61616");//8161
        props.setProperty("queue.xxx", "rinkeshQueue");

                            /*
         * Create a JNDI API InitialContext object
         */
            try {
                jndiContext = new InitialContext(props);
            } catch (NamingException e) {
                log.info("Could not create JNDI API context: " + e.toString());
                System.exit(1);
            }

        /*
         * Look up connection factory and destination.
         */
            try {
                ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
                Destination destination = (Destination) jndiContext.lookup("xxx");
                myConnection = new MyConnection(connectionFactory, destination);

            } catch (NamingException e) {
                log.info("JNDI API lookup failed: " + e);
            }


        return myConnection;
    }
}
