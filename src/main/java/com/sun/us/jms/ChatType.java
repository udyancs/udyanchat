package com.sun.us.jms;

/**
 * Created by udyan.shardhar on 3/13/17.
 */
public enum ChatType {
    TOPIC("topic.MyTopic"),
    QUEUE("queue.MyQueue");

    private String value;
    ChatType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
