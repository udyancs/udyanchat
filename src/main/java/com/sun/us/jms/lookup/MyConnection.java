package com.sun.us.jms.lookup;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

/**
 * Created by udyan.shardhar on 3/10/17.
 */
public class MyConnection {
    ConnectionFactory connectionFactory;
    Destination destination;

    public MyConnection(ConnectionFactory connectionFactory, Destination destination) {
        this.connectionFactory = connectionFactory;
        this.destination = destination;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public Destination getDestination() {
        return destination;
    }
}
